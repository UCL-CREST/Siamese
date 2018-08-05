    @SuppressWarnings("unchecked")
    public static Object forName(String s, Object[][] args) {
        try {
            System.out.println("trying to create a new instance of class [" + s + "] with arguments");
            java.lang.reflect.Constructor c = Class.forName(s).getConstructors()[0];
            return c.newInstance(args[c.getParameterTypes().length]);
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }
