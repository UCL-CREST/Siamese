    public static synchronized void shuffle(char[] anArray) {
        int n = anArray.length;
        for (int i = n - 1; i >= 1; i--) {
            int j = randomSource.nextInt(i + 1);
            char temp = anArray[j];
            anArray[j] = anArray[i];
            anArray[i] = temp;
        }
    }
