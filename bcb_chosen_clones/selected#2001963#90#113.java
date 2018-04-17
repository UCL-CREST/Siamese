    protected Object instantiate(Class<?> type, Object[] args) {
        try {
            if (args.length == 0 || this.constructors.length == 0) return type.newInstance();
            if (this.constructors.length == 1) if (this.constructors[0].getParameterTypes().length == 0) return this.constructors[0].newInstance(new Object[0]); else return this.constructors[0].newInstance(args); else {
                Class<?>[] types = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) types[i] = args[i].getClass();
                try {
                    return type.getConstructor(types).newInstance(args);
                } catch (NoSuchMethodException e) {
                    return type.newInstance();
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(instantiationError(type, "It is probably a non-static inner class or an abstract class."), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(instantiationError(type, "Either the class itself or its constructor are not public."), e);
        } catch (IllegalArgumentException e) {
            List<String> types = new ArrayList<String>();
            for (Object arg : args) types.add(arg.getClass().getName());
            throw new RuntimeException(instantiationError(type, "Argument mismatch in calling the constructor with arguments." + "\nExpected types are (probably): " + Arrays.asList(this.constructors[0].getParameterTypes()) + "\nProvided types are (sure): " + types.toString()), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(instantiationError(type, "Its construtor has thrown an exception, probably of type: " + e.getCause() + ". See the stack trace for details"), e);
        }
    }
