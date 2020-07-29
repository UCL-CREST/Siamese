    @Override
    protected struct[] getPole(int classIndex) throws OperatorException {
        if (classifier == null) {
            return new struct[0];
        }
        int vectNumber = 0;
        double[][] vectors = classifier.getLearningInputVectors();
        double[][] inputs = new double[0][0];
        double[][] outputs = classifier.getLearningOutputVectors();
        if (vectors.length > 0) {
            inputs = new double[vectors[0].length][vectors.length];
        }
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[0].length; j++) {
                inputs[j][i] = vectors[i][j];
            }
        }
        if (inputs.length > 0) {
            vectNumber = inputs.length;
        }
        struct[] pole = new struct[vectNumber];
        for (int i = 0; i < inputs.length; i++) {
            pole[i] = new struct();
            pole[i].response = classifier.getOutputProbabilities(inputs[i])[classIndex];
            double expectedOutput = outputs[classIndex][i];
            assert expectedOutput == 0 || expectedOutput == 1;
            pole[i].expected = expectedOutput;
        }
        return pole;
    }
