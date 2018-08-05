    public static void runHillClimb() throws InterruptedException {
        StreamGobbler errorGobbler, outputGobbler;
        String hillclimb = "cmd /c hillclimb.exe";
        try {
            Process p;
            p = Runtime.getRuntime().exec(hillclimb);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
