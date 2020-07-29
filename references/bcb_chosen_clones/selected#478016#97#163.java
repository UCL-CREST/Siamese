    public void initResources() throws XAwareException {
        final String methodName = "initResources";
        if (!initialized) {
            String host = channelObject.getProperty(XAwareConstants.BIZDRIVER_HOST);
            if (host == null || host.trim().length() == 0) {
                throw new XAwareException("xa:host must be specified.");
            }
            String portString = channelObject.getProperty(XAwareConstants.BIZDRIVER_PORT);
            if (portString == null || portString.trim().length() == 0) {
                throw new XAwareException("xa:port must be specified.");
            }
            int port = 0;
            try {
                port = Integer.parseInt(portString);
            } catch (Exception exception) {
                throw new XAwareException("xa:port must be numeric.");
            }
            String remoteVerification = channelObject.getProperty(XAwareConstants.XAWARE_FTP_REMOTE_VERIFICATION);
            String userName = channelObject.getProperty(XAwareConstants.BIZDRIVER_USER);
            String password = channelObject.getProperty(XAwareConstants.BIZDRIVER_PWD);
            String proxyUser = channelObject.getProperty(XAwareConstants.BIZCOMPONENT_ATTR_PROXYUSER);
            String proxyPassword = channelObject.getProperty(XAwareConstants.BIZCOMPONENT_ATTR_PROXYPASSWORD);
            ftpClient = new FTPClient();
            logger.finest("Connecting to host:" + host + " Port:" + port, className, methodName);
            try {
                ftpClient.connect(host, port);
                if (remoteVerification != null && remoteVerification.trim().equals(XAwareConstants.XAWARE_YES)) {
                    ftpClient.setRemoteVerificationEnabled(true);
                } else {
                    ftpClient.setRemoteVerificationEnabled(false);
                }
                final int reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    String errorMessage = "FTP server refused connection. Error Code:" + reply;
                    logger.severe(errorMessage, className, methodName);
                    throw new XAwareException(errorMessage);
                }
                logger.finest("Logging in User:" + userName + " PWD:" + password, className, methodName);
                if (!ftpClient.login(userName, password)) {
                    ftpClient.logout();
                    String errorMessage = "Error logging into server. Please check credentials.";
                    logger.severe(errorMessage, className, methodName);
                    throw new XAwareException(errorMessage);
                }
                if (proxyUser != null && proxyUser.trim().length() > 0) {
                    logger.finest("Logging in again proxy UID:" + proxyUser + " proxy password:" + proxyPassword, className, methodName);
                    if (!ftpClient.login(proxyUser, proxyPassword)) {
                        ftpClient.logout();
                        String errorMessage = "Error logging using proxy user/pwd. Please check proxy credentials.";
                        logger.severe(errorMessage, className, methodName);
                        throw new XAwareException(errorMessage);
                    }
                }
            } catch (SocketException e) {
                String errorMessage = "SocketException: " + ExceptionMessageHelper.getExceptionMessage(e);
                logger.severe(errorMessage, className, methodName);
                throw new XAwareException(errorMessage, e);
            } catch (IOException e) {
                String errorMessage = "IOException: " + ExceptionMessageHelper.getExceptionMessage(e);
                logger.severe(errorMessage, className, methodName);
                throw new XAwareException(errorMessage, e);
            }
            logger.finest("Connected to host:" + host + " Port:" + port, className, methodName);
            initialized = true;
        }
    }
