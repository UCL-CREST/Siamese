    protected Object convert(String optArg) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, CommandException {
        if (fieldClass.isPrimitive()) {
            if (fieldClass.equals(int.class)) {
                return new Integer(optArg);
            } else if (fieldClass.equals(long.class)) {
                return new Long(optArg);
            } else if (fieldClass.equals(int.class)) {
                return new Integer(optArg);
            } else if (fieldClass.equals(byte.class)) {
                return new Byte(optArg);
            } else if (fieldClass.equals(short.class)) {
                return new Short(optArg);
            } else if (fieldClass.equals(float.class)) {
                return new Float(optArg);
            } else if (fieldClass.equals(double.class)) {
                return new Double(optArg);
            } else if (fieldClass.equals(boolean.class)) {
                return new Boolean(optArg);
            } else if (fieldClass.equals(char.class)) {
                return new Character(optArg.charAt(0));
            }
            throw new CommandException("Unpredicted place. Please report.");
        } else if (isClassNumber()) {
            Constructor<?> con = fieldClass.getConstructor(String.class);
            return con.newInstance(optArg);
        }
        return optArg;
    }
