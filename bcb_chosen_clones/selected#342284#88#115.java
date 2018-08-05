    public void initContentPane(String id) {
        Class<? extends JPanel> cls = availablePanes.get(id);
        if (cls != null) {
            try {
                Constructor<? extends JPanel> constructor = cls.getConstructor(new Class[] {});
                jContentPane = constructor.newInstance(new Object[] {});
            } catch (SecurityException e) {
                JOptionPane.showMessageDialog(null, ERROR_MSG + e.getClass().getSimpleName() + ": " + e.getMessage(), ERROR_HEAD, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                JOptionPane.showMessageDialog(null, ERROR_MSG + e.getClass().getSimpleName() + ": " + e.getMessage(), ERROR_HEAD, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, ERROR_MSG + e.getClass().getSimpleName() + ": " + e.getMessage(), ERROR_HEAD, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (InstantiationException e) {
                JOptionPane.showMessageDialog(null, ERROR_MSG + e.getClass().getSimpleName() + ": " + e.getMessage(), ERROR_HEAD, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                JOptionPane.showMessageDialog(null, ERROR_MSG + e.getClass().getSimpleName() + ": " + e.getMessage(), ERROR_HEAD, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                JOptionPane.showMessageDialog(null, ERROR_MSG + e.getClass().getSimpleName() + ": " + e.getMessage(), ERROR_HEAD, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        initialize();
    }
