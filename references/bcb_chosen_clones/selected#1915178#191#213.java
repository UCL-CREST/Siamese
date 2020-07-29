    @SuppressWarnings("rawtypes")
    public Entity getEntityFromPrototype(String entityId, String entityName) throws NVFrameException {
        if (!prototypeEntityComp.containsKey(entityId) || !prototypeEntitySettings.containsKey(entityId)) throw new NVFrameException("cannot find entity [entityId: " + entityId + "]");
        Map<String, SettingsObj> components = prototypeEntityComp.get(entityId);
        SettingsObj settings = prototypeEntitySettings.get(entityId);
        Class<? extends Entity> cls = prototypeEntityCls.get(entityId);
        Entity entity = null;
        Constructor constr;
        try {
            constr = cls.getConstructor(String.class, String.class, SettingsObj.class);
            entity = (Entity) constr.newInstance(entityId, entityName, settings);
        } catch (Exception e) {
            throw new NVFrameException("Could not instantiate entity object with id: " + entityId, e);
        }
        for (String component_id : components.keySet()) {
            SettingsObj componentSettings = components.get(component_id);
            AbstractComponent comp = ComponentFactory.getInstance().getComponent(component_id, componentSettings, entity);
            entity.addComponent(comp);
        }
        EntityManager.getInstance().addEntity(entity);
        entity.initialize();
        return entity;
    }
