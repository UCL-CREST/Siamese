    private static boolean loadClasses() {
        switch(jvm) {
            case MRJ_2_0:
                try {
                    Class aeTargetClass = Class.forName("com.apple.MacOS.AETarget");
                    Class osUtilsClass = Class.forName("com.apple.MacOS.OSUtils");
                    Class appleEventClass = Class.forName("com.apple.MacOS.AppleEvent");
                    Class aeClass = Class.forName("com.apple.MacOS.ae");
                    aeDescClass = Class.forName("com.apple.MacOS.AEDesc");
                    aeTargetConstructor = aeTargetClass.getDeclaredConstructor(new Class[] { int.class });
                    appleEventConstructor = appleEventClass.getDeclaredConstructor(new Class[] { int.class, int.class, aeTargetClass, int.class, int.class });
                    aeDescConstructor = aeDescClass.getDeclaredConstructor(new Class[] { String.class });
                    makeOSType = osUtilsClass.getDeclaredMethod("makeOSType", new Class[] { String.class });
                    putParameter = appleEventClass.getDeclaredMethod("putParameter", new Class[] { int.class, aeDescClass });
                    sendNoReply = appleEventClass.getDeclaredMethod("sendNoReply", new Class[] {});
                    Field keyDirectObjectField = aeClass.getDeclaredField("keyDirectObject");
                    keyDirectObject = (Integer) keyDirectObjectField.get(null);
                    Field autoGenerateReturnIDField = appleEventClass.getDeclaredField("kAutoGenerateReturnID");
                    kAutoGenerateReturnID = (Integer) autoGenerateReturnIDField.get(null);
                    Field anyTransactionIDField = appleEventClass.getDeclaredField("kAnyTransactionID");
                    kAnyTransactionID = (Integer) anyTransactionIDField.get(null);
                } catch (ClassNotFoundException cnfe) {
                    errorMessage = cnfe.getMessage();
                    return false;
                } catch (NoSuchMethodException nsme) {
                    errorMessage = nsme.getMessage();
                    return false;
                } catch (NoSuchFieldException nsfe) {
                    errorMessage = nsfe.getMessage();
                    return false;
                } catch (IllegalAccessException iae) {
                    errorMessage = iae.getMessage();
                    return false;
                }
                break;
            case MRJ_2_1:
                try {
                    mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
                    mrjOSTypeClass = Class.forName("com.apple.mrj.MRJOSType");
                    Field systemFolderField = mrjFileUtilsClass.getDeclaredField("kSystemFolderType");
                    kSystemFolderType = systemFolderField.get(null);
                    findFolder = mrjFileUtilsClass.getDeclaredMethod("findFolder", new Class[] { mrjOSTypeClass });
                    getFileCreator = mrjFileUtilsClass.getDeclaredMethod("getFileCreator", new Class[] { File.class });
                    getFileType = mrjFileUtilsClass.getDeclaredMethod("getFileType", new Class[] { File.class });
                } catch (ClassNotFoundException cnfe) {
                    errorMessage = cnfe.getMessage();
                    return false;
                } catch (NoSuchFieldException nsfe) {
                    errorMessage = nsfe.getMessage();
                    return false;
                } catch (NoSuchMethodException nsme) {
                    errorMessage = nsme.getMessage();
                    return false;
                } catch (SecurityException se) {
                    errorMessage = se.getMessage();
                    return false;
                } catch (IllegalAccessException iae) {
                    errorMessage = iae.getMessage();
                    return false;
                }
                break;
            case MRJ_3_0:
                try {
                    Class linker = Class.forName("com.apple.mrj.jdirect.Linker");
                    Constructor constructor = linker.getConstructor(new Class[] { Class.class });
                    constructor.newInstance(new Object[] { BrowserLauncher.class });
                } catch (ClassNotFoundException cnfe) {
                    errorMessage = cnfe.getMessage();
                    return false;
                } catch (NoSuchMethodException nsme) {
                    errorMessage = nsme.getMessage();
                    return false;
                } catch (InvocationTargetException ite) {
                    errorMessage = ite.getMessage();
                    return false;
                } catch (InstantiationException ie) {
                    errorMessage = ie.getMessage();
                    return false;
                } catch (IllegalAccessException iae) {
                    errorMessage = iae.getMessage();
                    return false;
                }
                break;
            case MRJ_3_1:
                try {
                    mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
                    openURL = mrjFileUtilsClass.getDeclaredMethod("openURL", new Class[] { String.class });
                } catch (ClassNotFoundException cnfe) {
                    errorMessage = cnfe.getMessage();
                    return false;
                } catch (NoSuchMethodException nsme) {
                    errorMessage = nsme.getMessage();
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }
