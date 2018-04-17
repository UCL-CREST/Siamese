    protected void initialExtensions(E instance) {
        Contexts.getConversationContext().set(getInstanceName(), getInstance());
        try {
            Class<?> clazz = instance.getClass();
            String pkgName = clazz.getPackage().getName();
            String simpleName = clazz.getSimpleName();
            String className = pkgName + ".editor." + simpleName + "TreeNode";
            Class<EntityTreeNode<E>> entityTreeNodeClass;
            entityTreeNodeClass = (Class<EntityTreeNode<E>>) Class.forName(className);
            Constructor<EntityTreeNode<E>> entityTreeNodeConstructor = entityTreeNodeClass.getConstructor(new Class[] { TreeNode.class, clazz });
            extensions = entityTreeNodeConstructor.newInstance(null, instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RootTreeNode.instance().init(extensions);
    }
