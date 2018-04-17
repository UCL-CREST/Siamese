    private Throwable createThrowable(String typeName, String message) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(typeName);
        } catch (Exception e) {
            LOGGER.warning("Unable to load Throwable class " + typeName);
            return null;
        }
        if (!Throwable.class.isAssignableFrom(clazz)) {
            LOGGER.warning("Type does not inherit from Throwable" + clazz.getName());
            return null;
        }
        @SuppressWarnings("unchecked") Class<? extends Throwable> tClazz = (Class<? extends Throwable>) clazz;
        Constructor<? extends Throwable> defaultCtr = null;
        Constructor<? extends Throwable> messageCtr = null;
        try {
            defaultCtr = tClazz.getConstructor();
        } catch (Throwable t) {
        }
        try {
            messageCtr = tClazz.getConstructor(String.class);
        } catch (Throwable t) {
        }
        if (message != null && messageCtr != null) {
            return messageCtr.newInstance(message);
        } else if (message != null && defaultCtr != null) {
            LOGGER.warning("Unable to invoke message constructor for " + clazz.getName());
            return defaultCtr.newInstance();
        } else if (message == null && defaultCtr != null) {
            return defaultCtr.newInstance();
        } else if (message == null && messageCtr != null) {
            LOGGER.warning("Passing null message to message constructor for " + clazz.getName());
            return messageCtr.newInstance((String) null);
        } else {
            LOGGER.warning("Unable to find a suitable constructor for " + clazz.getName());
            return null;
        }
    }
