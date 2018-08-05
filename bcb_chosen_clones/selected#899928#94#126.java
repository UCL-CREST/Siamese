    protected void setupNodeDistanceSprings() {
        if (layoutPass != 0) {
            return;
        }
        nodeDistanceSpringRestLengths = new double[nodeCount][nodeCount];
        nodeDistanceSpringStrengths = new double[nodeCount][nodeCount];
        if (nodeDistanceSpringScalars[layoutPass] == 0.0D) {
            return;
        }
        SpringNodeDistances ind = new SpringNodeDistances(nodeList);
        int node_distances[][] = ind.calculate();
        if (node_distances == null) {
            return;
        }
        double node_distance_strength_constant = nodeDistanceStrengthConstant;
        double node_distance_rest_length_constant = nodeDistanceRestLengthConstant;
        for (int node_i = 0; node_i < nodeCount; node_i++) {
            for (int node_j = node_i + 1; node_j < nodeCount; node_j++) {
                if (node_distances[node_i][node_j] == SpringNodeDistances.INFINITY) {
                    nodeDistanceSpringRestLengths[node_i][node_j] = disconnectedNodeDistanceSpringRestLength;
                } else {
                    nodeDistanceSpringRestLengths[node_i][node_j] = node_distance_rest_length_constant * (double) node_distances[node_i][node_j];
                }
                nodeDistanceSpringRestLengths[node_j][node_i] = nodeDistanceSpringRestLengths[node_i][node_j];
                if (node_distances[node_i][node_j] == SpringNodeDistances.INFINITY) {
                    nodeDistanceSpringStrengths[node_i][node_j] = disconnectedNodeDistanceSpringStrength;
                } else {
                    nodeDistanceSpringStrengths[node_i][node_j] = node_distance_strength_constant / (double) (node_distances[node_i][node_j] * node_distances[node_i][node_j]);
                }
                nodeDistanceSpringStrengths[node_j][node_i] = nodeDistanceSpringStrengths[node_i][node_j];
            }
        }
    }
