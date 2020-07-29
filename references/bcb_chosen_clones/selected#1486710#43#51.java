    public ChoiceGenerator randomize() {
        for (int i = values.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            UIAction tmp = values.get(i);
            values.set(i, values.get(j));
            values.set(j, tmp);
        }
        return this;
    }
