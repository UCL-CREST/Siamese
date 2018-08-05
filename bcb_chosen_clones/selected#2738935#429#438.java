    public boolean validate(java.lang.String value) {
        if (value == null) return false;
        try {
            Constructor<T> cst = getJavaClass().getConstructor(String.class);
            cst.newInstance(mayTrim(value));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
