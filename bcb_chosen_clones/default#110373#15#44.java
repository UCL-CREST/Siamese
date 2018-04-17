    public static void go(final String cmd) {
        System.out.println("running a " + cmd);
        final String[] args = cmd.split("[ ]");
        if (args[0].equalsIgnoreCase("RobotServer")) {
            if (RobotServer > 0) {
                System.out.println("Sorry only once!");
                return;
            }
            RobotServer++;
        }
        Thread thread = new Thread(args[0]) {

            public void run() {
                try {
                    Class clazz = Class.forName(args[0]);
                    Class[] argsTypes = { String[].class };
                    Object[] args0 = { args };
                    Method method = clazz.getMethod("main", argsTypes);
                    method.invoke(clazz, args0);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e);
                    System.out.println("coudn't run the " + cmd);
                    runningPrograms--;
                }
            }
        };
        runningPrograms++;
        thread.start();
    }
