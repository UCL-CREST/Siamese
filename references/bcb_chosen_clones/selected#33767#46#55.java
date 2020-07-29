    public void shuffle() {
        currentDeckPosition = 0;
        int n = 52;
        while (--n > 0) {
            int k = rand.nextInt(n + 1);
            int temp = deck[n];
            deck[n] = deck[k];
            deck[k] = temp;
        }
    }
