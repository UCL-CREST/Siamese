    public static void shuffle(Object[] array, Random rng) {
        for (int i = array.length - 1; i >= 0; i--) {
            int index = rng.nextInt(i + 1);
            Object a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
