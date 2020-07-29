    protected <T extends RoundI> T createRound(Class<T> roundClass) {
        T round = null;
        try {
            Constructor<T> cons = roundClass.getConstructor(GameManagerI.class);
            round = cons.newInstance(this);
        } catch (Exception e) {
            log.fatal("Cannot instantiate class " + roundClass.getName(), e);
            System.exit(1);
        }
        setRound(round);
        return round;
    }
