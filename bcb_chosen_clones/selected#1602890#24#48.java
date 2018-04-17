    @SuppressWarnings("unchecked")
    public <T> T getOrCreateModel(Class<T> clazz, int pk, LocalThreadStorage storage) {
        Object model = _models.get(clazz);
        if (model == null) {
            if (pk == 0) {
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
            } else {
                model = _entityLoader.loadEntity(clazz, pk, storage);
            }
            _models.put(clazz, model);
        }
        return (T) model;
    }
