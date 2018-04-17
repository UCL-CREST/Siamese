    public static void bubbleSort(String[] a) {
        Collator myCollator = Collator.getInstance();
        boolean switched = true;
        for (int pass = 0; pass < a.length - 1 && switched; pass++) {
            switched = false;
            for (int i = 0; i < a.length - pass - 1; i++) {
                if (myCollator.compare(a[i], a[i + 1]) > 0) {
                    switched = true;
                    String temp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = temp;
                }
            }
        }
    }
