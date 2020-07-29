    public static Object stringToArray(Class cl, String val) {
        Object ret = null;
        StringTokenizer st = new StringTokenizer(val, "[()]", true);
        String token = null;
        boolean lastWasStart = false;
        try {
            Vector vec = new Vector();
            if (!st.hasMoreTokens()) {
                return new Object[0];
            }
            st.nextToken();
            String className = st.nextToken();
            Class c = null;
            try {
                Object primitive = (Object) primitives.get(className);
                if (primitive == null) {
                    c = Class.forName(className);
                } else {
                    c = primitive.getClass();
                }
                Class[] params = { String.class };
                Constructor constructor = c.getConstructor(params);
                Object[] args = new Object[1];
                Constructor emptyConstructor = null;
                Object[] noArgs = {};
                try {
                    emptyConstructor = c.getConstructor(new Class[] {});
                } catch (Exception e) {
                }
                while (st.hasMoreElements()) {
                    token = st.nextToken();
                    if (token.equals("(")) {
                        lastWasStart = true;
                    } else if (token.equals(")")) {
                        if (lastWasStart && emptyConstructor != null) {
                            vec.addElement(emptyConstructor.newInstance(noArgs));
                        }
                        lastWasStart = false;
                    } else if (token.equals("]")) {
                    } else {
                        args[0] = token;
                        vec.addElement(constructor.newInstance(args));
                        lastWasStart = false;
                    }
                }
                if (primitive != null) {
                    ret = Array.newInstance((Class) primitive.getClass().getField("TYPE").get(null), vec.size());
                } else {
                    ret = Array.newInstance(c, vec.size());
                }
                for (int i = 0; i < vec.size(); i++) {
                    Array.set(ret, i, vec.elementAt(i));
                }
            } catch (Exception e) {
                Logger.logError("Error constructing array", e);
            }
        } catch (NoSuchElementException e) {
            Logger.logError("Error processing " + val, e);
        }
        return ret;
    }
