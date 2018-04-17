    public static Graph wireKOut(Graph g, int k, Random r) {
        final int n = g.size();
        if (n < 2) {
            return g;
        }
        if (n <= k) {
            k = n - 1;
        }
        int[] nodes = new int[n];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = i;
        }
        for (int i = 0; i < n; ++i) {
            int j = 0;
            while (j < k) {
                int newedge = j + r.nextInt(n - j);
                int tmp = nodes[j];
                nodes[j] = nodes[newedge];
                nodes[newedge] = tmp;
                if (nodes[j] != i) {
                    g.setEdge(i, nodes[j]);
                    j++;
                }
            }
        }
        return g;
    }
