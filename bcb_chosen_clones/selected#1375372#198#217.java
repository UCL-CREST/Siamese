    public static int[] sort(int[] v) {
        int i;
        int l = v.length;
        int[] index = new int[l];
        for (i = 0; i < l; i++) index[i] = i;
        int tmp;
        boolean change = true;
        while (change) {
            change = false;
            for (i = 0; i < l - 1; i++) {
                if (v[index[i]] > v[index[i + 1]]) {
                    tmp = index[i];
                    index[i] = index[i + 1];
                    index[i + 1] = tmp;
                    change = true;
                }
            }
        }
        return index;
    }
