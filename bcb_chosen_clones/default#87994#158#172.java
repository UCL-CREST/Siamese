    public static void runDNAPars() {
        StreamGobbler errorGobbler, outputGobbler;
        String DNAPars = "cmd /c rundnapars";
        try {
            Process p;
            p = Runtime.getRuntime().exec(DNAPars);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
