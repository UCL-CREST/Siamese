    public static MappingResultIterator getMRIinstance(String mappingSrc, Map mappingMethodMap, String mappingMethod) {
        MappingResultIterator mri = null;
        try {
            Class mriClass = (Class) mappingMethodMap.get(mappingMethod);
            Class[] mriConstructorParameter = { String.class };
            Constructor mriConstructor = mriClass.getConstructor(mriConstructorParameter);
            Object[] mriparameter = { mappingSrc };
            mri = (MappingResultIterator) mriConstructor.newInstance(mriparameter);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return mri;
    }
