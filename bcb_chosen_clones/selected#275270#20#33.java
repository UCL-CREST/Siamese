    public static int gongYue(int a, int b) {
        int m = 1;
        if (a < b) {
            m = a;
            a = b;
            b = m;
        }
        while (m != 0) {
            m = a % b;
            a = b;
            b = m;
        }
        return a;
    }
