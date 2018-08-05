    public String execute(String script) {
        Class NSAppleScriptClass = null;
        Class NSMutableDictionaryClass = null;
        try {
            NSAppleScriptClass = Class.forName("com.apple.cocoa.foundation.NSAppleScript");
        } catch (ClassNotFoundException e) {
            handle(e, "Trouble creating class 'com.apple.cocoa.foundation.NSAppleScript'");
        }
        try {
            NSMutableDictionaryClass = Class.forName("com.apple.cocoa.foundation.NSMutableDictionary");
        } catch (ClassNotFoundException e) {
            handle(e, "Trouble creating class 'com.apple.cocoa.foundation.NSMutableDictionary'");
        }
        try {
            Object myScript = NSAppleScriptClass.getConstructor(new Class[] { String.class }).newInstance(new Object[] { script });
            Object errors = NSMutableDictionaryClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            Object results = NSAppleScriptClass.getMethod("execute", new Class[] { NSMutableDictionaryClass }).invoke(myScript, new Object[] { errors });
            if (results == null) return null;
            return String.valueOf(results.getClass().getMethod("stringValue", new Class[0]).invoke(results, new Object[0]));
        } catch (IllegalAccessException e) {
            handle(e, "Trouble executing script");
        } catch (NoSuchMethodException e) {
            handle(e, "Trouble executing script");
        } catch (InstantiationException e) {
            handle(e, "Trouble executing script");
        } catch (InvocationTargetException e) {
            handle(e, "Trouble executing script");
        }
        return null;
    }
