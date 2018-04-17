    private ScalarExpression newScalarExpression(Class cls) {
        try {
            return (ScalarExpression) cls.getConstructor(new Class[] { QueryExpression.class }).newInstance(new Object[] { qs });
        } catch (IllegalArgumentException e) {
            throw new NucleusException("Cannot create ScalarExpression for class " + cls.getName() + " due to " + e.getMessage(), e).setFatal();
        } catch (SecurityException e) {
            throw new NucleusException("Cannot create ScalarExpression for class " + cls.getName() + " due to " + e.getMessage(), e).setFatal();
        } catch (InstantiationException e) {
            throw new NucleusException("Cannot create ScalarExpression for class " + cls.getName() + " due to " + e.getMessage(), e).setFatal();
        } catch (IllegalAccessException e) {
            throw new NucleusException("Cannot create ScalarExpression for class " + cls.getName() + " due to " + e.getMessage(), e).setFatal();
        } catch (InvocationTargetException e) {
            throw new NucleusException("Cannot create ScalarExpression for class " + cls.getName() + " due to " + e.getMessage(), e).setFatal();
        } catch (NoSuchMethodException e) {
            throw new NucleusException("Cannot create ScalarExpression for class " + cls.getName() + " due to " + e.getMessage(), e).setFatal();
        }
    }
