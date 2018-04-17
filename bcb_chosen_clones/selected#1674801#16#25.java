    @Migratory
    public int fib(int n) throws MalformedURLException, MigrationException {
        if (n == 0) {
            if (!moved) {
                PadMig.migrate(new URL("pp://localhost:7100/first"));
                moved = true;
            }
            return 1;
        } else if (n == 1) return 1; else return fib(n - 1) + fib(n - 2);
    }
