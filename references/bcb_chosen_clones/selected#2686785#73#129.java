    private void computeStatistics(final DataSplit split) {
        Attributes as = graph.getAttributes(nodeType);
        int idx = split.getView().getAttributeIndex();
        attrDim = new int[as.attributeCount()];
        Arrays.fill(attrDim, 0);
        for (int i = 0; i < as.attributeCount(); i++) {
            if (i == as.getKeyIndex() || i == idx) continue;
            Attribute a = as.getAttribute(i);
            if (a.getType() == Type.CATEGORICAL) attrDim[i] = ((AttributeCategorical) a).getTokens().length; else attrDim[i] = 1;
        }
        int dim = VectorMath.sum(attrDim);
        srcInstance = new double[dim];
        destInstance = new double[dim];
        mean = new double[dim];
        count = new double[dim];
        double[] zeroes = new double[dim];
        Arrays.fill(zeroes, 0.0D);
        Arrays.fill(mean, 0.0D);
        Arrays.fill(count, 0.0D);
        double numNodes = 0;
        for (Node n : split.getTrainSet()) {
            if (n.isMissing(idx)) return;
            numNodes++;
            fillInstance(n, srcInstance, zeroes);
            VectorMath.add(mean, srcInstance);
        }
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 0) continue;
            mean[i] /= count[i];
        }
        cm = new double[dim][dim];
        for (double[] row : cm) Arrays.fill(row, 0.0D);
        for (Node n : split.getTrainSet()) {
            if (n.isMissing(idx)) return;
            Arrays.fill(count, 0);
            fillInstance(n, srcInstance, mean);
            for (int i = 0; i < count.length; i++) {
                double x = srcInstance[i] - mean[i];
                for (int j = i; j < count.length; j++) {
                    double y = srcInstance[j] - mean[j];
                    cm[i][j] += x * y;
                }
            }
        }
        if (numNodes > 0) {
            for (int i = 0; i < count.length; i++) {
                for (int j = i; j < count.length; j++) {
                    cm[i][j] /= numNodes;
                    cm[j][i] = cm[i][j];
                }
            }
        }
        cern.colt.matrix.DoubleMatrix2D coltCM = new cern.colt.matrix.impl.DenseDoubleMatrix2D(dim, dim);
        coltCM.assign(cm);
        coltCM = alg.inverse(coltCM);
        cm = coltCM.toArray();
    }
