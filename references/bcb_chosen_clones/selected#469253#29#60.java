    public void exec(String args[], boolean saveOutput) {
        StreamListener errorListener = null;
        StreamListener outputListener = null;
        try {
            if (log.isDebugEnabled()) {
                StrBuilder buf = new StrBuilder();
                for (int i = 0; i < args.length; i++) {
                    buf.append(args[i]);
                    if (i < args.length - 1) {
                        buf.append(" ");
                    }
                }
                log.debug("Executing " + buf.toString());
            }
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(args);
            errorListener = new StreamListener(process.getErrorStream(), "ERROR", saveOutput);
            outputListener = new StreamListener(process.getInputStream(), "OUTPUT", saveOutput);
            errorListener.start();
            outputListener.start();
            exitCode = process.waitFor();
            errorOutput = errorListener.getOutput();
            commandOutput = outputListener.getOutput();
        } catch (Throwable t) {
            if (log.isErrorEnabled()) {
                log.error("Error executing external process!", t);
            }
        } finally {
            errorListener = null;
            outputListener = null;
        }
    }
