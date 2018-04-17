    private int getGCD(int a, int b) {
        while (b != 0) {
            System.out.println("a=" + a + "  b=" + b);
            int temp = b;
            b = a % b;
            a = temp;
        }
        System.out.println("a=" + a + "  b=" + b);
        return a;
    }
