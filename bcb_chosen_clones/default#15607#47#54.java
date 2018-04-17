    public static void main(final String args[]) throws Exception {
        ClassLoader loader = new Adapt();
        Class c = loader.loadClass(args[0]);
        Method m = c.getMethod("main", new Class[] { String[].class });
        String[] applicationArgs = new String[args.length - 1];
        System.arraycopy(args, 1, applicationArgs, 0, applicationArgs.length);
        m.invoke(null, new Object[] { applicationArgs });
    }
