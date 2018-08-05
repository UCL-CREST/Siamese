    public static ModelInstance loadObjectProperties(RRL rrl) {
        ModelInstance result = null;
        URL url = Repository.get().getResource(rrl);
        Properties props = new Properties();
        try {
            props.load(url.openStream());
        } catch (IOException ex) {
            Logger.getLogger(CosmicLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        String type = props.getProperty("type");
        if (type == null) Logger.getLogger(CosmicLoader.class.getName()).severe("Object type not found in properties"); else {
            Class typeClass = null;
            try {
                typeClass = Class.forName(type);
            } catch (Exception ex) {
                Logger.getLogger(CosmicLoader.class.getName()).log(Level.SEVERE, "Unknown type: {0}\n{1}", new Object[] { type, ex });
            }
            if (typeClass != null) {
                ObjectComponent resultObject = null;
                try {
                    resultObject = (ObjectComponent) typeClass.getConstructor(Properties.class).newInstance(props);
                } catch (Exception ex) {
                    Logger.getLogger(CosmicLoader.class.getName()).log(Level.SEVERE, "Did not instantiate type: {0} due to: {1}", new Object[] { type, ex });
                }
                if (resultObject != null) {
                    result = resultObject.getModelInstance();
                    result.setExternalReference(rrl);
                }
            }
        }
        return result;
    }
