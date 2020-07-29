    public Filesystem addFilesystemOpt(String filesystemClass, Object... args) {
        Class<?> clazz;
        Constructor<?> constructor;
        Filesystem filesystem;
        try {
            clazz = Class.forName(filesystemClass);
        } catch (NoClassDefFoundError e) {
            return null;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("class not found: " + filesystemClass, e);
        }
        try {
            constructor = null;
            for (Constructor<?> c : clazz.getConstructors()) {
                if (Reflect.matches(c.getParameterTypes(), args)) {
                    if (constructor != null) {
                        throw new IllegalArgumentException("constructor ambiguous");
                    }
                    constructor = c;
                }
            }
            if (constructor == null) {
                throw new IllegalArgumentException("no constructor: " + filesystemClass);
            }
            try {
                filesystem = (Filesystem) constructor.newInstance(args);
            } catch (InvocationTargetException e) {
                return null;
            }
            addFilesystem(filesystem);
            return filesystem;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("cannot instantiate " + filesystemClass, e);
        }
    }
