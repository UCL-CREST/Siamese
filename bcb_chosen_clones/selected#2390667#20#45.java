    protected void evaluate(SnapShot snapShot) {
        Node[] nodes = snapShot.getNodeShadows();
        double[][] weightMatrix = new double[nodes.length][nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            Node node1 = nodes[i];
            weightMatrix[i][i] = 0;
            for (int i1 = i + 1; i1 < nodes.length; i1++) {
                Node node2 = nodes[i1];
                if (node1.isInRange(node2)) {
                    weightMatrix[i][i1] = node1.getLocation().getLength(node2.getLocation());
                } else {
                    weightMatrix[i][i1] = Double.MAX_VALUE;
                }
                weightMatrix[i1][i] = weightMatrix[i][i1];
            }
        }
        double[][] distanceWeights = FloydWarshal(weightMatrix);
        double max = -1;
        for (int i = 0; i < distanceWeights.length; i++) {
            for (int j = i + 1; j < distanceWeights.length; j++) {
                max = Math.max(max, distanceWeights[i][j] < Double.MAX_VALUE ? distanceWeights[i][j] : -1);
            }
        }
        numberOfData++;
        totalDistance += max;
    }
