    private static List<Integer> primeFactors(int N) {
        List<Integer> result = new ArrayList<Integer>();
        while (N % 10 == 0) {
            result.add(2);
            result.add(5);
            N = N / 10;
        }
        for (int i = 2; i <= N; i++) {
            while (N % i == 0) {
                result.add(i);
                N = N / i;
            }
        }
        return result;
    }
