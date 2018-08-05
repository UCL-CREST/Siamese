    public static int[] cs_randperm(int n, int seed) {
        int p[], k, j, t;
        if (seed == 0) return (null);
        p = new int[n];
        for (k = 0; k < n; k++) p[k] = n - k - 1;
        if (seed == -1) return (p);
        Random r = new Random(seed);
        for (k = 0; k < n; k++) {
            j = k + r.nextInt(n - k);
            t = p[j];
            p[j] = p[k];
            p[k] = t;
        }
        return (p);
    }
