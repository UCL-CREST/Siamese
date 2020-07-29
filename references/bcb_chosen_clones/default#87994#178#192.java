    public static void runDNADist() {
        StreamGobbler errorGobbler, outputGobbler;
        String DNADist = "cmd /c rundnadist";
        try {
            Process p;
            p = Runtime.getRuntime().exec(DNADist);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
