    private String logonToServer(FTPClient ftpClient, String ftpAddress, int noRetries) {
        String remoteHomeDir = null;
        noRetriesSoFar = 0;
        while (true) {
            try {
                ftpClient.connect(ftpAddress, ftpPort);
                int reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    throw new IOException();
                }
                if (!ftpClient.login(user, password)) {
                    throw new IOException();
                }
                remoteHomeDir = ftpClient.printWorkingDirectory();
                msgEntry.setAppContext("logonToServer()");
                msgEntry.setMessageText("Logged into FTP server " + ftpAddress + ":" + ftpPort + " as user " + user);
                logger.logProcess(msgEntry);
                break;
            } catch (IOException e) {
                logoutAndDisconnect(ftpClient);
                if (noRetriesSoFar++ < noRetries) {
                    waitBetweenRetry();
                    notifyAndStartWaitingFlag = false;
                } else {
                    notifyAndStartWaitingFlag = true;
                    errEntry.setThrowable(e);
                    errEntry.setAppContext("logonToServer()");
                    errEntry.setAppMessage("Unable to login after " + (noRetriesSoFar - 1) + " retries. Max Retries.\n" + "Address:" + ftpAddress + "\n" + "User:" + user);
                    errEntry.setSubjectSendEmail("Unable to login to " + ftpAddress + " after " + (noRetriesSoFar - 1) + " retries.");
                    logger.logError(errEntry);
                    break;
                }
            }
        }
        return remoteHomeDir;
    }
