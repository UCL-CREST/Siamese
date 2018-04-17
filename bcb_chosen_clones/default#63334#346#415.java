    private static void pipe_env_set_state() throws IOException {
        String buf;
        int act, count = 0, action_case;
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
            if (count == 1) {
                action_case = 1;
                for (int i = 0; i < token[0].length(); i++) if (token[0].charAt(i) == '.') action_case = 2;
            } else {
                action_case = 3;
                for (int i = 0; i < count; i++) for (int j = 0; j < token[i].length(); j++) if (token[i].charAt(j) == '.') action_case = 4;
            }
            if (action_case == 1) {
                ai = Integer.parseInt(token[0]);
                partypes = new Class[1];
                partypes[0] = Integer.TYPE;
                meth = agent.getMethod("env_set_state", partypes);
                arglist = new Object[1];
                arglist[0] = new Integer(ai);
                meth.invoke(retobj, arglist);
            } else if (action_case == 2) {
                af = Double.parseDouble(token[0]);
                partypes = new Class[1];
                partypes[0] = Double.TYPE;
                meth = agent.getMethod("env_set_state", partypes);
                arglist = new Object[1];
                arglist[0] = new Double(af);
                meth.invoke(retobj, arglist);
            } else if (action_case == 3) {
                aia = new int[count];
                for (int i = 0; i < count; i++) aia[i] = Integer.parseInt(token[i]);
                partypes = new Class[1];
                partypes[0] = Class.forName("[I");
                meth = agent.getMethod("env_set_state", partypes);
                arglist = new Object[1];
                arglist[0] = aia;
                meth.invoke(retobj, arglist);
            } else if (action_case == 4) {
                afa = new double[count];
                for (int i = 0; i < count; i++) afa[i] = Double.parseDouble(token[i]);
                partypes = new Class[1];
                partypes[0] = Class.forName("[D");
                meth = agent.getMethod("env_set_state", partypes);
                arglist = new Object[1];
                arglist[0] = afa;
                meth.invoke(retobj, arglist);
            }
            file.write("IMPLEMENTED" + "\n");
            file.flush();
        } catch (InvocationTargetException e) {
            System.err.println("\nError: Error in your env_set_state method!!\n");
            e.printStackTrace();
        } catch (Throwable e) {
            file.write("NOTIMPLEMENTED" + "\n");
            file.flush();
        }
    }
