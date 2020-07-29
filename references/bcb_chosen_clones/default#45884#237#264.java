    public void readPrefs(int wide) {
        StringTokenizer parameters;
        String str, parm;
        try {
            BufferedReader in = new BufferedReader(new FileReader("ListComm.def"));
            while ((str = in.readLine()) != null) {
                parameters = new StringTokenizer(str);
                parm = parameters.nextToken();
                if (parm.equals("Width")) wide = Integer.parseInt(parameters.nextToken()); else if (parm.equals("Nicks")) {
                    String[] temp = new String[parameters.countTokens()];
                    for (int i = 0; parameters.hasMoreTokens(); i++) temp[i] = parameters.nextToken().replace('�', ' ');
                    nicks = temp;
                } else if (parm.equals("Machines")) {
                    String[] temp = new String[parameters.countTokens()];
                    for (int i = 0; parameters.hasMoreTokens(); i++) temp[i] = parameters.nextToken();
                    machines = temp;
                }
            }
            if (nicks != null) {
                String[] temp = new String[nicks.length];
                for (int i = 0; i < nicks.length; i++) temp[i] = nicks[i] + " " + machines[i];
                coworkers = temp;
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Error reading prefs file.");
        }
    }
