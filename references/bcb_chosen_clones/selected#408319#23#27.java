    @Override
    public Object create(String key) throws Exception {
        key = key.trim();
        return ((Class) this.database.get(key)).getConstructor().newInstance();
    }
