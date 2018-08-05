    private ERXBrowser _createBrowserWithClassName(String className, String browserName, String version, String mozillaVersion, String platform, NSDictionary userInfo) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        Class browserClass = Class.forName(className);
        Class[] paramArray = new Class[] { String.class, String.class, String.class, String.class, NSDictionary.class };
        java.lang.reflect.Constructor constructor = browserClass.getConstructor(paramArray);
        return (ERXBrowser) constructor.newInstance(new Object[] { browserName, version, mozillaVersion, platform, userInfo });
    }
