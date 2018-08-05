        public synchronized FTPClient getFTPClient(String User, String Password) throws IOException {
            if (logger.isDebugEnabled()) {
                logger.debug("getFTPClient(String, String) - start");
            }
            while ((counter >= maxClients)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    logger.error("getFTPClient(String, String)", e);
                    e.printStackTrace();
                }
            }
            FTPClient result = null;
            String key = User.concat(Password);
            logger.debug("versuche vorhandenen FTPClient aus Liste zu lesen");
            if (Clients != null) {
                if (Clients.containsKey(key)) {
                    LinkedList ClientList = (LinkedList) Clients.get(key);
                    if (!ClientList.isEmpty()) do {
                        result = (FTPClient) ClientList.getLast();
                        logger.debug("-- hole einen Client aus der Liste: " + result.toString());
                        ClientList.removeLast();
                        if (!result.isConnected()) {
                            logger.debug("---- nicht mehr verbunden.");
                            result = null;
                        } else {
                            try {
                                result.changeWorkingDirectory("/");
                            } catch (IOException e) {
                                logger.debug("---- schmei�t Exception bei Zugriff.");
                                result = null;
                            }
                        }
                    } while (result == null && !ClientList.isEmpty());
                    if (ClientList.isEmpty()) {
                        Clients.remove(key);
                    }
                } else {
                }
            } else logger.debug("-- keine Liste vorhanden.");
            if (result == null) {
                logger.debug("Kein FTPCLient verf�gbar, erstelle einen neuen.");
                result = new FTPClient();
                logger.debug("-- Versuche Connect");
                result.connect(Host);
                logger.debug("-- Versuche Login");
                result.login(User, Password);
                result.setFileType(FTPClient.BINARY_FILE_TYPE);
                if (counter == maxClients - 1) {
                    RemoveBufferedClient();
                }
            }
            logger.debug("OK: neuer FTPClient ist " + result.toString());
            ;
            counter++;
            if (logger.isDebugEnabled()) {
                logger.debug("getFTPClient(String, String) - end");
            }
            return result;
        }
