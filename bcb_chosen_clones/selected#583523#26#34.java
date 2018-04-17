    void shuffle() {
        Random random = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Card c = cards.get(j);
            cards.set(j, cards.get(i));
            cards.set(i, c);
        }
    }
