    public static int binSearch(int data[], int find) {
        int min = 0;
        int max = data.length - 1;
        int pos = -1;
        while (min <= max && pos == -1) {
            int mitt = (min + max) / 2;
            if (find > data[mitt]) {
                min = mitt + 1;
            } else if (find < data[mitt]) {
                max = mitt - 1;
            } else {
                pos = mitt;
            }
        }
        return pos;
    }
