    public synchronized void connect() throws FTPConnectionException {
        checkLocked();
        try {
            int reply;
            this.disconnect();
            if (isSecured()) {
                this.client = new FTPSClient(protocol, protection, impliciteSec, null, null);
            } else {
                this.client = new FTPClient();
            }
            if (this.controlEncoding != null) {
                this.client.setControlEncoding(this.controlEncoding);
                debug("control encoding : ", controlEncoding);
            }
            Logger.defaultLogger().info("Trying to connect to server : " + this.remoteServer + " ...");
            debug("connect : connect", remoteServer);
            client.connect(remoteServer, this.remotePort);
            Logger.defaultLogger().info("Received FTP server response : " + formatFTPReplyString(client.getReplyString()));
            this.connectionId = Util.getRndLong();
            reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                String msg = formatFTPReplyString(client.getReplyString());
                this.disconnect();
                throw new FTPConnectionException("Unable to communicate with remote FTP server. Got message : " + msg);
            } else {
                Logger.defaultLogger().info("Trying to log in with user : " + this.login + " ...");
                debug("connect : login", login + "/" + password);
                if (!client.login(this.login, this.password)) {
                    String msg = formatFTPReplyString(client.getReplyString());
                    this.disconnect();
                    throw new FTPConnectionException("Unable to login on FTP server (" + login + "/" + password + "). Received response : " + msg);
                } else {
                    Logger.defaultLogger().info("Logged in with user : " + this.login + ". Received response : " + formatFTPReplyString(client.getReplyString()));
                    if (this.passivMode) {
                        Logger.defaultLogger().info("Switching to passive mode ...");
                        debug("connect : pasv");
                        client.enterLocalPassiveMode();
                        reply = client.getReplyCode();
                        if (!FTPReply.isPositiveCompletion(reply)) {
                            String msg = formatFTPReplyString(client.getReplyString());
                            this.disconnect();
                            throw new FTPConnectionException("Unable to switch to passiv mode. Received response : " + msg);
                        } else {
                            this.updateOpTime();
                        }
                    } else {
                        this.updateOpTime();
                    }
                    debug("connect : bin");
                    client.setFileType(FTP.BINARY_FILE_TYPE);
                    Logger.defaultLogger().info("Connected to server : " + this.remoteServer);
                }
            }
        } catch (UnknownHostException e) {
            resetClient(e);
            throw new FTPConnectionException("Unknown FTP server : " + this.remoteServer);
        } catch (SocketException e) {
            resetClient(e);
            throw new FTPConnectionException("Error during FTP connection : " + e.getMessage());
        } catch (IOException e) {
            resetClient(e);
            throw new FTPConnectionException("Error during FTP connection : " + e.getMessage());
        } finally {
            clearCache();
        }
    }
