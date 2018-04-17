    public static <T> T get(Class<T> type) {
        Object instance = registry.get((Class) type);
        if (instance == null) {
            if (type.isPrimitive()) throw new IllegalArgumentException("Class is primitive: " + type);
            final int modifiers = type.getModifiers();
            if (!Modifier.isPublic(modifiers)) throw new IllegalArgumentException("Class is not public: " + type);
            if (type.isInterface()) throw new IllegalArgumentException("Class is an interface: " + type);
            if (Modifier.isAbstract(modifiers)) throw new IllegalArgumentException("Class is abstract: " + type);
            try {
                Class.forName(type.getName());
                instance = registry.get((Class) type);
                if (instance != null) return (T) instance;
            } catch (ClassNotFoundException ex) {
            }
            Constructor ctor;
            try {
                ctor = type.getConstructor((Class[]) null);
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException("Class implements no public default constructor: " + type, ex);
            }
            try {
                instance = registry.putIfAbsentAndGet((Class) type, ctor.newInstance((Object[]) null));
            } catch (Error ex) {
                throw ex;
            } catch (RuntimeException ex) {
                throw ex;
            } catch (InstantiationException ex) {
                throw new IllegalArgumentException("Exception invoking constructor: " + ctor, ex);
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException("Constructor is not accessible: " + ctor, ex);
            } catch (InvocationTargetException ex) {
                throw new IllegalArgumentException("Exception thrown by constructor: " + ctor, ex);
            }
        }
        return (T) instance;
    }
