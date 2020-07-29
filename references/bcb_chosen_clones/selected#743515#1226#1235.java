    public static void shuffle(Object[] array) {
        int n = array.length;
        Object temp;
        for (int i = 1; i < n; ++i) {
            int swap = s_random.nextInt(i + 1);
            temp = array[swap];
            array[swap] = array[i];
            array[i] = temp;
        }
    }
