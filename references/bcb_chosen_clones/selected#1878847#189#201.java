    public Object callConstructor(Class c, Class[] classes, Object[] args) {
        Constructor con = null;
        try {
            con = c.getConstructor(classes);
        } catch (Exception e) {
            throw new RuntimeException("Error locating constructor: " + c);
        }
        try {
            return con.newInstance(args);
        } catch (Exception e1) {
            throw new RuntimeException("Error calling constructor: " + c);
        }
    }
