    public static <T extends JPLIMSPeak> T newEmptyPeakInstance(Class<T> clazz) {
        try {
            Constructor<T> ct = clazz.getConstructor();
            T instance = ct.newInstance();
            return instance;
        } catch (Throwable e) {
            System.err.println("error in newEmptyPeakInstance(" + clazz + ": " + e);
            return null;
        }
    }
