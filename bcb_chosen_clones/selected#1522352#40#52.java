    public static final Positioner generateInterface(Class<? extends Positioner> positionerClass, int totalNumberOfMotes, double startX, double endX, double startY, double endY, double startZ, double endZ) {
        try {
            Constructor<? extends Positioner> constr = positionerClass.getConstructor(new Class[] { int.class, double.class, double.class, double.class, double.class, double.class, double.class });
            return constr.newInstance(new Object[] { totalNumberOfMotes, startX, endX, startY, endY, startZ, endZ });
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                logger.fatal("Exception when creating " + positionerClass + ": " + e.getCause());
            } else {
                logger.fatal("Exception when creating " + positionerClass + ": " + e.getMessage());
            }
            return null;
        }
    }
