    public static void main(String[] args) {
        try {
            if (args.length == 0 && (new File("/usr/java/default/bin/java")).exists() && checkGNUVM() && (System.getProperty("php.java.bridge.exec_sun_vm", "true").equals("true"))) {
                Process p = Runtime.getRuntime().exec(new String[] { "/usr/java/default/bin/java", "-Dphp.java.bridge.exec_sun_vm=false", "-classpath", System.getProperty("java.class.path"), "TestInstallation" });
                (new Thread() {

                    InputStream in;

                    public Thread init(InputStream in) {
                        this.in = in;
                        return this;
                    }

                    public void run() {
                        int c;
                        try {
                            while ((c = in.read()) != -1) System.out.write(c);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).init(p.getInputStream()).start();
                (new Thread() {

                    InputStream in;

                    public Thread init(InputStream in) {
                        this.in = in;
                        return this;
                    }

                    public void run() {
                        int c;
                        try {
                            while ((c = in.read()) != -1) System.err.write(c);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).init(p.getErrorStream()).start();
                if (p != null) System.exit(p.waitFor());
            }
        } catch (Throwable t) {
        }
        try {
            start(args);
        } catch (Throwable t) {
            t.printStackTrace();
            if (runner != null) runner.destroy();
            System.exit(1);
        }
    }
