    public static String processCmd(String cmd, String charset, boolean waitFor) throws Exception {
        Process process = Runtime.getRuntime().exec("cmd /c " + cmd);
        InputStream inputStream = process.getInputStream();
        String result = IOUtil.readString(inputStream, charset, true);
        if (waitFor) {
            process.waitFor();
        }
        return result;
    }
