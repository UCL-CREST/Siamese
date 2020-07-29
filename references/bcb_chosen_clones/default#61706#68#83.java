    public static void exec() throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String[] params;
        if (System.getProperty("path.separator", ":").equals(":")) {
            params = new String[] { "sh", "-c", "set" };
        } else {
            params = new String[] { "cmd", "/C", "set" };
        }
        Process p = rt.exec(params);
        InputStream in = p.getInputStream();
        int c;
        while ((c = in.read()) != -1) {
            out.print((char) c);
        }
        in.close();
    }
