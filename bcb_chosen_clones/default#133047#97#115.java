    private int runApplication(String file, String args, Boolean wait) throws InterruptedException {
        int exitVal = 0;
        log.append("Execs: " + file + "\n");
        StreamGobbler errorGobbler, outputGobbler;
        try {
            Process p;
            p = Runtime.getRuntime().exec(file + " " + args);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            if (wait) {
                exitVal = p.waitFor();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exitVal;
    }
