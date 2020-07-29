    protected void setupNodeDistanceSprings() {
        if (layoutPass != 0) {
            return;
        }
        nodeDistanceSpringRestLengths = new double[nodeCount][nodeCount];
        nodeDistanceSpringStrengths = new double[nodeCount][nodeCount];
        if (nodeDistanceSpringScalars[layoutPass] == 0.0) {
            return;
        }
        ArrayList nodeList = new ArrayList();
        Collection matrixIndices = matrixIndexToNodeIndexMap.values();
        int i = 0;
        for (Iterator iterator = matrixIndices.iterator(); iterator.hasNext(); ) {
            Integer nodeIndex = (Integer) iterator.next();
            nodeList.add(i, graphView.getGraphPerspective().getNode(nodeIndex.intValue()));
            i++;
        }
        NodeDistance ind = new NodeDistance(nodeList, graphView.getGraphPerspective(), nodeIndexToMatrixIndexMap);
        int[][] node_distances = (int[][]) ind.calculate();
        if (node_distances == null) {
            return;
        }
        double node_distance_strength_constant = nodeDistanceStrengthConstant;
        double node_distance_rest_length_constant = nodeDistanceRestLengthConstant;
        for (int node_i = 0; node_i < nodeCount; node_i++) {
            for (int node_j = (node_i + 1); node_j < nodeCount; node_j++) {
                if (node_distances[node_i][node_j] == Integer.MAX_VALUE) {
                    nodeDistanceSpringRestLengths[node_i][node_j] = disconnectedNodeDistanceSpringRestLength;
                } else {
                    nodeDistanceSpringRestLengths[node_i][node_j] = (node_distance_rest_length_constant * node_distances[node_i][node_j]);
                }
                nodeDistanceSpringRestLengths[node_j][node_i] = nodeDistanceSpringRestLengths[node_i][node_j];
                if (node_distances[node_i][node_j] == Integer.MAX_VALUE) {
                    nodeDistanceSpringStrengths[node_i][node_j] = disconnectedNodeDistanceSpringStrength;
                } else {
                    nodeDistanceSpringStrengths[node_i][node_j] = (node_distance_strength_constant / (node_distances[node_i][node_j] * node_distances[node_i][node_j]));
                }
                nodeDistanceSpringStrengths[node_j][node_i] = nodeDistanceSpringStrengths[node_i][node_j];
            }
        }
    }
