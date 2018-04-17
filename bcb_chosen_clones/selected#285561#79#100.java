    public void runCommand(String command) throws IOException {
        final Process p = Runtime.getRuntime().exec(command);
        Thread streamGobbler = new Thread() {

            @Override
            public void run() {
                try {
                    InputStream is = new BufferedInputStream(p.getInputStream());
                    while (is.read() != -1) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        streamGobbler.start();
        try {
            streamGobbler.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
