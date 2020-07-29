    protected TreeBuilder createTreeBuilder(Properties properties, Feature... features) {
        Class<?> clazz = load(TreeBuilder.class, properties);
        if (clazz == null) {
            return new Builder(features);
        }
        try {
            if (Builder.class.isAssignableFrom(clazz)) {
                Constructor<?> constructor = clazz.getConstructor(Feature[].class);
                if (constructor == null) {
                    if (features == null || features.length == 0) {
                        return TreeBuilder.class.cast(clazz.newInstance());
                    } else {
                        throw new ELException("Builder " + clazz + " is missing constructor (can't pass features)");
                    }
                } else {
                    return TreeBuilder.class.cast(constructor.newInstance((Object) features));
                }
            } else {
                return TreeBuilder.class.cast(clazz.newInstance());
            }
        } catch (Exception e) {
            throw new ELException("TreeBuilder " + clazz + " could not be instantiated", e);
        }
    }
