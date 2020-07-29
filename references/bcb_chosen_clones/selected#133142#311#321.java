    private static <T extends Throwable> T getThrowable(Class<T> type, String msg) {
        Constructor<T> cons = null;
        T ex = null;
        try {
            cons = type.getConstructor(new Class[] { String.class });
            ex = cons.newInstance(new Object[] { msg });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ex;
    }
