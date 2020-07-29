    protected Object getInstance() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (params.length == 0 && classTested.equals(HashSet.class)) {
            return Collections.synchronizedSet((Set<?>) classTested.newInstance());
        } else if (params.length == 1) {
            Constructor<?> constructor = classTested.getConstructor(int.class);
            return constructor.newInstance(((Integer) params[0]).intValue());
        } else {
            return super.getInstance();
        }
    }
