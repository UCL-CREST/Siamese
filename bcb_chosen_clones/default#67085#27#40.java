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
