    private final String[] removeFirstIndexOfArguments(final String[] args) {
        if (args == null) return args;
        Object source = (Object) args;
        int length = Array.getLength(source);
        Object target = Array.newInstance(source.getClass().getComponentType(), length - 1);
        System.arraycopy(source, 0, target, 0, 0);
        if (0 < length - 1) System.arraycopy(source, 1, target, 0, length - 1);
        return (String[]) target;
    }
