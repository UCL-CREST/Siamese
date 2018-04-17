    public <T extends Unit> T buildNewUnit(String propertiesFile, Class<T> unitType) throws Exception {
        Constructor<T> constructor = unitType.getConstructor(String.class);
        T newUnit = constructor.newInstance(propertiesFile);
        units.add(newUnit);
        return newUnit;
    }
