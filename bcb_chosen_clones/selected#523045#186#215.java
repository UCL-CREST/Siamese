    private Type createNewType(int field) throws HL7Exception {
        Type retVal = createNewTypeWithoutReflection(field - 1);
        if (retVal != null) {
            return retVal;
        }
        int number = field - 1;
        Class<? extends Type> c = this.types.get(number);
        Type newType = null;
        try {
            Object[] args = getArgs(number);
            Class<?>[] argClasses = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Message) {
                    argClasses[i] = Message.class;
                } else {
                    argClasses[i] = args[i].getClass();
                }
            }
            newType = c.getConstructor(argClasses).newInstance(args);
        } catch (IllegalAccessException iae) {
            throw new HL7Exception("Can't access class " + c.getName() + " (" + iae.getClass().getName() + "): " + iae.getMessage(), HL7Exception.APPLICATION_INTERNAL_ERROR);
        } catch (InstantiationException ie) {
            throw new HL7Exception("Can't instantiate class " + c.getName() + " (" + ie.getClass().getName() + "): " + ie.getMessage(), HL7Exception.APPLICATION_INTERNAL_ERROR);
        } catch (InvocationTargetException ite) {
            throw new HL7Exception("Can't instantiate class " + c.getName() + " (" + ite.getClass().getName() + "): " + ite.getMessage(), HL7Exception.APPLICATION_INTERNAL_ERROR);
        } catch (NoSuchMethodException nme) {
            throw new HL7Exception("Can't instantiate class " + c.getName() + " (" + nme.getClass().getName() + "): " + nme.getMessage(), HL7Exception.APPLICATION_INTERNAL_ERROR);
        }
        return newType;
    }
