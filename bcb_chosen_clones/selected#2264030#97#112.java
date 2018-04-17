    private Bridge createBridge(final Component c, final BridgedPenListener bpl) {
        if (isJPenInstalled()) {
            try {
                final Class<? extends Bridge> clazz = (Class<? extends Bridge>) Class.forName("jpen.bridge.JPenBridge");
                if (null != clazz) {
                    return clazz.getConstructor(BridgedPenManager.class, Component.class, BridgedPenListener.class).newInstance(this, c, bpl);
                }
            } catch (ClassNotFoundException e) {
            } catch (InvocationTargetException e) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        return new MouseBridge(this, c, bpl);
    }
