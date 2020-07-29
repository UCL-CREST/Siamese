        public int getMaxPrimeNumberOddPower(int number) {
            int result = 0;
            for (int prime = 2; prime <= number; ++prime) {
                int count = 0;
                while (number % prime == 0) {
                    count ^= 1;
                    number /= prime;
                }
                if (count == 1) result = prime;
            }
            return result;
        }
