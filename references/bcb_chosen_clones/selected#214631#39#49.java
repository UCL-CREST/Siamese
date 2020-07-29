    @SuppressWarnings("unchecked")
    public static <T extends UserBackedFilter> T filterFactory(Object enclosingInstance, Class<T> clazz, User user) {
        try {
            Constructor<?> c = clazz.getConstructors()[0];
            T userBackedFilter = (T) c.newInstance(enclosingInstance);
            userBackedFilter.setUser(user);
            return userBackedFilter;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to create filter ", e);
        }
    }
