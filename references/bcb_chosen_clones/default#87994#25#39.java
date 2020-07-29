    public static void runFred() throws InterruptedException {
        StreamGobbler errorGobbler, outputGobbler;
        String acinaspolz = "cmd /c fredMethod.exe";
        try {
            Process p;
            p = Runtime.getRuntime().exec(acinaspolz);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
