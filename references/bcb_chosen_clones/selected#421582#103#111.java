    public IntChoiceFromSet randomize() {
        for (int i = values.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            String tmp = values[i];
            values[i] = values[j];
            values[j] = tmp;
        }
        return this;
    }
