    public static final void shuffle(Object[] list) {
        for (int i = list.length - 1; i >= 0; i--) {
            int j = rand.nextInt(i + 1);
            if (i == j) {
                continue;
            }
            Object tmp = list[i];
            list[i] = list[j];
            list[j] = tmp;
        }
    }
