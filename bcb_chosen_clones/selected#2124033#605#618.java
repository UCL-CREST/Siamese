    private final Object createDecorator(Object subject, Class decoratorClass, Class[] decoratorConstructorArgs) {
        Object decorator = null;
        String declaredConstructor = decoratorClass.getName() + "(" + decoratorConstructorArgs[0].getName() + ")";
        String invokedConstructor = decoratorClass.getName() + "(" + subject.getClass().getName() + ")";
        try {
            Constructor constructor = decoratorClass.getConstructor(decoratorConstructorArgs);
            Object[] constructorArgs = new Object[] { subject };
            decorator = constructor.newInstance(constructorArgs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Couldn't invoke constructor " + invokedConstructor);
        }
        return decorator;
    }
