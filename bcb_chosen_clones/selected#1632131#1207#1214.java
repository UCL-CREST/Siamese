    public static void shuffle(int[] a, Random rand) {
        for (int i = a.length - 1; i >= 1; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }
