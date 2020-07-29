    public static void randomize(int[] arr, int start, int end) {
        for (int i = end; i > 1 + start; i--) {
            int rnd_index = start + rnd.nextInt(i - start);
            int tmp = arr[i - 1];
            arr[i - 1] = arr[rnd_index];
            arr[rnd_index] = tmp;
        }
    }
