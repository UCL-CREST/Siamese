    public static synchronized void shuffle(byte[] anArray) {
        int n = anArray.length;
        for (int i = n - 1; i >= 1; i--) {
            int j = randomSource.nextInt(i + 1);
            byte temp = anArray[j];
            anArray[j] = anArray[i];
            anArray[i] = temp;
        }
    }
