    public static void bubble_sort(int[] objects, int len) {
        for (int i = len; --i >= 0; ) {
            boolean flipped = false;
            for (int j = 0; j < i; j++) {
                if (objects[j + 1] < objects[j]) {
                    int tmp = objects[j];
                    objects[j] = objects[j + 1];
                    objects[j + 1] = tmp;
                    flipped = true;
                }
            }
            if (!flipped) return;
        }
    }
