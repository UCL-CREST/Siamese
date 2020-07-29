    public void shuffle(Random rand) {
        for (int i = cards.length - 1; i >= 0; i--) {
            int r = rand.nextInt(i + 1);
            Card t = cards[i];
            cards[i] = cards[r];
            cards[r] = t;
        }
        nextCard = 0;
    }
