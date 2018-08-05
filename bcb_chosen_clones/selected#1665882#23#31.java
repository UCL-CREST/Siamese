    @Override
    protected Object doCreateObject(Element element) {
        try {
            Constructor<?> constructor = getClassType().getConstructor(Prop.class);
            return constructor.newInstance(XMLConverterRegistry.getObject(element.getChild("prop")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
