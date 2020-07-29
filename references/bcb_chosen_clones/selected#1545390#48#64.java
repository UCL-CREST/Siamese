    Framework createImpl0() {
        try {
            Class implClazz = getImplClass();
            Constructor constructor = implClazz.getConstructor(new Class[] { Map.class });
            return (Framework) constructor.newInstance(new Object[] { configuration });
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(implName);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
