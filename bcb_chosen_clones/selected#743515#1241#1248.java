    public static void shuffle(int[] array, int n) {
        for (int i = 1; i < n; ++i) {
            int swap = s_random.nextInt(i + 1);
            int temp = array[swap];
            array[swap] = array[i];
            array[i] = temp;
        }
    }
