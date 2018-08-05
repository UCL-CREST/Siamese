    public static synchronized void shuffle(int[] anArray) {
        int n = anArray.length;
        for (int i = n - 1; i >= 1; i--) {
            int j = randomSource.nextInt(i + 1);
            int temp = anArray[j];
            anArray[j] = anArray[i];
            anArray[i] = temp;
        }
    }
