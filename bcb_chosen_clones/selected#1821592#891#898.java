    public final void randomize(int[] index, Random random) {
        for (int j = index.length - 1; j > 0; j--) {
            int k = random.nextInt(j + 1);
            int temp = index[j];
            index[j] = index[k];
            index[k] = temp;
        }
    }
