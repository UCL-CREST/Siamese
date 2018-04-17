    public static void main(String[] args) throws IOException {
        if (args.length == 3) {
            System.out.println("\nUsing user pipe names!:");
            System.out.println("\t..." + args[1] + "\n\t..." + args[2] + "\n");
            System.out.println("\nExecutable name: " + args[0]);
            pipe_in = args[1];
            pipe_out = args[2];
            javaName = args[0];
        } else if (args.length == 1) {
            System.out.println("\nUsing default pipe names!");
            System.out.println("\t.../tmp/RL_pipe_env_in\n\t.../tmp/RL_pipe_env_out");
            System.out.println("\nExecutable name: " + args[0]);
            pipe_in = "/tmp/RL_pipe_env_in";
            pipe_out = "/tmp/RL_pipe_env_out";
            javaName = args[0];
        } else {
            System.out.println("\nError: incorrect input arguments.\n\nExample of valid invokations:\n java Java_env_handler mines /tmp/RL_pipe_env_in /tmp/RL_pipe_env_out\n...or...\njava Java_env_handler mines\n\nExiting...\n\n");
            System.exit(0);
        }
        boolean errorr = true;
        while (errorr) {
            try {
                fis = new FileInputStream(pipe_in);
                fr = new InputStreamReader(fis);
                br = new BufferedReader(fr);
                file = new FileWriter(pipe_out);
                errorr = false;
            } catch (FileNotFoundException e) {
                errorr = true;
            }
        }
        try {
            agent = Class.forName(javaName);
            Class partypes[] = null;
            Constructor ct = agent.getConstructor(partypes);
            Object arglist[] = null;
            retobj = ct.newInstance(arglist);
            String buf = br.readLine();
            while (true) {
                if (buf != null) {
                    if (buf.equals("init")) {
                        pipe_env_init();
                    } else if (buf.equals("start")) {
                        pipe_env_start();
                    } else if (buf.equals("step")) {
                        pipe_env_step();
                    } else if (buf.equals("getstate")) {
                        pipe_env_get_state();
                    } else if (buf.equals("setstate")) {
                        pipe_env_set_state();
                    } else if (buf.equals("getrandom")) {
                        pipe_env_get_random_seed();
                    } else if (buf.equals("setrandom")) {
                        pipe_env_set_random_seed();
                    } else if (buf.equals("cleanup")) {
                        partypes = null;
                        Method meth = agent.getMethod("env_cleanup", partypes);
                        arglist = null;
                        meth.invoke(retobj, arglist);
                        System.exit(0);
                    }
                }
                buf = br.readLine();
            }
        } catch (Throwable e) {
            System.err.println("\nError: BAD COMMAND!!   OR  Error in env_cleanup method\n");
            e.printStackTrace();
        }
    }
