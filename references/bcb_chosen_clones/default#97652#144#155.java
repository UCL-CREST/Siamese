    public int exec() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(getCommandline());
        PrintWriter logfile = new PrintWriter(out);
        ReaderThread err = new ReaderThread("ERROR", process.getErrorStream(), logfile);
        ReaderThread out = new ReaderThread("OUT", process.getInputStream(), logfile);
        err.start();
        out.start();
        int exitVal = process.waitFor();
        err.waitFor();
        out.waitFor();
        return exitVal;
    }
