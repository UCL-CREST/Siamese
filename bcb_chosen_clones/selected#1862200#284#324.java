    protected static List<TestDecoratorFactory> parseDecorators(String decorators) {
        List<TestDecoratorFactory> result = new LinkedList<TestDecoratorFactory>();
        String toParse = decorators;
        if ((decorators == null) || "".equals(decorators)) {
            return result;
        }
        if (toParse.charAt(0) == '\"') {
            toParse = toParse.substring(1, toParse.length() - 1);
        }
        if (toParse.charAt(toParse.length() - 1) == '\"') {
            toParse = toParse.substring(0, toParse.length() - 2);
        }
        for (String decoratorClassName : toParse.split(":")) {
            try {
                Class decoratorClass = Class.forName(decoratorClassName);
                final Constructor decoratorConstructor = decoratorClass.getConstructor(WrappedSuiteTestDecorator.class);
                if (!WrappedSuiteTestDecorator.class.isAssignableFrom(decoratorClass)) {
                    throw new RuntimeException("The decorator class " + decoratorClassName + " is not a sub-class of WrappedSuiteTestDecorator, which it needs to be.");
                }
                result.add(new TestDecoratorFactory() {

                    public WrappedSuiteTestDecorator decorateTest(Test test) {
                        try {
                            return (WrappedSuiteTestDecorator) decoratorConstructor.newInstance(test);
                        } catch (InstantiationException e) {
                            throw new RuntimeException("The decorator class " + decoratorConstructor.getDeclaringClass().getName() + " cannot be instantiated.", e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("The decorator class " + decoratorConstructor.getDeclaringClass().getName() + " does not have a publicly accessable constructor.", e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException("The decorator class " + decoratorConstructor.getDeclaringClass().getName() + " cannot be invoked.", e);
                        }
                    }
                });
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("The decorator class " + decoratorClassName + " could not be found.", e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("The decorator class " + decoratorClassName + " does not have a constructor that accepts a single 'WrappedSuiteTestDecorator' argument.", e);
            }
        }
        return result;
    }
