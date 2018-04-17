    public static Permission instantiatePermission(Class<?> targetType, String targetName, String targetActions) throws Exception {
        Class[][] argTypes = null;
        Object[][] args = null;
        if (targetActions != null) {
            argTypes = new Class[][] { TWO_ARGS, ONE_ARGS, NO_ARGS };
            args = new Object[][] { { targetName, targetActions }, { targetName }, {} };
        } else if (targetName != null) {
            argTypes = new Class[][] { ONE_ARGS, TWO_ARGS, NO_ARGS };
            args = new Object[][] { { targetName }, { targetName, targetActions }, {} };
        } else {
            argTypes = new Class[][] { NO_ARGS, ONE_ARGS, TWO_ARGS };
            args = new Object[][] { {}, { targetName }, { targetName, targetActions } };
        }
        for (int i = 0; i < argTypes.length; i++) {
            try {
                Constructor<?> ctor = targetType.getConstructor(argTypes[i]);
                return (Permission) ctor.newInstance(args[i]);
            } catch (NoSuchMethodException ignore) {
            }
        }
        throw new IllegalArgumentException(Messages.getString("security.150", targetType));
    }
