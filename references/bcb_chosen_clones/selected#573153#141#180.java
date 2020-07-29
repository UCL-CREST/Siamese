    protected void setupNodeDistanceSprings() {
        if (layoutPass != 0) {
            return;
        }
        nodeDistanceSpringRestLengths = new double[nodeCount][nodeCount];
        nodeDistanceSpringStrengths = new double[nodeCount][nodeCount];
        if (nodeDistanceSpringScalars[layoutPass] == 0.0) {
            return;
        }
        ArrayList<PNodeView> nodeList = new ArrayList<PNodeView>();
        Collection<PNodeView> matrixIndices = matrixIndexToNodeIndexMap.values();
        int i = 0;
        for (Iterator<PNodeView> iterator = matrixIndices.iterator(); iterator.hasNext(); ) {
            PNodeView nodeIndex = iterator.next();
            nodeList.add(i, nodeIndex);
            i++;
        }
        NodeDistances ind = new NodeDistances(nodeList, view, nodeIndexToMatrixIndexMap);
        int[][] node_distances = ind.calculate();
        if (node_distances == null) return;
        double node_distance_strength_constant = nodeDistanceStrengthConstant;
        double node_distance_rest_length_constant = nodeDistanceRestLengthConstant;
        System.out.println("node_distances: " + nodeDistanceSpringRestLengths + ":" + node_distances);
        for (int ii = 0; ii < nodeCount; ii++) {
            for (int jj = (ii + 1); jj < nodeCount; jj++) {
                if (node_distances[ii][jj] == Integer.MAX_VALUE) {
                    nodeDistanceSpringRestLengths[ii][jj] = disconnectedNodeDistanceSpringRestLength;
                } else {
                    nodeDistanceSpringRestLengths[ii][jj] = (node_distance_rest_length_constant * node_distances[ii][jj]);
                }
                nodeDistanceSpringRestLengths[jj][ii] = nodeDistanceSpringRestLengths[ii][jj];
                if (node_distances[ii][jj] == Integer.MAX_VALUE) {
                    nodeDistanceSpringStrengths[ii][jj] = disconnectedNodeDistanceSpringStrength;
                } else {
                    nodeDistanceSpringStrengths[ii][jj] = (node_distance_strength_constant / (node_distances[ii][jj] * node_distances[ii][jj]));
                }
                nodeDistanceSpringStrengths[jj][ii] = nodeDistanceSpringStrengths[ii][jj];
            }
        }
    }
