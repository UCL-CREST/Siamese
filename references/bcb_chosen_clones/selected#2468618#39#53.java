    int chopMonday(int key, int array[]) {
        int min = 0;
        int max = array.length - 1;
        while (min <= max) {
            int probe = (min + max) / 2;
            if (key == array[probe]) {
                return probe;
            } else if (key > array[probe]) {
                min = probe + 1;
            } else {
                max = probe - 1;
            }
        }
        return -1;
    }
