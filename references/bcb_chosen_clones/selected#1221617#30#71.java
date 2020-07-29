    public void connect(RescueMap rm, int uniformity, boolean nooneway, Random rand) {
        int nodes = rm.getNodeCount();
        distances = new int[nodes][nodes];
        for (int i = 0; i < nodes; i++) for (int j = 0; j < i; j++) {
            int x = rm.getX(i) - rm.getX(j);
            int y = rm.getY(i) - rm.getY(j);
            distances[i][j] = (int) Math.sqrt(x * x + y * y);
            distances[j][i] = distances[i][j];
        }
        usedCount = new int[nodes][nodes];
        System.out.print("Simulating road use.");
        System.out.flush();
        int steps = RUNS / 20;
        int[] prevs = new int[nodes];
        int[] dists = new int[nodes];
        for (int i = 0; i < RUNS; i++) {
            int[] picked = pickNodes(rm, rand);
            runPath(prevs, dists, rm, picked[0], picked[1]);
            if (i % steps == 0) {
                System.out.print(".");
                System.out.flush();
            }
        }
        System.out.println("done.");
        ArrayList l = new ArrayList(nodes * 5);
        for (int i = 0; i < nodes; i++) for (int j = 0; j < nodes; j++) if (rm.getRoad(i, j) > 0) {
            l.add(new Integer(usedCount[i][j]));
        }
        Collections.sort(l);
        int index1 = (int) (l.size() * (1 - THREELANE / 100.0));
        int v1 = ((Integer) (l.get(index1))).intValue();
        int v2 = ((Integer) (l.get(index1 - (int) (l.size() * TWOLANE / 100.0)))).intValue();
        for (int i = 0; i < nodes; i++) for (int j = 0; j < nodes; j++) {
            if (usedCount[i][j] >= v1 || (nooneway && usedCount[j][i] >= v1)) {
                rm.setRoad(i, j, 3);
                if (nooneway) rm.setRoad(j, i, 3);
            } else if (usedCount[i][j] >= v2 || (nooneway && usedCount[j][i] >= v2)) {
                rm.setRoad(i, j, 2);
                if (nooneway) rm.setRoad(j, i, 2);
            }
        }
    }
