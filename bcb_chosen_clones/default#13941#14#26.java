    private static String getPid() {
        try {
            String[] args = new String[] { "/bin/sh", "-c", "echo $PPID" };
            Process p = Runtime.getRuntime().exec(args);
            InputStream p_out = p.getInputStream();
            String s = (new BufferedReader(new InputStreamReader(p_out))).readLine();
            p.destroy();
            if (s != null) return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "???";
    }
