    @SuppressWarnings("unchecked")
    public T getInstance(String className, Object... args) throws InstantiationException {
        T ret = null;
        FpmCollections<Class<?>> colls = FpmCollections.getOne(new ArrayList<Class<?>>());
        try {
            Class<?> dad = Class.forName(className);
            for (Object o : args) {
                colls.add(o.getClass());
            }
            Class<?>[] app = new Class<?>[] {};
            Constructor<?> c = dad.getConstructor(colls.toArray(app));
            ret = (T) c.newInstance(args);
        } catch (ClassNotFoundException e) {
            throw ErrorHandler.getOne(getClass()).<InstantiationException>rethrow(e);
        } catch (SecurityException e) {
            throw ErrorHandler.getOne(getClass()).<InstantiationException>rethrow(e);
        } catch (NoSuchMethodException e) {
            throw ErrorHandler.getOne(getClass()).<InstantiationException>rethrow(e);
        } catch (IllegalArgumentException e) {
            throw ErrorHandler.getOne(getClass()).<InstantiationException>rethrow(e);
        } catch (IllegalAccessException e) {
            throw ErrorHandler.getOne(getClass()).<InstantiationException>rethrow(e);
        } catch (InvocationTargetException e) {
            throw ErrorHandler.getOne(getClass()).<InstantiationException>rethrow(e);
        }
        return ret;
    }
