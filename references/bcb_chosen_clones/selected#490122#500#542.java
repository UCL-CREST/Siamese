    private Vector[] calculate(boolean useAbsolute, float diameter, int minimumClusterSize) throws AlgorithmException {
        long startTime, calculationTime;
        Vector[] clusters;
        if (stop) return null;
        if ((function == Algorithm.COVARIANCE) || (function == Algorithm.DOTPRODUCT)) {
            setMinMaxCovOrDotProd();
        }
        progress.setMessage(0, "<html>" + "<p>Distance: " + AbstractAlgorithm.getDistanceName(function) + "<p>Absolute? " + ((useAbsolute == true) ? "Yes" : "No") + "<p>Minimum cluster size: " + minimumClusterSize + "<p>Threshold diameter: " + diameter + "</html>");
        progress.setTimerLabel(1, "Running for ", " seconds.", 1000);
        progress.setMessage(2, "# of clusters formed: 0");
        progress.setMessage(3, "size of last cluster formed: 0");
        progress.setMessage(4, "# of assigned genes: 0");
        progress.setMessage(5, "# of genes not yet assigned: " + number_of_genes);
        progress.setVisible(true);
        if (stop) return null;
        this.adjustedDiameter = getAdjustedDiameter();
        this.jacked = new JackknifedMatrixBySpecifiedExp[number_of_samples];
        for (int i = 0; i < jacked.length; i++) {
            jacked[i] = new JackknifedMatrixBySpecifiedExp(expMatrix, i);
        }
        this.proximity = new float[number_of_genes][number_of_genes];
        for (int i = 0; i < number_of_genes; i++) {
            for (int j = 0; j <= i; j++) {
                proximity[i][j] = (float) getJackknifeDistance(i, j);
                proximity[j][i] = proximity[i][j];
            }
        }
        this.jacked = null;
        Vector allUniqueIDIndices = new Vector();
        Vector clusterVector = new Vector();
        allUniqueIDIndices = new Vector();
        for (int i = 0; i < number_of_genes; i++) {
            allUniqueIDIndices.add(new Integer(i));
        }
        if (stop) return null;
        startTime = System.currentTimeMillis();
        clusterVector = getAllClusters(allUniqueIDIndices);
        if (stop) return null;
        progress.dismiss();
        calculationTime = System.currentTimeMillis() - startTime;
        clusters = (Vector[]) clusterVector.toArray(new Vector[clusterVector.size()]);
        return clusters;
    }
