    public static final void randomShuffle(int[] v, Random r) {
        int n = v.length;
        while (--n > 0) {
            int k = r.nextInt(n + 1);
            int temp = v[n];
            v[n] = v[k];
            v[k] = temp;
        }
    }
