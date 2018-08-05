    public static void shuffle(int[] a) {
        Random random = new Random();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int swap = a[i];
            a[i] = a[j];
            a[j] = swap;
        }
    }
