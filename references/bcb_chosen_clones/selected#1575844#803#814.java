    private int[] sortRows(int[] rows) {
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < rows.length - 1; j++) {
                if (rows[j] > rows[j + 1]) {
                    int temp = rows[j];
                    rows[j] = rows[j + 1];
                    rows[j + 1] = temp;
                }
            }
        }
        return rows;
    }
