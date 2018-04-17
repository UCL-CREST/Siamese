    <T> T getInstance(String key, Class<?> cls, Class<T> type, Class<?>[] argTypes, Object[] args, String id) throws Exception {
        Object o = null;
        Constructor<?> ctor = null;
        if (cls == null) {
            return null;
        }
        if (id != null) {
            if (singletons == null) {
                singletons = new HashMap<String, Object>();
            } else {
                o = type.cast(singletons.get(id));
            }
        }
        while (o == null) {
            try {
                ctor = cls.getConstructor(argTypes);
                o = ctor.newInstance(args);
            } catch (NoSuchMethodException nmx) {
                if ((argTypes.length > 1) || ((argTypes.length == 1) && (argTypes[0] != Config.class))) {
                    argTypes = CONFIG_ARGTYPES;
                    args = CONFIG_ARGS;
                } else if (argTypes.length > 0) {
                    argTypes = NO_ARGTYPES;
                    args = NO_ARGS;
                } else {
                    throw new Exception(key, cls, "no suitable ctor found");
                }
            } catch (IllegalAccessException iacc) {
                throw new Exception(key, cls, "\n> ctor not accessible: " + getMethodSignature(ctor));
            } catch (IllegalArgumentException iarg) {
                throw new Exception(key, cls, "\n> illegal constructor arguments: " + getMethodSignature(ctor));
            } catch (InvocationTargetException ix) {
                Throwable tx = ix.getTargetException();
                if (tx instanceof Config.Exception) {
                    throw new Exception(tx.getMessage() + "\n> used within \"" + key + "\" instantiation of " + cls);
                } else {
                    throw new Exception(key, cls, "\n> exception in " + getMethodSignature(ctor) + ":\n>> " + tx, tx);
                }
            } catch (InstantiationException ivt) {
                throw new Exception(key, cls, "\n> abstract class cannot be instantiated");
            } catch (ExceptionInInitializerError eie) {
                throw new Exception(key, cls, "\n> static initialization failed:\n>> " + eie.getException(), eie.getException());
            }
        }
        if (!type.isInstance(o)) {
            throw new Exception(key, cls, "\n> instance not of type: " + type.getName());
        }
        if (id != null) {
            singletons.put(id, o);
        }
        return type.cast(o);
    }
