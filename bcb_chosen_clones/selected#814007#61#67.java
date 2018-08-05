    private int NOD(int x, int y) {
        if (y == 0) {
            return x;
        } else {
            return NOD(y, x % y);
        }
    }
