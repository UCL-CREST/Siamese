    @Override
    public Term contract(MatchingContext context) {
        switch(args.length) {
            case 2:
                throw new RuntimeException(args[1].getSymbol());
            case 3:
                try {
                    Class<?> clazz = Class.forName(args[2].getSymbol());
                    java.lang.reflect.Constructor<?> c = clazz.getConstructor(String.class);
                    throw (RuntimeException) c.newInstance(args[1].getSymbol());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            case 1:
            default:
                throw new RuntimeException();
        }
    }
