    private Process execute(String cmd) throws IOException, InterruptedException {
        messagePrint(cmd);
        Process proc = Runtime.getRuntime().exec(cmd);
        inOut = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        inErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        synchronized (errLock) {
            errLock.notify();
        }
        synchronized (outLock) {
            outLock.notify();
        }
        return proc;
    }
