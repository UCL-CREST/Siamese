    protected static void runBinning() throws InterruptedException {
        StreamGobbler errorGobbler;
        StreamGobbler outputGobbler;
        makeRandPCRError();
        String removeGaps = "cmd /c removegaps3000.exe";
        try {
            Process a;
            a = Runtime.getRuntime().exec(removeGaps);
            errorGobbler = new StreamGobbler(a.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(a.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = a.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String readSynec = "cmd /c readsynec3000.exe";
        try {
            Process b;
            b = Runtime.getRuntime().exec(readSynec);
            errorGobbler = new StreamGobbler(b.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(b.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = b.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String correctPcr = "cmd /c correctpcr3000.exe";
        try {
            Process c;
            c = Runtime.getRuntime().exec(correctPcr);
            errorGobbler = new StreamGobbler(c.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(c.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = c.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String divergencematrix = "cmd /c divergencematrix3000.exe";
        try {
            Process d;
            d = Runtime.getRuntime().exec(divergencematrix);
            errorGobbler = new StreamGobbler(d.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(d.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = d.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String binningdanny = "cmd /c binningdanny.exe";
        try {
            Process e;
            e = Runtime.getRuntime().exec(binningdanny);
            errorGobbler = new StreamGobbler(e.getErrorStream(), "ERROR");
            outputGobbler = new StreamGobbler(e.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = e.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
