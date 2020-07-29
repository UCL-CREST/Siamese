                    public final void deliver(final String from, final String recipient, final InputStream data) throws TooMuchDataException, IOException {
                        System.out.println("FROM: " + from);
                        System.out.println("TO: " + recipient);
                        final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
                        final File file = new File(tmpDir, recipient);
                        final FileWriter fw = new FileWriter(file);
                        try {
                            IOUtils.copy(data, fw);
                        } finally {
                            fw.close();
                        }
                    }
