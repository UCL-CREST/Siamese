    public static void bubble_sort(Sortable[] objects) {
        for (int i = objects.length; --i >= 0; ) {
            boolean flipped = false;
            for (int j = 0; j < i; j++) {
                if (objects[j].greater_than(objects[j + 1])) {
                    Sortable tmp = objects[j];
                    objects[j] = objects[j + 1];
                    objects[j + 1] = tmp;
                    flipped = true;
                }
            }
            if (!flipped) return;
        }
    }
