    public static Page newInstance(String className) throws Exception {
        Class<?> pageClass = Class.forName(className);
        return (Page) pageClass.getConstructor().newInstance();
    }
