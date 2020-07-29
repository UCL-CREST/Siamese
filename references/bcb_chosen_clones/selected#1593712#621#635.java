    protected <T extends RoundI, U extends RoundI> T createRound(Class<T> roundClass, U parentRound) {
        if (parentRound == null) {
            return createRound(roundClass);
        }
        T round = null;
        try {
            Constructor<T> cons = roundClass.getConstructor(GameManagerI.class, RoundI.class);
            round = cons.newInstance(this, parentRound);
        } catch (Exception e) {
            log.fatal("Cannot instantiate class " + roundClass.getName(), e);
            System.exit(1);
        }
        setRound(round);
        return round;
    }
