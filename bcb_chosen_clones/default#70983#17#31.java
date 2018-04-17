    public void start(Gedcom gedcom) {
        String cmd = getValueFromUser("executables", translate("WhichExecutable"), new String[0]);
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
            println(translate("Error") + ioe.getMessage());
        }
    }
