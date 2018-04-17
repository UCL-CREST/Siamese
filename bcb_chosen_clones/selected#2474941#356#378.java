    public static int[] BubbleSortDEC(int[] values) {
        boolean change = true;
        int aux;
        int[] indexes = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            indexes[i] = i;
        }
        while (change) {
            change = false;
            for (int i = 0; i < values.length - 1; i++) {
                if (values[i] < values[i + 1]) {
                    aux = values[i];
                    values[i] = values[i + 1];
                    values[i + 1] = aux;
                    aux = indexes[i];
                    indexes[i] = indexes[i + 1];
                    indexes[i + 1] = aux;
                    change = true;
                }
            }
        }
        return (indexes);
    }
