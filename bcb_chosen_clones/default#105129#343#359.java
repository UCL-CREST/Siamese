    private static String myRunCommand(String cmd) throws IOException {
        String result = "";
        Process proc = Runtime.getRuntime().exec(cmd);
        InputStream istr = proc.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(istr));
        String str;
        while ((str = br.readLine()) != null) {
            result += "\n" + str;
        }
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            System.err.println("Process was interrupted");
        }
        br.close();
        return result;
    }
