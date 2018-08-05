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
