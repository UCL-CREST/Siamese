    public boolean batchFinished() throws Exception {
        Instances input = getInputFormat();
        SequenceProperty sequenceProperty = sequenceProperty();
        Aligner aligner = getAligner();
        String relationName = BioWekaUtils.makeRelationName(input.relationName(), aligner);
        int numInstances = input.numInstances();
        int numAttributes = input.numAttributes() + 1 + numInstances;
        FastVector newAttributes = new FastVector(numAttributes);
        copyAttributes(newAttributes);
        newAttributes.addElement(new Attribute(ATTR_NAME_SEQUENCE_ALIGNMENT_INDEX));
        for (int i = 0; i < numInstances; i++) {
            newAttributes.addElement(new Attribute(ATTR_NAME_SEQUENCE_ALIGNMENT_SCORE + Integer.toString(i)));
        }
        Instances newInstances = new Instances(relationName, newAttributes, numInstances);
        newInstances.setClassIndex(input.classIndex());
        setOutputFormat(newInstances);
        double[][] scores = new double[numInstances][numInstances];
        for (int i = 0; i < numInstances; i++) {
            Instance target = input.instance(i);
            double[] values = target.toDoubleArray();
            double[] newValues = new double[numAttributes];
            System.arraycopy(values, 0, newValues, 0, values.length);
            newValues[values.length] = i;
            String targetSeq = sequenceProperty.getSequence(target);
            for (int j = 0; j < numInstances; j++) {
                Instance template = input.instance(j);
                String templateSeq = sequenceProperty.getSequence(template);
                if (symmetricAlignment && j < i) {
                    scores[i][j] = scores[j][i];
                } else {
                    scores[i][j] = aligner.align(targetSeq, templateSeq);
                }
                newValues[values.length + 1 + j] = scores[i][j];
            }
            push(target, newValues);
        }
        return (numPendingOutput() > 0);
    }
