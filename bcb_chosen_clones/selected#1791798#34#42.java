    public static synchronized void shuffle(double[] anArray) {
        int n = anArray.length;
        for (int i = n - 1; i >= 1; i--) {
            int j = randomSource.nextInt(i + 1);
            double temp = anArray[j];
            anArray[j] = anArray[i];
            anArray[i] = temp;
        }
    }
