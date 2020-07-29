    @SuppressWarnings("unchecked")
    private static BoundPropertyRenderer createRenderer(BoundProperty prop, Class cls) {
        try {
            Class[] clsses = { BoundProperty.class };
            return (BoundPropertyRenderer) cls.getConstructor(clsses).newInstance(new Object[] { prop });
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Unable to create renderer for property: " + prop.getName() + ". Creating default renderer.");
        }
        return new DefaultRenderer(prop);
    }
