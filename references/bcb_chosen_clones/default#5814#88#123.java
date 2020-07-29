    public void start(final Callback callback) throws Exception {
        if (t != null) return;
        String[] cmd = { ams, "-u", String.valueOf(period), "-s" };
        final Process proc = Runtime.getRuntime().exec(cmd);
        final BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        t = new Thread(new Runnable() {

            public void run() {
                boolean going = true;
                while (going) {
                    try {
                        String line = in.readLine();
                        if (line.indexOf("AMS") != -1) continue;
                        StringTokenizer st = new StringTokenizer(line, " \n\t\r", false);
                        int x = Integer.parseInt(st.nextToken().trim());
                        int y = Integer.parseInt(st.nextToken().trim());
                        int z = Integer.parseInt(st.nextToken().trim());
                        callback.process(-x, y, z);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    System.out.println("trying to kill process");
                    proc.destroy();
                    System.out.println("killed process");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        proc.waitFor();
        proc.exitValue();
        t.join();
    }
