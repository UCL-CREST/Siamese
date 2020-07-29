    public static int[] permute(int N, Random rand) {
        int[] a = new int[N];
        for (int i = 0; i < N; i++) a[i] = i;
        for (int i = 0; i < N; i++) {
            int r = rand.nextInt(i + 1);
            int swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
        return a;
    }
