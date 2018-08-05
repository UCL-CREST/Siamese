    public static Object newInstance(String className, String p1) {
        try {
            Class classObj = Class.forName(className);
            Constructor classConstructor = classObj.getConstructor(new Class[] { String.class });
            Object[] args = new Object[] { p1 };
            return classConstructor.newInstance(args);
        } catch (Exception e) {
            _log.error(e, e);
            return null;
        }
    }
