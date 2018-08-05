    private void _extract(JCas view, FeatureStructure featureStructure, String[] pathMembers, List<Object> pathValues) throws CleartkExtractorException {
        if (pathMembers.length == 1) {
            Feature feature = featureStructure.getType().getFeatureByBaseName(pathMembers[0]);
            if (feature == null) {
                return;
            }
            Type featureType = feature.getRange();
            if (featureType.isPrimitive()) {
                Object pathValue = getPrimitiveFeatureValue(view, featureStructure, feature);
                if (pathValue != null) pathValues.add(pathValue);
            } else if (typeSystem.subsumes(typeSystem.getType("uima.tcas.Annotation"), featureType)) {
                String coveredText = ((Annotation) featureStructure.getFeatureValue(feature)).getCoveredText();
                if (coveredText != null) pathValues.add(coveredText);
            } else if (featureType.isArray()) {
                Type componentType = featureType.getComponentType();
                if (componentType.isPrimitive()) {
                    Object[] values = getPrimitiveArrayFeatureValue(view, featureStructure, feature);
                    if (allValues) pathValues.addAll(Arrays.asList(values)); else if (values.length > 0) pathValues.add(values[0]);
                } else if (typeSystem.subsumes(typeSystem.getType("uima.tcas.Annotation"), componentType)) {
                    ArrayFS fsArray = (ArrayFS) featureStructure.getFeatureValue(feature);
                    FeatureStructure[] array = fsArray.toArray();
                    if (allValues) {
                        for (FeatureStructure ftr : array) pathValues.add(((Annotation) ftr).getCoveredText());
                    } else {
                        if (array.length > 0) pathValues.add(((Annotation) array[0]).getCoveredText());
                    }
                }
            }
        } else {
            String[] remainingPathMembers = new String[pathMembers.length - 1];
            System.arraycopy(pathMembers, 1, remainingPathMembers, 0, pathMembers.length - 1);
            Feature feature = featureStructure.getType().getFeatureByBaseName(pathMembers[0]);
            FeatureStructure featureValue = featureStructure.getFeatureValue(feature);
            if (featureValue == null) return;
            if (featureValue instanceof FSArray) {
                FSArray fsArray = (FSArray) featureValue;
                if (allPaths) {
                    for (int i = 0; i < fsArray.size(); i++) {
                        FeatureStructure fs = fsArray.get(i);
                        _extract(view, fs, remainingPathMembers, pathValues);
                    }
                } else {
                    if (fsArray.size() > 0) _extract(view, fsArray.get(0), remainingPathMembers, pathValues);
                }
            } else {
                _extract(view, featureValue, remainingPathMembers, pathValues);
            }
        }
    }
