    @Override
    public Integer perform() throws Exception {
        ProcessBuilder b = new ProcessBuilder();
        b.command(command);
        b.directory(workDir);
        Process p = b.start();
        if (out != null) Streams.drain(p.getInputStream(), out);
        if (err != null) Streams.drain(p.getErrorStream(), err);
        return block ? p.waitFor() : 0;
    }
