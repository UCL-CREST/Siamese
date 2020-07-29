    public static void polishOff(IProgressMonitor monitor, String from, String to, String renameTo) {
        if (monitor != null && monitor.isCanceled()) {
            return;
        }
        try {
            ftpClient = new FTPClient();
            ftpClient.setRemoteAddr(InetAddress.getByName(PrefPageOne.getValue(CONSTANTS.PREF_HOST)));
            ftpClient.setControlPort(PrefPageOne.getIntValue(CONSTANTS.PREF_FTPPORT));
            ftpClient.connect();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ftpClient.login((PrefPageOne.getValue(CONSTANTS.PREF_USERNAME)), FTPUtils.decrypt(PrefPageOne.getValue(CONSTANTS.PREF_PASSWORD)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (from != null) {
                FTPHolder ftpHolder = new FTPHolder(from, to, renameTo, false);
                synchedSet.add(ftpHolder);
            }
            JobHandler.aquireFTPLock();
            for (Iterator iter = synchedSet.iterator(); iter.hasNext(); ) {
                if (monitor != null && monitor.isCanceled()) {
                    JobHandler.releaseFTPLock();
                    ftpClient.quit();
                    return;
                }
                Thread.yield();
                FTPHolder element = (FTPHolder) iter.next();
                ftpClient.setType(FTPTransferType.ASCII);
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
            JobHandler.releaseFTPLock();
            ftpClient.quit();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchedSet.clear();
    }
