    public int[] shuffle(int[] d) {
        int n = d.length;
        int[] res = new int[n];
        System.arraycopy(d, 0, res, 0, n);
        for (int i = 0; i < n; i++) {
            int p = i + random.nextInt(n - i);
            int q = res[p];
            res[p] = res[i];
            res[i] = q;
        }
        return (res);
    }
