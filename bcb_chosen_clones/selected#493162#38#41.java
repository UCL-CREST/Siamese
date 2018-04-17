    public void throwException() throws Throwable {
        Constructor c = exceptionClass.getConstructor(String.class);
        throw (Throwable) c.newInstance(message);
    }
