    public T parse(java.lang.String lexical) {
        if (lexical == null) throw new NullPointerException("cannot parse null");
        try {
            Constructor<T> cst = getJavaClass().getConstructor(String.class);
            return cst.newInstance(mayTrim(lexical));
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }
