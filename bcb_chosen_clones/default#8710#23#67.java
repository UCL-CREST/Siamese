        public String handle(Map<String, String> args) {
            String cmd = demand(args, "command");
            String exe = commands2execs.get(cmd.toLowerCase());
            if (Util.isEmpty(exe)) {
                return NO;
            }
            Process procTmp = null;
            try {
                procTmp = Runtime.getRuntime().exec(exe);
            } catch (IOException e) {
                Remote.this.handle(e);
            }
            final Process proc = procTmp;
            final BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            Thread t = new Thread(new Runnable() {

                public void run() {
                    boolean going = true;
                    String line;
                    try {
                        while ((line = in.readLine()) != null) {
                            System.out.println(in.readLine());
                        }
                    } catch (Exception e) {
                        Remote.this.handle(e);
                    }
                    try {
                        System.out.println("trying to kill process");
                        proc.destroy();
                        System.out.println("killed process");
                    } catch (Exception e) {
                        Remote.this.handle(e);
                    }
                }
            });
            t.start();
            try {
                proc.waitFor();
                proc.exitValue();
                t.join();
            } catch (InterruptedException e) {
                Remote.this.handle(e);
            }
            return OK;
        }
