    public static void runNJ() {
        StreamGobbler errorGobbler, outputGobbler;
        String neighbor = "cmd /c runneighbor";
        try {
            Process p;
            p = Runtime.getRuntime().exec(neighbor);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
