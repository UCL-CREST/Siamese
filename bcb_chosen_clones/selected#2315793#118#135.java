    public Object newModelInstance() {
        Class<?> clazz = _getter.getDeclaringClass();
        Object model = null;
        try {
            Constructor<?> ctor = clazz.getConstructor(new Class<?>[] {});
            boolean accessible = ctor.isAccessible();
            if (!accessible) {
                ctor.setAccessible(true);
            }
            model = ctor.newInstance(new Object[] {});
            if (!accessible) {
                ctor.setAccessible(false);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get model default constructor: " + clazz.getName());
        }
        return model;
    }
