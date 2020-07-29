    public static void putWithUserSettings(String from, String to, String renameTo, boolean binary, IProgressMonitor monitor) {
        if (monitor != null && monitor.isCanceled()) {
            return;
        }
        FTPHolder ftpHolder = new FTPHolder(from, to, renameTo, binary);
        synchedSet.add(ftpHolder);
        int ftpqueuesize = PrefPageOne.getIntValue(CONSTANTS.PREF_FTPQUEUE);
        if (synchedSet.size() >= ftpqueuesize) {
            JobHandler.aquireFTPLock();
            try {
                ftpClient = new FTPClient();
                ftpClient.setRemoteAddr(InetAddress.getByName(PrefPageOne.getValue(CONSTANTS.PREF_HOST)));
                ftpClient.setControlPort(PrefPageOne.getIntValue(CONSTANTS.PREF_FTPPORT));
                ftpClient.connect();
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ftpClient.login((PrefPageOne.getValue(CONSTANTS.PREF_USERNAME)), FTPUtils.decrypt(PrefPageOne.getValue(CONSTANTS.PREF_PASSWORD)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (monitor != null && monitor.isCanceled()) {
                    JobHandler.releaseFTPLock();
                    ftpClient.quit();
                    return;
                }
                synchronized (synchedSet) {
                    for (Iterator iter = synchedSet.iterator(); iter.hasNext(); ) {
                        if (monitor != null && monitor.isCanceled()) {
                            JobHandler.releaseFTPLock();
                            ftpClient.quit();
                            return;
                        }
                        Thread.yield();
                        FTPHolder element = (FTPHolder) iter.next();
                        if (element.binary) {
                            ftpClient.setType(FTPTransferType.BINARY);
                        } else {
                            ftpClient.setType(FTPTransferType.ASCII);
                        }
                        ftpClient.put(element.from, element.to);
                        if (element.renameTo != null) {
                            try {
                                ftpClient.delete(element.renameTo);
                            } catch (Exception e) {
                            }
                            ftpClient.rename(element.to, element.renameTo);
                            log.info("RENAME: " + element.to + "To: " + element.renameTo);
                        }
                    }
                    synchedSet.clear();
                }
                JobHandler.releaseFTPLock();
                ftpClient.quit();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
