    @SuppressWarnings("unchecked")
    @Override
    public T apply(T argument) {
        Class<?> argumentClass = argument.getClass();
        try {
            try {
                Method cloneMethod = argumentClass.getMethod("clone");
                return (T) cloneMethod.invoke(argument);
            } catch (NoSuchMethodException e) {
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof CloneNotSupportedException) {
                    throw new CopyNotSupportedException(argumentClass);
                } else throw e;
            }
            try {
                Constructor<?> ctor = argumentClass.getConstructor(argumentClass);
                return (T) ctor.newInstance(argument);
            } catch (NoSuchMethodException e) {
                throw new CopyNotSupportedException(argumentClass);
            }
        } catch (CopyException e) {
            throw e;
        } catch (Exception e) {
            throw new CopyFailureException(argumentClass, e);
        }
    }
