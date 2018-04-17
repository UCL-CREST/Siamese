    @SuppressWarnings("unchecked")
    public static <S, R> ServiceLoader<R> load(Class<S> service, Class<R> result, ClassLoader loader, Condition<String[]> filter) {
        Service s = service.getAnnotation(Service.class);
        Class<? extends ServiceLoader> sl = null;
        if (s != null) {
            sl = s.loader();
            if (ServiceLoader.class.equals(sl)) sl = null;
        }
        if (sl == null) sl = SimpleServiceLoader.class;
        try {
            return sl.getConstructor(ServiceInfo.class).newInstance(new InfoImpl<S, R>(loader, filter, result, service, false));
        } catch (Exception e) {
            throw new RuntimeException("Error invoking loader " + sl.getName() + " for service " + service.getName(), e);
        }
    }
