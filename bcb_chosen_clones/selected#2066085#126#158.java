    protected void setupNodeDistanceSprings() {
        if (layoutPass != 0) {
            return;
        }
        nodeDistanceSpringRestLengths = new double[vertexCount][vertexCount];
        nodeDistanceSpringStrengths = new double[vertexCount][vertexCount];
        if (nodeDistanceSpringScalars[layoutPass] == 0.0) {
            return;
        }
        NodeDistances ind = new NodeDistances(pGraph.getVertexList(), null, pGraph);
        int[][] node_distances = (int[][]) ind.calculate();
        if (node_distances == null) {
            return;
        }
        double node_distance_strength_constant = nodeDistanceStrengthConstant;
        double node_distance_rest_length_constant = nodeDistanceRestLengthConstant;
        for (int node_i = 0; node_i < vertexCount; node_i++) {
            for (int node_j = (node_i + 1); node_j < vertexCount; node_j++) {
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
