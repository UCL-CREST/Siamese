    public double[][] computeTreeVariance() {
        final int tipCount = treeModel.getExternalNodeCount();
        double[][] variance = new double[tipCount][tipCount];
        for (int i = 0; i < tipCount; i++) {
            double marginalTime = getRescaledLengthToRoot(treeModel.getExternalNode(i));
            variance[i][i] = marginalTime;
            for (int j = i + 1; j < tipCount; j++) {
                NodeRef mrca = findMRCA(i, j);
                variance[i][j] = getRescaledLengthToRoot(mrca);
            }
        }
        for (int i = 0; i < tipCount; i++) {
            for (int j = i + 1; j < tipCount; j++) {
                variance[j][i] = variance[i][j];
            }
        }
        if (DEBUG) {
            System.err.println("");
            System.err.println("New tree conditional variance:\n" + new Matrix(variance));
        }
        variance = removeMissingTipsInTreeVariance(variance);
        if (DEBUG) {
            System.err.println("");
            System.err.println("New tree (trimmed) conditional variance:\n" + new Matrix(variance));
        }
        return variance;
    }
