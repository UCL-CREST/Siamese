    static int calcSequential(int n) {
        if (n <= 1) {
            return n;
        } else {
            return calcSequential(n - 1) + calcSequential(n - 2);
        }
    }
