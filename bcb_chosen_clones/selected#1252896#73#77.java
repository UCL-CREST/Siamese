    private Integer slowFib(int i) {
        if (i <= 0) return 0;
        if (i == 1) return 1;
        return slowFib(i - 1) + slowFib(i - 2);
    }
