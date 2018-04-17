    int chopTuesday(int key, int array[]) {
        int min = 0;
        int max = array.length - 1;
        while (min <= max) {
            int probe = (min + max) / 2;
            switch(new Integer(key).compareTo(new Integer(array[probe]))) {
                case (0):
                    return probe;
                case (1):
                    min = probe + 1;
                    break;
                case (-1):
                    max = probe - 1;
                    break;
                default:
                    throw new Error("unexpected result from compareTo");
            }
        }
        return -1;
    }
