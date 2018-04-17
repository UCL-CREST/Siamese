    protected void addLocalHandler(String name, Class defaultHandlerClass) {
        String handlerClassName = properties.get(handlerPropertyPrefix + name + ".class");
        Class handlerClass = null;
        try {
            handlerClass = Class.forName(handlerClassName);
        } catch (Exception e) {
            try {
                handlerClass = Class.forName(defaultHandlerPackage + handlerClassName);
            } catch (Exception ee) {
                handlerClass = defaultHandlerClass;
            }
        }
        try {
            Class types[] = { String.class, String.class };
            Constructor constructor = handlerClass.getConstructor(types);
            Object params[] = { handlerPropertyPrefix, name };
            Handler handler = (Handler) constructor.newInstance(params);
            lookupFilterAndAddHandler(handler, name);
            checkAndBindHandler(name, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
