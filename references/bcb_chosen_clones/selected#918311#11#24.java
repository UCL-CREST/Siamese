    public static LoaderException wrapInto(Class wrapperExceptionClass, Component component, String message, Exception internal) {
        if (wrapperExceptionClass.isAssignableFrom(internal.getClass())) {
            return (LoaderException) internal;
        } else {
            try {
                Constructor con = wrapperExceptionClass.getConstructor(new Class[] { Component.class, String.class, Throwable.class });
                LoaderException wrapperException = (LoaderException) con.newInstance(new Object[] { component, message, internal });
                wrapperException.fillInStackTrace();
                return wrapperException;
            } catch (Exception e) {
                throw new RuntimeException("Error creating exception of type " + wrapperExceptionClass + ". Failed to invoke the public constructor (Component, String, Exception)", e);
            }
        }
    }
