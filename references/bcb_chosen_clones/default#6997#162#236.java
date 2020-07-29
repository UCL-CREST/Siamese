    public static Vector RL_step() throws IOException {
        int count = 0;
        file.write("RLstep\n");
        file.flush();
        try {
            buf = br.readLine();
            while (buf == null) buf = br.readLine();
            roat.set(0, new Double(Double.parseDouble(buf.trim())));
        } catch (Throwable e) {
            System.err.println("ERROR: Reading reward in RL_step! \nExiting...\n");
            e.printStackTrace();
        }
        try {
            buf = br.readLine();
            while (buf == null) buf = br.readLine();
            st = new StringTokenizer(buf);
            while (st.hasMoreTokens()) {
                token[count] = st.nextToken().trim();
                if (!token[count].equals("\n")) count++;
            }
            if (obs_case == 1) {
                roat.set(1, new Integer(Integer.parseInt(token[0])));
            } else if (obs_case == 2) {
                roat.set(1, new Double(Double.parseDouble(token[0])));
            } else if (obs_case == 3) {
                sia = new int[count];
                for (int i = 0; i < count; i++) sia[i] = Integer.parseInt(token[i]);
                roat.set(1, sia);
            } else {
                sfa = new double[count];
                for (int i = 0; i < count; i++) sfa[i] = Double.parseDouble(token[i]);
                roat.set(1, sfa);
            }
        } catch (Throwable e) {
            System.err.println("ERROR: Reading Observation in RL_step!");
            System.err.println("Observation types may be invalid. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Observation type when using pipe communication. \nExiting...\n");
            e.printStackTrace();
        }
        try {
            count = 0;
            buf = br.readLine();
            while (buf == null) buf = br.readLine();
            st = new StringTokenizer(buf);
            while (st.hasMoreTokens()) {
                token[count] = st.nextToken().trim();
                if (!token[count].equals("\n")) count++;
            }
            if (act_case == 1) {
                roat.set(2, new Integer(Integer.parseInt(token[0])));
            } else if (act_case == 2) {
                roat.set(2, new Double(Double.parseDouble(token[0])));
            } else if (act_case == 3) {
                aia = new int[count];
                for (int i = 0; i < count; i++) aia[i] = Integer.parseInt(token[i]);
                roat.set(2, aia);
            } else {
                afa = new double[count];
                for (int i = 0; i < count; i++) afa[i] = Double.parseDouble(token[i]);
                roat.set(2, afa);
            }
        } catch (Throwable e) {
            System.err.println("ERROR: Reading Action in RL_step!");
            System.err.println("Action types may be invalid. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Action type when using pipe communication. \nExiting...\n");
            e.printStackTrace();
        }
        try {
            buf = br.readLine();
            while (buf == null) buf = br.readLine();
            roat.set(3, new Boolean(Boolean.getBoolean(buf.trim())));
        } catch (Throwable e) {
            System.err.println("ERROR: Reading terminal flag in RL_step! \nExiting...\n");
            e.printStackTrace();
        }
        return roat;
    }
