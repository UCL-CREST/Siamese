    private int runApplication(String file, String args, Boolean wait) throws InterruptedException {
        int exitVal = -1;
        StreamGobbler errorGobbler, outputGobbler;
        try {
            Process p = Runtime.getRuntime().exec(file + " " + args);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            errorGobbler.start();
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            outputGobbler.start();
            if (wait) {
                exitVal = p.waitFor();
            } else {
                exitVal = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exitVal;
    }
