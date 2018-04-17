    public synchronized void connect() throws FTPException, IOException {
        if (eventAggregator != null) {
            eventAggregator.setConnId(ftpClient.getId());
            ftpClient.setMessageListener(eventAggregator);
            ftpClient.setProgressMonitor(eventAggregator);
            ftpClient.setProgressMonitorEx(eventAggregator);
        }
        statistics.clear();
        configureClient();
        log.debug("Configured client");
        ftpClient.connect();
        log.debug("Client connected");
        if (masterContext.isAutoLogin()) {
            log.debug("Logging in");
            ftpClient.login(masterContext.getUserName(), masterContext.getPassword());
            log.debug("Logged in");
            configureTransferType(masterContext.getContentType());
        } else {
            log.debug("Manual login enabled");
        }
    }
