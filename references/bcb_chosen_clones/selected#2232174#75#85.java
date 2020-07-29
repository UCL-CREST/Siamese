    @Migratory
    public int f(int n) throws MalformedURLException, MigrationException {
        if (n == 0) {
            PadMig.migrate(getDest());
            return 1;
        } else if (n == 1) {
            return 1;
        } else {
            return f(n - 1) + f(n - 2);
        }
    }
