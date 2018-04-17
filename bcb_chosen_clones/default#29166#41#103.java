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
            System.out.println("\t.../tmp/RL_pipe_agent_in\n\t.../tmp/RL_pipe_agent_out");
            System.out.println("\nExecutable name: " + args[0]);
            pipe_in = "/tmp/RL_pipe_agent_in";
            pipe_out = "/tmp/RL_pipe_agent_out";
            javaName = args[0];
        } else {
            System.out.println("\nError: incorrect input arguments.\n\nExample of valid invokations:\n java Java_agent_handler mineAgent /tmp/RL_pipe_agent_in /tmp/RL_pipe_agent_out\n...or...\njava Java_agent_handler mineAgent\n\nExiting...\n\n");
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
                        pipe_agent_init();
                    } else if (buf.equals("start")) {
                        pipe_agent_start();
                    } else if (buf.equals("step")) {
                        pipe_agent_step();
                    } else if (buf.equals("end")) {
                        pipe_agent_end();
                    } else if (buf.equals("cleanup")) {
                        partypes = null;
                        Method meth = agent.getMethod("agent_cleanup", partypes);
                        arglist = null;
                        meth.invoke(retobj, arglist);
                        System.exit(0);
                    }
                }
                buf = br.readLine();
            }
        } catch (Throwable e) {
            System.err.println("\nERROR: BAD COMMAN!!  OR Error in agent_cleanup method\n");
            e.printStackTrace();
        }
    }
