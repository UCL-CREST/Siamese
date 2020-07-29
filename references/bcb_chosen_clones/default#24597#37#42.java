    public Matrix(double[][] graph) {
        heightV = graph[0].length;
        widthV = graph.length;
        data = new double[heightV][widthV];
        for (int i = 0; i < heightV; i++) for (int j = 0; j < widthV; j++) data[i][j] = graph[j][i];
    }
