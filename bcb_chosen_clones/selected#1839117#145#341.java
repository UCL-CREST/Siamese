    public void run() {
        if (getCommand() == null) throw new IllegalArgumentException("Given command is null!");
        if (getSocketProvider() == null) throw new IllegalArgumentException("Given connection is not open!");
        if (getCommand() instanceof ListCommand) {
            try {
                setReply(ReplyWorker.readReply(getSocketProvider(), true));
                setStatus(ReplyWorker.FINISHED);
            } catch (IOException ioe) {
                setCaughtException(ioe);
                setStatus(ReplyWorker.ERROR_IO_EXCEPTION);
            }
            return;
        } else if (getCommand() instanceof RetrieveCommand) {
            RetrieveCommand retrieveCommand = (RetrieveCommand) getCommand();
            if (retrieveCommand.getFromFile().getTransferType().intern() == Command.TYPE_I || retrieveCommand.getFromFile().getTransferType().intern() == Command.TYPE_A) {
                try {
                    log.debug("Download file: " + retrieveCommand.getFromFile().toString());
                    FileOutputStream out = null;
                    FileChannel channel = null;
                    if (getDownloadMethod() == RetrieveCommand.FILE_BASED) {
                        out = new FileOutputStream(retrieveCommand.getToFile().getFile());
                        channel = out.getChannel();
                        if (retrieveCommand.getResumePosition() != -1) {
                            try {
                                channel.position(retrieveCommand.getResumePosition());
                            } catch (IOException ioe) {
                                setCaughtException(ioe);
                                setStatus(ReplyWorker.ERROR_IO_EXCEPTION);
                                try {
                                    channel.close();
                                } catch (IOException ioe2) {
                                }
                                return;
                            }
                        }
                    } else if (getDownloadMethod() == RetrieveCommand.BYTEBUFFER_BASED) {
                    }
                    int amount;
                    try {
                        while ((amount = getSocketProvider().read(buffer)) != -1) {
                            if (amount == 0) {
                                try {
                                    Thread.sleep(4);
                                } catch (InterruptedException e) {
                                }
                            }
                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                if (getDownloadMethod() == RetrieveCommand.STREAM_BASED) {
                                    int rem = buffer.remaining();
                                    byte[] buf = new byte[rem];
                                    buffer.get(buf, 0, rem);
                                    this.outputPipe.write(buf, 0, rem);
                                } else if (getDownloadMethod() == RetrieveCommand.BYTEBUFFER_BASED) {
                                } else {
                                    channel.write(buffer);
                                }
                            }
                            buffer.clear();
                        }
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            if (getDownloadMethod() == RetrieveCommand.STREAM_BASED) {
                                int rem = buffer.remaining();
                                byte[] buf = new byte[rem];
                                buffer.get(buf, 0, rem);
                                this.outputPipe.write(buf, 0, rem);
                            } else if (getDownloadMethod() == RetrieveCommand.BYTEBUFFER_BASED) {
                            } else {
                                channel.write(buffer);
                            }
                        }
                        buffer.clear();
                        setStatus(ReplyWorker.FINISHED);
                        if (channel != null) channel.close();
                        if (this.outputPipe != null) this.outputPipe.close();
                        getSocketProvider().close();
                    } catch (IOException ioe) {
                        setCaughtException(ioe);
                        setStatus(ReplyWorker.ERROR_IO_EXCEPTION);
                    } finally {
                        try {
                            channel.close();
                            getSocketProvider().close();
                        } catch (Exception e) {
                        }
                    }
                } catch (FileNotFoundException fnfe) {
                    setCaughtException(fnfe);
                    setStatus(ReplyWorker.ERROR_FILE_NOT_FOUND);
                }
            } else throw new IllegalArgumentException("Unknown file transfer type for download!");
            return;
        } else if (getCommand() instanceof StoreCommand) {
            StoreCommand storeCommand = (StoreCommand) getCommand();
            if (storeCommand.getToFile().getTransferType().intern() == Command.TYPE_I || storeCommand.getToFile().getTransferType().intern() == Command.TYPE_A) {
                try {
                    log.debug("Upload file: " + storeCommand.getFromFile());
                    InputStream in = storeCommand.getStream();
                    int amount;
                    int socketWrite;
                    int socketAmount = 0;
                    if (in instanceof FileInputStream) {
                        FileChannel channel = ((FileInputStream) in).getChannel();
                        if (storeCommand.getResumePosition() != -1) {
                            try {
                                channel.position(storeCommand.getResumePosition());
                            } catch (IOException ioe) {
                                setCaughtException(ioe);
                                setStatus(ReplyWorker.ERROR_IO_EXCEPTION);
                                try {
                                    channel.close();
                                } catch (IOException ioe2) {
                                }
                                return;
                            }
                        }
                        try {
                            while ((amount = channel.read(buffer)) != -1) {
                                buffer.flip();
                                socketWrite = 0;
                                while ((socketWrite = getSocketProvider().write(buffer)) != -1) {
                                    socketAmount += socketWrite;
                                    if (amount <= socketAmount) {
                                        break;
                                    }
                                    if (socketWrite == 0) {
                                        try {
                                            Thread.sleep(4);
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                                if (socketWrite == -1) {
                                    break;
                                }
                                socketAmount = 0;
                                buffer.clear();
                            }
                            setStatus(ReplyWorker.FINISHED);
                            channel.close();
                            getSocketProvider().close();
                        } catch (IOException ioe) {
                            setCaughtException(ioe);
                            setStatus(ReplyWorker.ERROR_IO_EXCEPTION);
                        } finally {
                            try {
                                channel.close();
                                getSocketProvider().close();
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        try {
                            while ((amount = in.read(buffer.array())) != -1) {
                                buffer.flip();
                                buffer.limit(amount);
                                socketWrite = 0;
                                while ((socketWrite = getSocketProvider().write(buffer)) != -1) {
                                    socketAmount = socketWrite;
                                    if (amount <= socketAmount) {
                                        break;
                                    }
                                    if (socketWrite == 0) {
                                        try {
                                            Thread.sleep(4);
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                                if (socketWrite == -1) {
                                    break;
                                }
                                socketAmount = 0;
                                buffer.clear();
                            }
                            setStatus(ReplyWorker.FINISHED);
                            in.close();
                            getSocketProvider().close();
                        } catch (IOException ioe) {
                            setCaughtException(ioe);
                            setStatus(ReplyWorker.ERROR_IO_EXCEPTION);
                        } finally {
                            try {
                                in.close();
                                getSocketProvider().close();
                            } catch (Exception e) {
                            }
                        }
                    }
                } catch (FileNotFoundException fnfe) {
                    setCaughtException(fnfe);
                    setStatus(ReplyWorker.ERROR_FILE_NOT_FOUND);
                }
            } else throw new IllegalArgumentException("Unknown file transfer type for upload!");
        } else throw new IllegalArgumentException("Given command is not supported!");
    }
