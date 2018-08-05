    AbstractConnection newConnection(FBManagedConnection mc) throws ResourceException {
        Class connectionClass = GDSFactory.getConnectionClass(getGDSType());
        if (!AbstractConnection.class.isAssignableFrom(connectionClass)) throw new IllegalArgumentException("Specified connection class" + " does not extend " + AbstractConnection.class.getName() + " class");
        try {
            Constructor constructor = connectionClass.getConstructor(new Class[] { FBManagedConnection.class });
            return (AbstractConnection) constructor.newInstance(new Object[] { mc });
        } catch (NoSuchMethodException ex) {
            throw new FBResourceException("Cannot instantiate connection class " + connectionClass.getName() + ", no constructor accepting " + FBManagedConnection.class + " class as single parameter was found.");
        } catch (InvocationTargetException ex) {
            if (ex.getTargetException() instanceof RuntimeException) throw (RuntimeException) ex.getTargetException();
            if (ex.getTargetException() instanceof Error) throw (Error) ex.getTargetException();
            throw new FBResourceException((Exception) ex.getTargetException());
        } catch (IllegalAccessException ex) {
            throw new FBResourceException("Constructor for class " + connectionClass.getName() + " is not accessible.");
        } catch (InstantiationException ex) {
            throw new FBResourceException("Cannot instantiate class" + connectionClass.getName());
        }
    }
