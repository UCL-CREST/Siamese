    public WorldObject getWorldObject(WorldObjectProperties woProperties) throws WorldObjectFactoryException {
        Class<? extends WorldObject> woClass = worldObjectMapper.get(woProperties.getType());
        try {
            return woClass.getConstructor(woProperties.getClass()).newInstance(woProperties);
        } catch (Exception e) {
            throw new WorldObjectFactoryException(e);
        }
    }
