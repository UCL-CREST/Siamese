    public static void goClass(final String cmd) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        String[] args = cmd.split("[ \t]");
        final String[] realargs = new String[args.length - 1];
        String prog = args[0];
        if (args.length > 1) System.arraycopy(args, 1, realargs, 0, args.length - 1);
        final Class clazz = Class.forName(args[0]);
        final Class[] argsTypes = { String[].class };
        final Method method = clazz.getMethod("main", argsTypes);
        Thread thread = new Thread(args[0]) {

            public void run() {
                System.out.println("running " + cmd);
                try {
                    Object[] obj = { realargs };
                    method.invoke(clazz, obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e);
                    System.out.println("coudn't run the " + cmd);
                }
            }
        };
        thread.start();
    }
