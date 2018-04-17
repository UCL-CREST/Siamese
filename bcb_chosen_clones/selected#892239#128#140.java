    public boolean[][] getAdjacency() {
        int n = vertices.size();
        GraphVertex verts[] = vertices.toArray(new GraphVertex[0]);
        boolean adj[][] = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            adj[i][i] = false;
            for (int j = i + 1; j < n; j++) {
                adj[i][j] = verts[i].getNeighbors().contains(verts[j]) || verts[j].getNeighbors().contains(verts[i]);
                adj[j][i] = adj[i][j];
            }
        }
        return adj;
    }
