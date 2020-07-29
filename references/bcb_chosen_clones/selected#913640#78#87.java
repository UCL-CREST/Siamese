    public void shuffle() {
        Card tempCard = new Card();
        for (int i = 0; i < NUM_CARDS; i++) {
            int j = i + r.nextInt(NUM_CARDS - i);
            tempCard = cards[j];
            cards[j] = cards[i];
            cards[i] = tempCard;
        }
        position = 0;
    }
