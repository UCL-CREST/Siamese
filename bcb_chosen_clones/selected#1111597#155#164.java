    private static TreeViewTreeNode newInstance(String className, Object userObject) {
        try {
            @SuppressWarnings("unchecked") Class<? extends TreeViewTreeNode> clazz = (Class<? extends TreeViewTreeNode>) Class.forName(className);
            Constructor<? extends TreeViewTreeNode> c = clazz.getConstructor(Object.class);
            return c.newInstance(userObject);
        } catch (Exception e) {
            Debug.println(e);
            throw (RuntimeException) new IllegalStateException().initCause(e);
        }
    }
