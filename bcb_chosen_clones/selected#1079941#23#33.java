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
