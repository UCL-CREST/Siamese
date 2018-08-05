    public static synchronized <T> void shuffle(T[] anArray) {
        int n = anArray.length;
        for (int i = n - 1; i >= 1; i--) {
            int j = randomSource.nextInt(i + 1);
            T temp = anArray[j];
            anArray[j] = anArray[i];
            anArray[i] = temp;
        }
    }
