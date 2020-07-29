    private <T> T delegate(Class<T> clazz, T target) {
        try {
            Class<?> generatedClass = create(clazz);
            @SuppressWarnings("unchecked") Constructor<T> constructor = (Constructor<T>) generatedClass.getConstructor(clazz);
            return constructor.newInstance(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
