    protected ExternalDecoder(InputStream source, Process process) {
        super(source);
        this.process = process;
        this.processStdOut = process.getInputStream();
        this.processStdIn = process.getOutputStream();
        new Thread() {

            @Override
            public void run() {
                try {
                    IOUtils.copy(getSource(), processStdIn);
                    System.err.println("Copy done.");
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                    IOUtils.closeQuietly(ExternalDecoder.this);
                }
            }
        }.start();
    }
