    private void printToStdout(InputStream in) throws IOException {
        try {
            IOUtils.copyBytes(in, System.out, getConf(), false);
        } finally {
            in.close();
        }
    }
