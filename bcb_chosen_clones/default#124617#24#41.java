    public String[] action(String command, OutputStream logStream) throws java.io.IOException {
        String logString = "\tHeartBeatAction.action:acknowledged by FIBS\n";
        synchronized (logStream) {
            logStream.write(logString.getBytes(), 0, logString.length());
            logStream.flush();
        }
        try {
            Process p = (Runtime.getRuntime()).exec("fortune");
            BufferedReader in = new BufferedReader((Reader) new InputStreamReader(p.getInputStream()));
            fortuneString = "";
        } catch (IOException ioe) {
        }
        ret[0] = "back";
        if (fortuneString != null) ret[1] = fortuneString; else {
            ret[1] = "tell repbot thump";
        }
        return (ret);
    }
