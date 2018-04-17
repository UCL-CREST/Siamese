    private ArrayList<String> exec(String cmd) throws IOException {
        ArrayList<String> msgLog = new ArrayList<String>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec(crashPath + " " + cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                msgLog.add(line);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return msgLog;
    }
