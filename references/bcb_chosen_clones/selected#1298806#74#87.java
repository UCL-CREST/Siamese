    public static String getOutput(String command) throws IOException {
        StringBuilder ret = new StringBuilder();
        Process p = Runtime.getRuntime().exec(command);
        InputStream is = p.getInputStream();
        try {
            int ch;
            while ((ch = is.read()) >= 0) {
                ret.append((char) ch);
            }
        } finally {
            is.close();
        }
        return ret.toString();
    }
