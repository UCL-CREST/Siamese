    public void write(String resource, String destinationPath) throws IOException, CoreException {
        OutputStream output = null;
        InputStream contentStream = null;
        try {
            contentStream = new BufferedInputStream(new FileInputStream(resource));
            output = new BufferedOutputStream(new FileOutputStream(destinationPath));
            int available = contentStream.available();
            available = available <= 0 ? DEFAULT_BUFFER_SIZE : available;
            int chunkSize = Math.min(DEFAULT_BUFFER_SIZE, available);
            byte[] readBuffer = new byte[chunkSize];
            int n = contentStream.read(readBuffer);
            while (n > 0) {
                output.write(readBuffer, 0, n);
                n = contentStream.read(readBuffer);
            }
        } finally {
            if (contentStream != null) {
                try {
                    contentStream.close();
                } catch (IOException e) {
                    IDEWorkbenchPlugin.log("Error closing input stream for file: " + resource, e);
                }
            }
            if (output != null) {
                output.close();
            }
        }
    }
