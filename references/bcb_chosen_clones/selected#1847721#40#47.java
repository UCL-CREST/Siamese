    public static <E> E[][] transpose(final E[][] matrix, final E[][] transposed) {
        for (int i = matrix.length; --i >= 0; ) {
            for (int j = matrix[i].length; --j >= 0; ) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }
