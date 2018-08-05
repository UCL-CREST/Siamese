    private static int fib(int a_index) {
        if (a_index == 0 || a_index == 1) {
            return 1;
        }
        return fib(a_index - 1) + fib(a_index - 2);
    }
