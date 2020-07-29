    public static InputStream processCmdStream(String cmd, boolean waitFor) throws Exception {
        Process process = Runtime.getRuntime().exec("cmd /c " + cmd);
        InputStream inputStream = process.getInputStream();
        if (waitFor) {
            process.waitFor();
        }
        return inputStream;
    }
