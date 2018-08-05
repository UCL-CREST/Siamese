    public static void openTree(String treeFile) {
        StreamGobbler errorGobbler, outputGobbler;
        String NJPlotCMD = "cmd /c njplot \"" + treeFile + "\"";
        try {
            Process p;
            p = Runtime.getRuntime().exec(NJPlotCMD);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
