    private String invokeInternal(String[] cmd) throws Exception {
        final Process proc = Runtime.getRuntime().exec(cmd);
        final BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        final StringBuffer res = new StringBuffer();
        Thread t = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        String line = in.readLine();
                        if (line == null) break;
                        res.append(line).append("|");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    proc.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        proc.waitFor();
        proc.exitValue();
        t.join();
        return res.toString();
    }
