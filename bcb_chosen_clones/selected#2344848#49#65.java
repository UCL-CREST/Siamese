    public StudentDistanceMatrix(StudentDistance[][] matrix) {
        int size = matrix.length;
        distanceMatrix = new StudentDistance[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (matrix[i][j] == null) throw new IllegalArgumentException("Null element in matrix (" + i + "," + j + ")"); else {
                    distanceMatrix[i][j] = matrix[i][j];
                    distanceMatrix[j][i] = matrix[i][j];
                }
            }
        }
        nameList = new ArrayList<String>();
        nameList.add(matrix[0][1].getNameA());
        for (int j = 0 + 1; j < size; j++) {
            nameList.add(matrix[0][j].getNameB());
        }
    }
