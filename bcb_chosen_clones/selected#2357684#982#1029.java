    public static void compareToDFSProbabilistically(int numAgents, double graphDensity) {
        long numInstances = 0;
        double dfsTotal = 0;
        double myTotal = 0;
        long numTimesDFSwasWorse = 0;
        double p = graphDensity;
        Random random = new Random();
        long dfsInducedWidthTotal = 0;
        long myInducedWidthTotal = 0;
        double dfsAverageDepth = 0;
        double myAverageDepth = 0;
        long myNumMessages = 0;
        long myNumMessagesDelta = 0;
        for (int i = 0; i < Math.min(Math.pow(2, numAgents), 100); i++) {
            boolean adj[][] = Graph.randomConnectedGraph(numAgents, p, random);
            Graph<GraphVertex> interactionGraph = Graph.newInstance(adj);
            Statistics stats = new Statistics();
            Tree ourFirstTree = buildHierarchy(interactionGraph, stats);
            Tree dfsFirstTree = interactionGraph.getSpanningTree();
            myNumMessages += stats.numMessages;
            dfsInducedWidthTotal += dfsFirstTree.calculateInducedWidth();
            myInducedWidthTotal += ourFirstTree.calculateInducedWidth();
            dfsAverageDepth += dfsFirstTree.getAverageDepth();
            myAverageDepth += ourFirstTree.getAverageDepth();
            boolean newAdj[][] = new boolean[numAgents + 1][numAgents + 1];
            for (int row = 0; row < numAgents; row++) for (int col = 0; col < numAgents; col++) newAdj[row][col] = adj[row][col];
            boolean addedLink = false;
            for (int j = 0; j < numAgents; j++) {
                if (j == numAgents - 1 && !addedLink) newAdj[numAgents][j] = true; else {
                    newAdj[numAgents][j] = (random.nextDouble() <= p);
                    if (newAdj[numAgents][j]) addedLink = true;
                }
                newAdj[j][numAgents] = newAdj[numAgents][j];
            }
            Graph<GraphVertex> nextGraph = Graph.newInstance(newAdj);
            numInstances++;
            stats = new Statistics();
            Tree ourNextTree = buildHierarchy(nextGraph, ourFirstTree, stats);
            Tree dfsNextTree = nextGraph.getSpanningTree();
            myNumMessagesDelta += stats.numMessages;
            int dfsDistance = dfsFirstTree.calculateEditDistance(dfsNextTree);
            int ourDistance = ourFirstTree.calculateEditDistance(ourNextTree);
            if (dfsDistance > ourDistance) numTimesDFSwasWorse++;
            myTotal += ourDistance;
            dfsTotal += dfsDistance;
        }
        System.out.println(Integer.toString(numAgents) + "\t" + p + "\t" + Double.toString((double) dfsTotal / (double) numInstances) + "\t" + Double.toString((double) myTotal / (double) numInstances) + "\t" + Double.toString((double) dfsInducedWidthTotal / (double) numInstances) + "\t" + Double.toString((double) myInducedWidthTotal / (double) numInstances) + "\t" + Double.toString((double) dfsAverageDepth / (double) numInstances) + "\t" + Double.toString((double) myAverageDepth / (double) numInstances) + "\t" + Double.toString((double) myNumMessages / (double) numInstances) + "\t" + Double.toString((double) myNumMessagesDelta / (double) numInstances));
    }
