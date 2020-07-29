    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("USAGE: java TestExec \"cmd\"");
            System.exit(1);
        }
        try {
            String cmd = args[0];
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERR");
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
