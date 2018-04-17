    public int fib(int x) {
        int test = this.prevX;
        this.prevX = x;
        System.out.println("fib(" + x + ")");
        if (x <= 2) {
            return 1;
        }
        return fib(x - 1) + fib(x - 2);
    }
