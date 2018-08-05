    static <T extends Throwable> void throwException(String message, Class<T> exceptionClass) throws T, ValidatorError {
        if (exceptionClass == null) {
            throw new ValidatorException(message);
        }
        T result = null;
        try {
            Constructor<T> constuctor = exceptionClass.getConstructor(String.class);
            result = exceptionClass.cast(constuctor.newInstance(message));
        } catch (Throwable e) {
            throw new ValidatorError(e);
        }
        throw result;
    }
