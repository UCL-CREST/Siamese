    public List equivalenceClasses() {
        int[] weight = weightVector();
        Object[] cyclesArray = (Object[]) cycles.toArray();
        Arrays.sort(cyclesArray, new Comparator() {

            public int compare(Object o1, Object o2) {
                return (int) (((SimpleCycle) o1).weight() - ((SimpleCycle) o2).weight());
            }
        });
        Collection essentialCycles = essentialCycles();
        boolean[][] u = new boolean[cyclesArray.length][edgeList.size()];
        boolean[][] a = getCycleEdgeIncidenceMatrix(cyclesArray);
        boolean[][] ai = inverseBinaryMatrix(a, cyclesArray.length);
        for (int i = 0; i < cyclesArray.length; i++) {
            for (int j = 0; j < cyclesArray.length; j++) {
                u[i][j] = ai[j][i];
            }
        }
        UndirectedGraph h = new SimpleGraph();
        h.addAllVertices(cycles);
        ConnectivityInspector connectivityInspector = new ConnectivityInspector(h);
        int left = 0;
        for (int right = 0; right < weight.length; right++) {
            if ((right < weight.length - 1) && (weight[right + 1] == weight[right])) continue;
            for (int i = left; i <= right; i++) {
                if (essentialCycles.contains((SimpleCycle) cyclesArray[i])) continue;
                for (int j = i + 1; j <= right; j++) {
                    if (essentialCycles.contains((SimpleCycle) cyclesArray[j])) continue;
                    if (connectivityInspector.pathExists(cyclesArray[i], cyclesArray[j])) continue;
                    boolean sameClass = false;
                    AuxiliaryGraph2 auxGraph = new AuxiliaryGraph2(graph, edgeList, u[i], u[j]);
                    for (Iterator it = graph.vertexSet().iterator(); it.hasNext(); ) {
                        Object vertex = it.next();
                        boolean shouldSearchCycle = false;
                        Collection incidentEdges = graph.edgesOf(vertex);
                        Iterator edgeIterator = incidentEdges.iterator();
                        while (edgeIterator.hasNext()) {
                            Edge edge = (Edge) edgeIterator.next();
                            int index = getEdgeIndex(edge);
                            if (u[i][index] || u[j][index]) {
                                shouldSearchCycle = true;
                                break;
                            }
                        }
                        if (shouldSearchCycle) {
                            Object auxVertex00 = auxGraph.auxVertex00(vertex);
                            Object auxVertex11 = auxGraph.auxVertex11(vertex);
                            List auxPath = BFSShortestPath.findPathBetween(auxGraph, auxVertex00, auxVertex11);
                            double pathWeight = auxPath.size();
                            if (pathWeight == weight[left]) {
                                sameClass = true;
                                break;
                            }
                        }
                    }
                    if (sameClass) {
                        h.addEdge(cyclesArray[i], cyclesArray[j]);
                    }
                }
            }
            for (int i = left; i <= right; i++) {
                if (essentialCycles.contains((SimpleCycle) cyclesArray[i])) continue;
                for (int j = i + 1; j <= right; j++) {
                    if (essentialCycles.contains((SimpleCycle) cyclesArray[j])) continue;
                    if (connectivityInspector.pathExists(cyclesArray[i], cyclesArray[j])) continue;
                    boolean sameClass = false;
                    for (int k = 0; ((SimpleCycle) cyclesArray[k]).weight() < weight[left]; k++) {
                        AuxiliaryGraph2 auxGraph = new AuxiliaryGraph2(graph, edgeList, u[i], u[k]);
                        boolean shortestPathFound = false;
                        for (Iterator it = graph.vertexSet().iterator(); it.hasNext(); ) {
                            Object vertex = it.next();
                            Object auxVertex00 = auxGraph.auxVertex00(vertex);
                            Object auxVertex11 = auxGraph.auxVertex11(vertex);
                            List auxPath = BFSShortestPath.findPathBetween(auxGraph, auxVertex00, auxVertex11);
                            double pathWeight = auxPath.size();
                            if (pathWeight == weight[left]) {
                                shortestPathFound = true;
                                break;
                            }
                        }
                        if (!shortestPathFound) continue;
                        auxGraph = new AuxiliaryGraph2(graph, edgeList, u[j], u[k]);
                        for (Iterator it = graph.vertexSet().iterator(); it.hasNext(); ) {
                            Object vertex = it.next();
                            Object auxVertex00 = auxGraph.auxVertex00(vertex);
                            Object auxVertex11 = auxGraph.auxVertex11(vertex);
                            List auxPath = BFSShortestPath.findPathBetween(auxGraph, auxVertex00, auxVertex11);
                            double pathWeight = auxPath.size();
                            if (pathWeight == weight[left]) {
                                sameClass = true;
                                break;
                            }
                        }
                        if (sameClass) break;
                    }
                    if (sameClass) {
                        h.addEdge(cyclesArray[i], cyclesArray[j]);
                    }
                }
            }
            left = right + 1;
        }
        return connectivityInspector.connectedSets();
    }
