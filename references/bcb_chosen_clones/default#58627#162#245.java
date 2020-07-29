    private void showURLWithMethod(String url, String clazz, String method) {
        Class c = null;
        Object obj = null;
        try {
            c = Class.forName(clazz);
        } catch (Throwable e) {
            GUIUtilities.error(null, "infoviewer.error.classnotfound", new Object[] { clazz });
            return;
        }
        if (method == null) {
            Constructor constr = null;
            try {
                constr = c.getConstructor(new Class[] { URL.class });
                obj = constr.newInstance(new Object[] { new URL(url) });
            } catch (Exception ex) {
                Log.log(Log.DEBUG, this, ex);
            }
            if (obj == null) {
                try {
                    constr = c.getConstructor(new Class[] { String.class });
                    obj = constr.newInstance(new Object[] { url });
                } catch (Exception ex) {
                    Log.log(Log.DEBUG, this, ex);
                }
            }
            if (obj == null) {
                try {
                    constr = c.getConstructor(new Class[0]);
                    obj = constr.newInstance(new Object[0]);
                } catch (Exception ex) {
                    Log.log(Log.DEBUG, this, ex);
                }
            }
            if (obj == null) {
                GUIUtilities.error(null, "infoviewer.error.classnotfound", new Object[] { clazz });
                return;
            }
        } else {
            Method meth = null;
            boolean ok = false;
            try {
                meth = c.getDeclaredMethod(method, new Class[] { URL.class });
                obj = meth.invoke(null, new Object[] { new URL(url) });
                ok = true;
            } catch (Exception ex) {
                Log.log(Log.DEBUG, this, ex);
            }
            if (!ok) {
                try {
                    meth = c.getDeclaredMethod(method, new Class[] { String.class });
                    obj = meth.invoke(null, new Object[] { url });
                    ok = true;
                } catch (Exception ex) {
                    Log.log(Log.DEBUG, this, ex);
                }
            }
            if (!ok) {
                try {
                    meth = c.getDeclaredMethod(method, new Class[0]);
                    obj = meth.invoke(null, new Object[0]);
                    ok = true;
                } catch (Exception ex) {
                    Log.log(Log.DEBUG, this, ex);
                }
            }
            if (!ok) {
                GUIUtilities.error(null, "infoviewer.error.methodnotfound", new Object[] { clazz, method });
                return;
            }
        }
        if (obj != null) {
            if (obj instanceof JComponent) {
                JFrame f = new JFrame("Infoviewer JWrapper");
                f.getContentPane().add((JComponent) obj);
                f.pack();
                f.setVisible(true);
            } else if (obj instanceof Component) {
                Frame f = new Frame("Infoviewer Wrapper");
                f.add((Component) obj);
                f.pack();
                f.setVisible(true);
            }
        }
    }
