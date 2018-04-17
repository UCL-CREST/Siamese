    private static void pipe_env_start() throws IOException {
        int s, count = 0;
        String buf;
        try {
            Method meth = agent.getMethod("env_start", null);
            Object retobj2 = meth.invoke(retobj, null);
            buf = "";
            if (retobj2 instanceof Integer) {
                obs_case = 1;
                Integer retval1 = (Integer) retobj2;
                buf = String.valueOf(retval1.intValue());
            } else if (retobj2 instanceof Double) {
                obs_case = 2;
                Double retval2 = (Double) retobj2;
                buf = String.valueOf(retval2.doubleValue());
            } else if (Class.forName("[I").isInstance(retobj2)) {
                obs_case = 3;
                buf = "";
                for (int i = 0; i < Array.getLength(retobj2); i++) buf = buf + String.valueOf(Array.getInt(retobj2, i)) + " ";
                buf = buf.trim();
            } else if (Class.forName("[D").isInstance(retobj2)) {
                obs_case = 4;
                buf = "";
                for (int i = 0; i < Array.getLength(retobj2); i++) buf = buf + String.valueOf(Array.getDouble(retobj2, i)) + " ";
                buf = buf.trim();
            } else {
                System.out.println("ERROR2: Observation is not a valid type. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Observation type when using pipe communication. \nExiting...\n");
                System.exit(1);
            }
            file.write(buf + "\n");
            file.flush();
        } catch (InvocationTargetException e) {
            System.err.println("\nError: Error in your env_state method!!\n");
            e.printStackTrace();
        } catch (Throwable e) {
            System.err.println("ERROR3: Arguments are not valid. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Observation type when using pipe communication. \nExiting...\n");
            e.printStackTrace();
        }
    }
