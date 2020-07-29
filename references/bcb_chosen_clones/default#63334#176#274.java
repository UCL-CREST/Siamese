    private static void pipe_env_step() throws IOException {
        String buf;
        int act, count = 0;
        Class partypes[];
        Method meth;
        Object arglist[];
        Object retobj2 = null;
        String buf2;
        double r;
        boolean term;
        try {
            buf = br.readLine();
            while (buf == null) buf = br.readLine();
            st = new StringTokenizer(buf);
            while (st.hasMoreTokens()) {
                token[count] = st.nextToken().trim();
                if (!token[count].equals("\n")) count++;
            }
            if (first_step) {
                if (count == 1) {
                    action_case = 1;
                    for (int i = 0; i < token[0].length(); i++) if (token[0].charAt(i) == '.') action_case = 2;
                } else {
                    action_case = 3;
                    for (int i = 0; i < count; i++) for (int j = 0; j < token[i].length(); j++) if (token[i].charAt(j) == '.') action_case = 4;
                }
            }
            first_step = false;
            if (action_case == 1) {
                ai = Integer.parseInt(token[0]);
                partypes = new Class[1];
                partypes[0] = Integer.TYPE;
                meth = agent.getMethod("env_step", partypes);
                arglist = new Object[1];
                arglist[0] = new Integer(ai);
                retobj2 = meth.invoke(retobj, arglist);
            } else if (action_case == 2) {
                af = Double.parseDouble(token[0]);
                partypes = new Class[1];
                partypes[0] = Double.TYPE;
                meth = agent.getMethod("env_step", partypes);
                arglist = new Object[1];
                arglist[0] = new Double(af);
                retobj2 = meth.invoke(retobj, arglist);
            } else if (action_case == 3) {
                aia = new int[count];
                for (int i = 0; i < count; i++) aia[i] = Integer.parseInt(token[i]);
                partypes = new Class[1];
                partypes[0] = Class.forName("[I");
                meth = agent.getMethod("env_step", partypes);
                arglist = new Object[1];
                arglist[0] = aia;
                retobj2 = meth.invoke(retobj, arglist);
            } else if (action_case == 4) {
                afa = new double[count];
                for (int i = 0; i < count; i++) afa[i] = Double.parseDouble(token[i]);
                partypes = new Class[1];
                partypes[0] = Class.forName("[D");
                meth = agent.getMethod("env_step", partypes);
                arglist = new Object[1];
                arglist[0] = afa;
                retobj2 = meth.invoke(retobj, arglist);
            }
            Vector retval = (Vector) retobj2;
            r = ((Double) (retval.get(1))).doubleValue();
            term = ((Boolean) (retval.get(2))).booleanValue();
            buf2 = "";
            if (obs_case == 1) {
                Integer retval1 = (Integer) retval.get(0);
                buf2 = String.valueOf(retval1.intValue());
            } else if (obs_case == 2) {
                Double retval2 = (Double) retval.get(0);
                buf2 = String.valueOf(retval2.doubleValue());
            } else if (obs_case == 3) {
                buf2 = "";
                for (int i = 0; i < Array.getLength(retval.get(0)); i++) buf2 = buf2 + String.valueOf(Array.getInt(retval.get(0), i)) + " ";
                buf2 = buf2.trim();
            } else if (obs_case == 4) {
                buf2 = "";
                for (int i = 0; i < Array.getLength(retval.get(0)); i++) buf2 = buf2 + String.valueOf(Array.getDouble(retval.get(0), i)) + " ";
                buf2 = buf2.trim();
            }
            file.write(buf2 + "\n");
            file.flush();
            buf2 = String.valueOf(r);
            file.write(buf2 + "\n");
            file.flush();
            if (term) {
                file.write("1" + "\n");
            } else file.write("0" + "\n");
            file.flush();
        } catch (InvocationTargetException e) {
            System.err.println("\nError: Error in your env_step method!!\n");
            e.printStackTrace();
        } catch (Throwable e) {
            System.err.println("ERROR5: Arguments are not valid. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Observation type when using pipe communication. \nExiting...\n");
            e.printStackTrace();
        }
    }
