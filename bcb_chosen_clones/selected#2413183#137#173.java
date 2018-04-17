    private int[] sortNodesToGoal(Graph g, int parent, int goal) {
        float a, b;
        int tempI, tempR;
        float tempF;
        int len = g.nodeList[parent].edges.length;
        int[] nodes = new int[len];
        float[] dists = new float[len];
        int[] ref = new int[len];
        if (len == 0) return null;
        if (len == 1) {
            ref[0] = 0;
            return ref;
        }
        for (int i = 0; i < len; i++) {
            nodes[i] = g.getOtherNode(parent, g.nodeList[parent].edges[i]);
            a = g.nodeList[nodes[i]].x - g.nodeList[goal].x;
            b = g.nodeList[nodes[i]].y - g.nodeList[goal].y;
            dists[i] = (float) Math.sqrt(a * a + b * b);
            ref[i] = i;
        }
        for (int i = len - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (dists[j] > dists[j + 1]) {
                    tempI = nodes[j];
                    nodes[j] = nodes[j + 1];
                    nodes[j + 1] = tempI;
                    tempF = dists[j];
                    dists[j] = dists[j + 1];
                    dists[j + 1] = tempF;
                    tempR = ref[j];
                    ref[j] = ref[j + 1];
                    ref[j + 1] = tempR;
                }
            }
        }
        return ref;
    }
