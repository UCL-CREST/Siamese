    public int calculate(int x) {
        if (x < 0) throw new IllegalArgumentException("positive numbers only");
        if (x <= 1) return x;
        return calculate(x - 1) + calculate(x - 2);
    }
