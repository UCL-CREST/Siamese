    public static void main(final String args[]) {
        try {
            final String osName = System.getProperty("os.name");
            final String[] cmd = getCommands("enumtypes.xsd", "com.enumtypes", "src\\gen", "C:\\java\\jdk\\1.6.0_10");
            final Runtime rt = Runtime.getRuntime();
            for (int i = 0; i < cmd.length; i++) {
                System.out.println("Execing: " + cmd[i]);
            }
            final Process proc = rt.exec(cmd);
            final StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
            final StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            final int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }
