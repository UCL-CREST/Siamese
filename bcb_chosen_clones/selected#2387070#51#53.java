    private Long getGCD(Long a, Long b) {
        if (b == 0) return a; else return getGCD(b, a % b);
    }
