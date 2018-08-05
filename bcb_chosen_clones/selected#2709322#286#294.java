                                        @Override
                                        protected void sendData(OutputStream out) throws IOException {
                                            Reader reader = c.getReader();
                                            try {
                                                IOUtils.copy(reader, out);
                                            } finally {
                                                reader.close();
                                            }
                                        }
