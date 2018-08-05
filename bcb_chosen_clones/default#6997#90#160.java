    public static Vector RL_start() throws IOException {
        int count = 0;
        try {
            file.write("RLstart\n");
            file.flush();
            buf = br.readLine();
            while (buf == null) buf = br.readLine();
            st = new StringTokenizer(buf);
            while (st.hasMoreTokens()) {
                token[count] = st.nextToken().trim();
                if (!token[count].equals("\n")) count++;
            }
            if (count == 1) {
                obs_case = 1;
                for (int i = 0; i < token[0].length(); i++) if (token[0].charAt(i) == '.') obs_case = 2;
            } else {
                obs_case = 3;
                for (int i = 0; i < count; i++) for (int j = 0; j < token[i].length(); j++) if (token[i].charAt(j) == '.') obs_case = 4;
            }
            if (obs_case == 1) {
                oa.set(0, new Integer(Integer.parseInt(token[0])));
            } else if (obs_case == 2) {
                oa.set(0, new Double(Double.parseDouble(token[0])));
            } else if (obs_case == 3) {
                sia = new int[count];
                for (int i = 0; i < count; i++) sia[i] = Integer.parseInt(token[i]);
                oa.set(0, sia);
            } else {
                sfa = new double[count];
                for (int i = 0; i < count; i++) sfa[i] = Double.parseDouble(token[i]);
                oa.set(0, sfa);
            }
        } catch (Throwable e) {
            System.err.println("ERROR: Observation types not valid. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Observation type when using pipe communication. \nExiting...\n");
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
            if (count == 1) {
                act_case = 1;
                for (int i = 0; i < token[0].length(); i++) if (token[0].charAt(i) == '.') act_case = 2;
            } else {
                act_case = 3;
                for (int i = 0; i < count; i++) for (int j = 0; j < token[i].length(); j++) if (token[i].charAt(j) == '.') act_case = 4;
            }
            if (act_case == 1) {
                oa.set(1, new Integer(Integer.parseInt(token[0])));
            } else if (act_case == 2) {
                oa.set(1, new Double(Double.parseDouble(token[0])));
            } else if (act_case == 3) {
                aia = new int[count];
                for (int i = 0; i < count; i++) aia[i] = Integer.parseInt(token[i]);
                oa.set(1, aia);
            } else {
                afa = new double[count];
                for (int i = 0; i < count; i++) afa[i] = Double.parseDouble(token[i]);
                oa.set(1, afa);
            }
        } catch (Throwable e) {
            System.err.println("ERROR: Action types not valid. RL-Framework supports \nInt, \nDouble, \nInt Array, \nDouble Array \nfor Action type when using pipe communication. \nExiting...\n");
            e.printStackTrace();
        }
        return oa;
    }
