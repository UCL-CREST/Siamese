    protected void permute(int v[], Random random) {
        for (int i = v.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            if (i != j) {
                int tmp = v[i];
                v[i] = v[j];
                v[j] = tmp;
            }
        }
    }
