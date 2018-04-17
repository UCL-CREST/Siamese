    static Object[] getArguments(Object[] arguments, Class<?>[] types) {
        int index = types.length - 1;
        if (types.length == arguments.length) {
            Object argument = arguments[index];
            if (argument == null) {
                return arguments;
            }
            Class<?> type = types[index];
            if (type.isAssignableFrom(argument.getClass())) {
                return arguments;
            }
        }
        int length = arguments.length - index;
        Class<?> type = types[index].getComponentType();
        Object array = Array.newInstance(type, length);
        System.arraycopy(arguments, index, array, 0, length);
        Object[] args = new Object[types.length];
        System.arraycopy(arguments, 0, args, 0, index);
        args[index] = array;
        return args;
    }
