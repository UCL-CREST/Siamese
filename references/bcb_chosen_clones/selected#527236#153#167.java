    public void writeTo(OutputStream out) throws IOException {
        if (!closed) {
            throw new IOException("Stream not closed");
        }
        if (isInMemory()) {
            memoryOutputStream.writeTo(out);
        } else {
            FileInputStream fis = new FileInputStream(outputFile);
            try {
                IOUtils.copy(fis, out);
            } finally {
                IOUtils.close(fis);
            }
        }
    }
