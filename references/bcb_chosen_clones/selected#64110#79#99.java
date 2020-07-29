    public AbstractNode create(Class<? extends AbstractNode> className, String uniqueId) throws Exception {
        if (registry.get(uniqueId) != null) {
            if (!className.getSimpleName().equals(registry.get(uniqueId).getClass().getSimpleName())) {
                if (Protein.class.isAssignableFrom(className) && registry.get(uniqueId) instanceof Compound) {
                } else if (Compound.class.isAssignableFrom(className) && registry.get(uniqueId) instanceof Protein) {
                    return registry.get(uniqueId);
                } else {
                    throw new Exception("uniqueId was already given to another sink class type :" + registry.get(uniqueId).getClass().getSimpleName() + " to " + className.getSimpleName() + " for " + uniqueId);
                }
            } else {
                return registry.get(uniqueId);
            }
        }
        if (className.getConstructors().length != 1 || className.getConstructors()[0].getParameterTypes().length != 0) {
            throw new Exception("Sink object constructor doesn't match. Caused by:" + className);
        }
        AbstractNode node = (AbstractNode) className.getConstructors()[0].newInstance((Object[]) null);
        node.setUniqueId(uniqueId);
        this.register(node);
        return node;
    }
