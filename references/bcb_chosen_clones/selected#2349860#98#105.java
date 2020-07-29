    public AbstractItem getWorldObject(WorldObjectProperties woProperties, int amount) throws WorldObjectFactoryException {
        @SuppressWarnings("unchecked") Class<? extends AbstractItem> woClass = (Class<? extends AbstractItem>) worldObjectMapper.get(woProperties.getType());
        try {
            return woClass.getConstructor(woProperties.getClass(), int.class).newInstance(woProperties, amount);
        } catch (Exception e) {
            throw new WorldObjectFactoryException(e);
        }
    }
