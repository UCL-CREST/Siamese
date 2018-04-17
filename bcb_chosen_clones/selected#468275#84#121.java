    protected AbstractFilterImpl createImplInstance() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassCastException {
        String header = "Filter " + getFullName() + ": ";
        Class implClass = null;
        try {
            implClass = Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            CustomLogger.INSTANCE.warning(header + "class " + className + " not found");
            throw cnfe;
        }
        Constructor constr = null;
        try {
            constr = implClass.getConstructor(new Class[] { String.class });
        } catch (NoSuchMethodException nsme) {
            CustomLogger.INSTANCE.log(Level.WARNING, header + "class " + implClass + " does not have the required constructor " + " - " + nsme.getMessage(), nsme);
            throw nsme;
        } catch (SecurityException se) {
            CustomLogger.INSTANCE.log(Level.WARNING, header + "class " + implClass + " does not provide access to the required constructor" + " - " + se.getMessage(), se);
            throw se;
        }
        try {
            return (AbstractFilterImpl) constr.newInstance(new Object[] { getFullName() });
        } catch (InstantiationException ie) {
            CustomLogger.INSTANCE.log(Level.WARNING, header + "instantiation exception for class " + className + " - " + ie.getMessage(), ie);
            throw ie;
        } catch (IllegalAccessException iae) {
            CustomLogger.INSTANCE.log(Level.WARNING, header + "illegal access exception for class " + className + " - " + iae.getMessage(), iae);
            throw iae;
        } catch (IllegalArgumentException iae2) {
            CustomLogger.INSTANCE.log(Level.WARNING, header + "illegal argument exception for class " + className + " - " + iae2.getMessage(), iae2);
            throw iae2;
        } catch (InvocationTargetException ite) {
            CustomLogger.INSTANCE.log(Level.WARNING, header + "invocation target exception for class " + className + " - " + ite.getMessage(), ite);
            throw ite;
        } catch (ClassCastException cce) {
            CustomLogger.INSTANCE.warning(header + "class " + className + " does not extend AbstractFilterImpl class");
            throw cce;
        }
    }
