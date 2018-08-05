    private static Layout getInstance(String className, Object[] list) throws Exception {
        Constructor method = getConstructor(className);
        return (Layout) method.newInstance(list);
    }
