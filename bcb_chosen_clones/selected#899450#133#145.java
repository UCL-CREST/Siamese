    @SuppressWarnings("unchecked")
    private Sbe getSbeInstance(String argument, String condType) {
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(condType);
            Constructor cons;
            cons = clazz.getConstructor(new Class[] { String.class });
            logger.debug("creating sbe of type: " + condType);
            logger.debug("sbe argument: " + argument);
            return (Sbe) cons.newInstance(new Object[] { argument });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
