    public static void bubbleSort(Auto[] xs) {
        boolean unsorted = true;
        while (unsorted) {
            unsorted = false;
            for (int i = 0; i < xs.length - 1; i++) {
                if (!(xs[i].getPreis() >= xs[i + 1].getPreis())) {
                    Auto dummy = xs[i];
                    xs[i] = xs[i + 1];
                    xs[i + 1] = dummy;
                    unsorted = true;
                }
            }
        }
    }
