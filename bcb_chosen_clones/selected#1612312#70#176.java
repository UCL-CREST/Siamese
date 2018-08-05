    public void run() {
        FTPClient ftp = null;
        try {
            StarkHhDownloaderEtcProperties etcProperties = new StarkHhDownloaderEtcProperties(getUri());
            StarkHhDownloaderVarProperties varProperties = new StarkHhDownloaderVarProperties(getUri());
            ftp = new FTPClient();
            int reply;
            ftp.connect(etcProperties.getHostname());
            log("Connecting to ftp server at " + etcProperties.getHostname() + ".");
            log("Server replied with '" + ftp.getReplyString() + "'.");
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                throw UserException.newOk("FTP server refused connection.");
            }
            log("Connected to server, now logging in.");
            ftp.login(etcProperties.getUsername(), etcProperties.getPassword());
            log("Server replied with '" + ftp.getReplyString() + "'.");
            List<String> directories = etcProperties.getDirectories();
            for (int i = 0; i < directories.size(); i++) {
                log("Checking the directory '" + directories.get(i) + "'.");
                boolean found = false;
                FTPFile[] filesArray = ftp.listFiles(directories.get(i));
                List<FTPFile> files = Arrays.asList(filesArray);
                Collections.sort(files, new Comparator<FTPFile>() {

                    public int compare(FTPFile file1, FTPFile file2) {
                        if (file2.getTimestamp().getTime().equals(file1.getTimestamp().getTime())) {
                            return file2.getName().compareTo(file1.getName());
                        } else {
                            return file1.getTimestamp().getTime().compareTo(file2.getTimestamp().getTime());
                        }
                    }
                });
                for (FTPFile file : files) {
                    if (file.getType() == FTPFile.FILE_TYPE && (varProperties.getLastImportDate(i) == null ? true : (file.getTimestamp().getTime().equals(varProperties.getLastImportDate(i).getDate()) ? file.getName().compareTo(varProperties.getLastImportName(i)) < 0 : file.getTimestamp().getTime().after(varProperties.getLastImportDate(i).getDate())))) {
                        String fileName = directories.get(i) + "\\" + file.getName();
                        if (file.getSize() == 0) {
                            log("Ignoring '" + fileName + "'because it has zero length");
                        } else {
                            log("Attempting to download '" + fileName + "'.");
                            InputStream is = ftp.retrieveFileStream(fileName);
                            if (is == null) {
                                reply = ftp.getReplyCode();
                                throw UserException.newOk("Can't download the file '" + file.getName() + "', server says: " + reply + ".");
                            }
                            log("File stream obtained successfully.");
                            hhImporter = new HhDataImportProcess(getContract().getId(), new Long(0), is, fileName + ".df2", file.getSize());
                            hhImporter.run();
                            List<VFMessage> messages = hhImporter.getMessages();
                            hhImporter = null;
                            if (messages.size() > 0) {
                                for (VFMessage message : messages) {
                                    log(message.getDescription());
                                }
                                throw UserException.newInvalidParameter("Problem loading file.");
                            }
                        }
                        if (!ftp.completePendingCommand()) {
                            throw UserException.newOk("Couldn't complete ftp transaction: " + ftp.getReplyString());
                        }
                        varProperties.setLastImportDate(i, new MonadDate(file.getTimestamp().getTime()));
                        varProperties.setLastImportName(i, file.getName());
                        found = true;
                    }
                }
                if (!found) {
                    log("No new files found.");
                }
            }
        } catch (UserException e) {
            try {
                log(e.getVFMessage().getDescription());
            } catch (ProgrammerException e1) {
                throw new RuntimeException(e1);
            } catch (UserException e1) {
                throw new RuntimeException(e1);
            }
        } catch (IOException e) {
            try {
                log(e.getMessage());
            } catch (ProgrammerException e1) {
                throw new RuntimeException(e1);
            } catch (UserException e1) {
                throw new RuntimeException(e1);
            }
        } catch (Throwable e) {
            try {
                log("Exception: " + e.getClass().getName() + " Message: " + e.getMessage());
            } catch (ProgrammerException e1) {
                throw new RuntimeException(e1);
            } catch (UserException e1) {
                throw new RuntimeException(e1);
            }
            ChellowLogger.getLogger().logp(Level.SEVERE, "ContextListener", "contextInitialized", "Can't initialize context.", e);
        } finally {
            if (ftp != null && ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
                    log("Logged out.");
                } catch (IOException ioe) {
                } catch (ProgrammerException e) {
                } catch (UserException e) {
                }
            }
        }
    }
