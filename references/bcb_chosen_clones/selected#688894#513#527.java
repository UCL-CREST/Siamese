    public <C extends FreelordsEntity> C getCreateEntity(Class<C> clazz, EntityId id) {
        FreelordsEntity entity = entityMap.get(id);
        if (entity != null) {
            return clazz.cast(entity);
        } else {
            try {
                Constructor<C> construct = clazz.getConstructor(EntityId.class);
                C ent = construct.newInstance(id);
                entityMap.put(id, ent);
                return ent;
            } catch (Exception e) {
                throw new RuntimeException("Error when creating " + clazz + " with id " + id, e);
            }
        }
    }
