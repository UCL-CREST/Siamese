    public static int[][] createMatrix(List<String> list, List<Relation> ers) {
        int[][] adjMatrix = new int[list.size()][list.size()];
        for (int i = 0; i < list.size(); i++) {
            adjMatrix[i][i] = -1;
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                adjMatrix[i][j] = distance(list.get(i), list.get(j), ers);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < i; j++) {
                adjMatrix[i][j] = adjMatrix[j][i];
            }
        }
        return adjMatrix;
    }
