    public boolean start(ReportBridge bridge, Gedcom gedcom) {
        String cmd = bridge.getValueFromUser("Please enter path and name of the executable to run", new String[0], "executables");
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) break;
                bridge.println(line);
            }
        } catch (IOException ioe) {
            bridge.println("*** Sorry, failed with " + ioe.getMessage());
        }
        return true;
    }
