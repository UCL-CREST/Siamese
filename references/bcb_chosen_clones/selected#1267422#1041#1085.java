    private static <OS extends OutputStream> OS getUnzipAndDecodeOutputStream(InputStream inputStream, final OS outputStream) {
        final PipedOutputStream pipedOutputStream = new PipedOutputStream();
        final List<Throwable> ungzipThreadThrowableList = new LinkedList<Throwable>();
        Writer decoderWriter = null;
        Thread ungzipThread = null;
        try {
            final PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
            ungzipThread = new Thread(new Runnable() {

                public void run() {
                    GZIPInputStream gzipInputStream = null;
                    try {
                        gzipInputStream = new GZIPInputStream(pipedInputStream);
                        IOUtils.copy(gzipInputStream, outputStream);
                    } catch (Throwable t) {
                        ungzipThreadThrowableList.add(t);
                    } finally {
                        IOUtils.closeQuietly(gzipInputStream);
                        IOUtils.closeQuietly(pipedInputStream);
                    }
                }
            });
            decoderWriter = Base64.newDecoder(pipedOutputStream);
            ungzipThread.start();
            IOUtils.copy(inputStream, decoderWriter, DVK_MESSAGE_CHARSET);
            decoderWriter.flush();
            pipedOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("failed to unzip and decode input", e);
        } finally {
            IOUtils.closeQuietly(decoderWriter);
            IOUtils.closeQuietly(pipedOutputStream);
            if (ungzipThread != null) {
                try {
                    ungzipThread.join();
                } catch (InterruptedException ie) {
                    throw new RuntimeException("thread interrupted while for ungzip thread to finish", ie);
                }
            }
        }
        if (!ungzipThreadThrowableList.isEmpty()) {
            throw new RuntimeException("ungzip failed", ungzipThreadThrowableList.get(0));
        }
        return outputStream;
    }
