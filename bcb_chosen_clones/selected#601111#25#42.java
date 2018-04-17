    @SuppressWarnings("unchecked")
    @Override
    public void configClassifier(XMLConfiguration config, String section) {
        super.configClassifier(config, section);
        Configuration methodConf = config.configurationAt(section);
        try {
            knnName = CommonUtils.getFromConfIfNotNull(methodConf, "knn", knnName);
            Class c = Class.forName(knnName);
            Constructor[] a = c.getConstructors();
            for (Constructor con : a) {
                if (con.getParameterTypes().length == 0) {
                    knnModel = (NearestNeighbourSearch) con.newInstance();
                }
            }
        } catch (Exception e) {
        }
        N = CommonUtils.getIntFromConfIfNotNull(methodConf, "N", N);
    }
