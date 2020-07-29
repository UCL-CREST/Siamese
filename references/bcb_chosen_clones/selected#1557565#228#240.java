    private static boolean searchPrime(int n, int min, int max) {
        if (min > max) {
            return false;
        }
        int i = (min + max) / 2;
        if (primes[i] == n) {
            return true;
        } else if (n < primes[i]) {
            return searchPrime(n, min, i - 1);
        } else {
            return searchPrime(n, i + 1, max);
        }
    }
