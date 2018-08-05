    public void shuffle() {
        Random rng = new Random();
        int n = this.cards.size();
        while (--n > 0) {
            int k = rng.nextInt(n + 1);
            Card temp = this.cards.get(n);
            this.cards.set(n, this.cards.get(k));
            this.cards.set(k, temp);
        }
    }
