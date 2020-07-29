    @Override
    public void shuffleInstances() {
        Random random = new Random(seed);
        for (int i = 0; i < userRecords.length; i++) {
            for (int j = userRecords[i].length - 1; j > 0; j--) {
                Rating r = userRecords[i][j];
                int k = random.nextInt(j + 1);
                userRecords[i][j] = userRecords[i][k];
                userRecords[i][k] = r;
            }
        }
    }
