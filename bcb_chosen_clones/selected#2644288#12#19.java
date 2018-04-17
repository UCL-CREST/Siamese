    void permute(int[] a, int n) {
        for (int i = 0; i < n; i++) {
            int j = random.nextInt(i + 1);
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }
