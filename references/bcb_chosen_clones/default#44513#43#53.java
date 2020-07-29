    private void init() {
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            p = pb.start();
            isr = new InputStreamReader(p.getInputStream());
            bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
