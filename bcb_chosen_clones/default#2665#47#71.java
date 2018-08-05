    public void processEvent(final EventData d) throws RemoteException, Exception {
        TextualEventData se = (TextualEventData) d;
        StringBuffer result = new StringBuffer();
        String parameters = se.getData();
        StringTokenizer st = new StringTokenizer(parameters, " ", false);
        String[] cmd = new String[st.countTokens()];
        int pos = 0;
        while (st.hasMoreTokens()) cmd[pos++] = st.nextToken();
        if (cmd.length == 0) throw new RemoteException("No command given");
        String[] env = new String[] {};
        String defaultPath = se.getFlags().getProperty("PATH", _api.getDirectory());
        File workDir = new File(defaultPath);
        if (workDir.exists() == false) workDir = new File(_api.getDirectory());
        String outLine;
        Process p = Runtime.getRuntime().exec(cmd, env, workDir);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((outLine = input.readLine()) != null) {
            result.append(outLine).append("\n");
        }
        input.close();
        int index = -1;
        if ((index = parameters.indexOf(" ")) != -1) parameters = parameters.substring(0, index);
        parameters = parameters.replaceAll("/", "_");
        _results.insertResult(parameters, "Parameters were: " + se.getData(), result.toString());
    }
