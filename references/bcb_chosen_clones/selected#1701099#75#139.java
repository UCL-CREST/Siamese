    protected static void download(FtpSiteConnector connector, File localFile, String remotePath, final IProgressMonitor monitor) throws FtpException {
        if (!localFile.exists()) {
            FTPClient ftp = new FTPClient();
            try {
                FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
                ftp.configure(conf);
                String hostname = connector.getUrl().getHost();
                ftp.connect(hostname);
                log.info("Connected to " + hostname);
                log.info(ftp.getReplyString());
                boolean loggedIn = ftp.login(connector.getUsername(), connector.getPassword());
                if (loggedIn) {
                    log.info("downloading file: " + remotePath);
                    ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    ftp.enterLocalPassiveMode();
                    final long fileSize = getFileSize(ftp, remotePath);
                    FileOutputStream dfile = new FileOutputStream(localFile);
                    ftp.retrieveFile(remotePath, dfile, new CopyStreamListener() {

                        public int worked = 0;

                        public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                            int percent = percent(fileSize, totalBytesTransferred);
                            int delta = percent - worked;
                            if (delta > 0) {
                                if (monitor != null) {
                                    monitor.worked(delta);
                                }
                                worked = percent;
                            }
                        }

                        public void bytesTransferred(CopyStreamEvent event) {
                        }

                        private int percent(long totalBytes, long totalBytesTransferred) {
                            long percent = (totalBytesTransferred * 100) / totalBytes;
                            return Long.valueOf(percent).intValue();
                        }
                    });
                    dfile.flush();
                    dfile.close();
                    ftp.logout();
                } else {
                    throw new FtpException("Invalid login");
                }
                ftp.disconnect();
            } catch (SocketException e) {
                log.error("File download failed with message: " + e.getMessage());
                throw new FtpException("File download failed with message: " + e.getMessage());
            } catch (IOException e) {
                log.error("File download failed with message: " + e.getMessage());
                throw new FtpException("File download failed with message: " + e.getMessage());
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException ioe) {
                        throw new FtpException("File download failed with message: " + ioe.getMessage());
                    }
                }
            }
        }
    }
