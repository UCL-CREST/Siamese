    public void run(byte in[], OutputStream redirection, File dir) throws IOException, InterruptedException {
        final Process process = Runtime.getRuntime().exec(cmd, null, dir);
        final ThreadStream errorStream = new ThreadStream(process.getErrorStream(), null);
        final ThreadStream outStream = new ThreadStream(process.getInputStream(), redirection);
        errorStream.start();
        outStream.start();
        if (in != null) {
            final OutputStream os = process.getOutputStream();
            os.write(in);
            os.close();
        }
        process.waitFor();
        errorStream.join(10000L);
        outStream.join(10000L);
        this.out = outStream.sb.toString();
        this.error = errorStream.sb.toString();
        Log.info("RUN nb = " + (++cpt));
    }
