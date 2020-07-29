    private void initSsGrid() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < i + 1; j++) {
                ss[i][j] = (int) (Math.random() * 9);
                ss[j][i] = ss[i][j];
            }
        }
    }
