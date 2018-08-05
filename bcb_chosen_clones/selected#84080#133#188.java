        public void close() throws IOException {
            output.flush();
            output.close();
            FTPClient client = new FTPClient();
            if (server == null) {
                throw new IOException("FTP_SERVER property is missing");
            } else {
                if (port != null) {
                    client.connect(server, Integer.parseInt(port));
                } else {
                    client.connect(server);
                }
            }
            if (username != null) {
                logger.debug("log in as specified user");
                client.login(username, password);
            } else {
                logger.debug("log in as anonymous");
                client.login("anonymous", this.getClass().getName());
            }
            if (binaery) {
                logger.debug("use binaery mode");
                client.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                logger.debug("use ascii mode");
                client.setFileType(FTP.ASCII_FILE_TYPE);
            }
            client.enterLocalPassiveMode();
            logger.debug("store file on server: " + tempFile + " under name: " + file);
            InputStream stream = new FileInputStream(tempFile);
            String dir = file.substring(0, file.lastIndexOf("/")) + "/";
            String split[] = dir.split("/");
            String last = "";
            logger.debug("creating dir: " + dir);
            for (int i = 0; i < split.length; i++) {
                last = last + "/" + split[i];
                logger.debug(last + " --> " + client.makeDirectory(last));
            }
            logger.debug("storing file: " + file);
            client.deleteFile(file);
            client.storeFile(file, stream);
            client.disconnect();
            tempFile.delete();
            try {
                FTPSource source = new FTPSource();
                source.configure(properties);
                source.setIdentifier(file);
                if (source.exist()) {
                    logger.debug("done");
                } else {
                    throw new IOException("can't find file I just wrote, something went wrong!");
                }
            } catch (ConfigurationException e) {
                throw new IOException(e.getMessage());
            }
        }
