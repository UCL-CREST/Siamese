    public static <T> void shuffle(T[] a) {
        Random random = new Random();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T swap = a[i];
            a[i] = a[j];
            a[j] = swap;
        }
    }
