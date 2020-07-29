    public void shuffle() {
        Random rand = new Random();
        for (int i = size() - 1; i > 0; i--) {
            int newIndex = rand.nextInt(i + 1);
            T temp = get(newIndex);
            set(newIndex, get(i));
            set(i, temp);
        }
    }
