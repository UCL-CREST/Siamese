    public void start(Object context) {
        String cmd = getValueFromUser("executables", "Please enter path and name of the executable to run", new String[0]);
        if (cmd == null) return;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) break;
                println(line);
            }
        } catch (IOException ioe) {
            println("*** Sorry, failed with " + ioe.getMessage());
        }
    }
