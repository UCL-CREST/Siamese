    @SuppressWarnings("unchecked")
    private Class<Loader> getLoaderClass(String loaderClassName) {
        try {
            Class<Loader> loaderClass = (Class<Loader>) getClass().getClassLoader().loadClass(loaderClassName);
            if (!Loader.class.isAssignableFrom(loaderClass)) {
                throw new IllegalArgumentException(loaderClassName + " not a subclass of " + Loader.class.getName());
            } else if (Modifier.isAbstract(loaderClass.getModifiers()) || !Modifier.isPublic(loaderClass.getModifiers())) {
                throw new IllegalArgumentException(loaderClassName + " not a public static class");
            }
            Constructor<Loader> constructor = loaderClass.getConstructor(new Class[0]);
            constructor.newInstance(new Object[0]);
            return loaderClass;
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(loaderClassName + " constructor not accessible");
        } catch (InstantiationException ex) {
            throw new IllegalArgumentException(loaderClassName + " not a public static class");
        }
    }
