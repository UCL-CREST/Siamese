    protected FredOutVal runCI(String type) {
        StreamGobbler errorGobbler;
        StreamGobbler outputGobbler;
        try {
            Process p;
            p = Runtime.getRuntime().exec(cmd);
            errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = p.waitFor();
            if (isNew) return getOutputValue();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
