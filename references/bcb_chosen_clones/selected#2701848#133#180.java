    protected void entryMatched(EntryMonitor monitor, Entry entry) {
        FTPClient ftpClient = new FTPClient();
        try {
            Resource resource = entry.getResource();
            if (!resource.isFile()) {
                return;
            }
            if (server.length() == 0) {
                return;
            }
            String passwordToUse = monitor.getRepository().getPageHandler().processTemplate(password, false);
            ftpClient.connect(server);
            if (user.length() > 0) {
                ftpClient.login(user, password);
            }
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                monitor.handleError("FTP server refused connection:" + server, null);
                return;
            }
            ftpClient.setFileType(FTP.IMAGE_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            if (directory.length() > 0) {
                ftpClient.changeWorkingDirectory(directory);
            }
            String filename = monitor.getRepository().getEntryManager().replaceMacros(entry, fileTemplate);
            InputStream is = new BufferedInputStream(monitor.getRepository().getStorageManager().getFileInputStream(new File(resource.getPath())));
            boolean ok = ftpClient.storeUniqueFile(filename, is);
            is.close();
            if (ok) {
                monitor.logInfo("Wrote file:" + directory + " " + filename);
            } else {
                monitor.handleError("Failed to write file:" + directory + " " + filename, null);
            }
        } catch (Exception exc) {
            monitor.handleError("Error posting to FTP:" + server, exc);
        } finally {
            try {
                ftpClient.logout();
            } catch (Exception exc) {
            }
            try {
                ftpClient.disconnect();
            } catch (Exception exc) {
            }
        }
    }
