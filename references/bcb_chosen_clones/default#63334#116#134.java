    private static void pipe_env_init() throws IOException {
        first_step = true;
        try {
            Class partypes[] = null;
            Method meth = agent.getMethod("env_init", partypes);
            Object arglist[] = null;
            Object retobj2 = meth.invoke(retobj, arglist);
            String retval = (String) retobj2;
            String buf = String.valueOf(retval);
            file.write(buf + "\n");
            file.flush();
        } catch (InvocationTargetException e) {
            System.err.println("\nError: Error in your env_init method!!\n");
            e.printStackTrace();
        } catch (Throwable e) {
            System.err.println("ERROR1: Arguments are not valid. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Observation type when using pipe communication. \nExiting...\n");
            e.printStackTrace();
        }
    }
