    @SuppressWarnings("unchecked")
    @Override
    public void configClassifier(XMLConfiguration config, String section) {
        Configuration methodConf = config.configurationAt(section);
        try {
            classifierName = CommonUtils.getFromConfIfNotNull(methodConf, "classifier", classifierName);
            Class c = Class.forName(classifierName);
            Constructor[] a = c.getConstructors();
            cModel = (Classifier) a[0].newInstance();
        } catch (Exception e) {
        }
        try {
            wantsNumericClass = methodConf.getBoolean("wantsNumericClass", wantsNumericClass);
        } catch (Exception e) {
        }
    }
