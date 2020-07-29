    public void updateVal(Configuration config) {
        updateValSuper(config);
        String value = field.getText();
        Class[] params = new Class[1];
        Object[] args = new Object[1];
        params[0] = String.class;
        args[0] = value;
        Object o = null;
        try {
            Constructor c = mObj.getClass().getConstructor(params);
            o = c.newInstance(args);
        } catch (Exception ex) {
            throw new IllegalStateException("Problem writing unknown class to configuration");
        }
        config.put(key, o);
    }
