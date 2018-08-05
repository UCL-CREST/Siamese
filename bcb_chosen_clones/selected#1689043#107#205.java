    public CSolver(int maxR, int minR, int maxS, int minS, boolean u, ClusterManager cm, SolutionFactory f) {
        this.model = new CPModel();
        this.solver = null;
        this.nFound = 0;
        this.maxRosters = maxR;
        this.minRosters = minR;
        this.maxSize = maxS;
        this.minSize = minS;
        this.useAll = u;
        this.mgr = cm;
        this.nClusters = cm.getClusterCount();
        this.capacity = cm.getCapacities();
        this.singletons = cm.getSingletons();
        this.factory = f;
        this.result = Result.UNSOLVED;
        if (useAll) {
            int all = 0;
            for (int c : capacity) {
                all += c;
            }
            int m = (int) Math.round(Math.ceil(((double) all) / maxSize));
            minRosters = Math.max(minRosters, m);
        }
        assign = new IntegerVariable[nClusters][maxRosters + 1];
        transpose = new IntegerVariable[maxRosters + 1][nClusters];
        for (int c = 0; c < nClusters; c++) {
            int x = capacity[c];
            for (int r = 1; r <= maxRosters; r++) {
                assign[c][r] = Choco.makeIntVar("assign_" + c + "_" + r, 0, x);
                model.addVariable(assign[c][r]);
                transpose[r][c] = assign[c][r];
            }
            assign[c][0] = Choco.makeIntVar("unassigned_" + c, 0, (useAll) ? 0 : x);
            model.addVariable(assign[c][0]);
            transpose[0][c] = assign[c][0];
        }
        size = new IntegerVariable[maxRosters + 1];
        used = new IntegerVariable[maxRosters + 1];
        int[] temp = new int[maxSize - minSize + 2];
        temp[0] = 0;
        for (int i = 0; i <= maxSize - minSize; i++) {
            temp[i + 1] = minSize + i;
        }
        for (int r = 1; r <= minRosters; r++) {
            size[r] = Choco.makeIntVar("size_" + r, minSize, maxSize);
            used[r] = Choco.makeIntVar("used_" + r, 1, 1);
        }
        for (int r = minRosters + 1; r <= maxRosters; r++) {
            size[r] = Choco.makeIntVar("size_" + r, temp);
            used[r] = Choco.makeIntVar("used_" + r, 0, 1);
        }
        int h = (useAll) ? 1 : 0;
        used[0] = Choco.makeIntVar("used_0", 0, 0);
        assignTo = new IntegerVariable[nClusters];
        for (int i : singletons) {
            assignTo[i] = Choco.makeIntVar("assign_" + i + "_to", h, maxRosters);
        }
        nUsed = Choco.makeIntVar("nRosters", minRosters, maxRosters);
        model.addVariable(nUsed);
        for (int c = 0; c < nClusters; c++) {
            model.addConstraint(Choco.eq(Choco.sum(assign[c]), capacity[c]));
        }
        for (int r = 1; r <= maxRosters; r++) {
            model.addConstraint(Choco.eq(Choco.sum(transpose[r]), size[r]));
        }
        for (int r = 1; r <= maxRosters; r++) {
            model.addConstraint(Choco.reifiedIntConstraint(used[r], Choco.gt(size[r], 0)));
        }
        model.addConstraint(Choco.eq(Choco.sum(used), nUsed));
        for (int i : singletons) {
            model.addConstraint(Choco.domainConstraint(assignTo[i], assign[i]));
        }
        Set<HashSet<Integer>> cliques = mgr.getCliques();
        for (Set<Integer> c : cliques) {
            if (c.size() < 2) {
                continue;
            }
            Integer[] clist = c.toArray(new Integer[1]);
            for (int i = 1; i < clist.length; i++) {
                model.addConstraint(Choco.eq(assignTo[clist[0]], assignTo[clist[i]]));
            }
        }
        Map<Integer, ArrayList<Integer>> incompatible = mgr.getIncompatible();
        if (incompatible != null) {
            for (int i0 : incompatible.keySet()) {
                for (int i1 : incompatible.get(i0)) {
                    if (i0 < i1) {
                        model.addConstraint(Choco.or(Choco.eq(assignTo[i0], 0), Choco.neq(assignTo[i0], assignTo[i1])));
                    }
                }
            }
        }
        for (int r = 1; r < maxRosters; r++) {
            model.addConstraint(Choco.lexeq(transpose[r + 1], transpose[r]));
        }
        for (int r = minRosters; r < maxRosters; r++) {
            model.addConstraint(Choco.leq(used[r + 1], used[r]));
        }
    }
