    private static void pipe_env_get_random_seed() throws IOException {
        String buf;
        try {
            Method meth = agent.getMethod("env_get_random_seed", null);
            Object retobj2 = meth.invoke(retobj, null);
            buf = "";
            if (retobj2 instanceof Integer) {
                Integer retval1 = (Integer) retobj2;
                buf = String.valueOf(retval1.intValue());
            } else if (retobj2 instanceof Double) {
                Double retval2 = (Double) retobj2;
                buf = String.valueOf(retval2.doubleValue());
            } else if (Class.forName("[I").isInstance(retobj2)) {
                buf = "";
                for (int i = 0; i < Array.getLength(retobj2); i++) buf = buf + String.valueOf(Array.getInt(retobj2, i)) + " ";
                buf = buf.trim();
            } else if (Class.forName("[D").isInstance(retobj2)) {
                buf = "";
                for (int i = 0; i < Array.getLength(retobj2); i++) buf = buf + String.valueOf(Array.getDouble(retobj2, i)) + " ";
                buf = buf.trim();
            } else {
                System.out.println("ERROR7: Random_seed_key is not a valid type. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Observation type when using pipe communication. \nExiting...\n");
                System.exit(1);
            }
            file.write(buf + "\n");
            file.flush();
        } catch (InvocationTargetException e) {
            System.err.println("\nError: Error in your env_get_random_seed method!!\n");
            e.printStackTrace();
        } catch (Throwable e) {
            file.write("NOTIMPLEMENTED" + "\n");
            file.flush();
        }
    }
