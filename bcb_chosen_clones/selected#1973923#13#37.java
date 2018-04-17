    public static DefaultTableModel transpose(DefaultTableModel orig) {
        Object[][] all = new Object[orig.getRowCount() + 1][orig.getColumnCount()];
        for (int i = 0; i < all.length; i++) {
            for (int k = 0; k < all[0].length; k++) {
                if (i == 0) {
                    all[i][k] = orig.getColumnName(k);
                } else {
                    all[i][k] = orig.getValueAt(i - 1, k);
                }
            }
        }
        Object[][] transpose = new Object[all[0].length][all.length];
        for (int row = 0; row < all.length; row++) {
            for (int col = 0; col < all[0].length; col++) {
                transpose[col][row] = all[row][col];
            }
        }
        Object[][] data = new Object[transpose.length - 1][transpose[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int k = 0; k < data[0].length; k++) {
                data[i][k] = transpose[i + 1][k];
            }
        }
        return new DefaultTableModel(data, transpose[0]);
    }
