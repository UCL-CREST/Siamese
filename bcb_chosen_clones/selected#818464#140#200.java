    public boolean receiveFile(FileDescriptor fileDescriptor) {
        try {
            byte[] block = new byte[1024];
            int sizeRead = 0;
            int totalRead = 0;
            File dir = new File(Constants.DOWNLOAD_DIR + fileDescriptor.getLocation());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(Constants.DOWNLOAD_DIR + fileDescriptor.getLocation() + fileDescriptor.getName());
            if (!file.exists()) {
                file.createNewFile();
            }
            SSLSocket sslsocket = getFileTransferConectionConnectMode(ServerAdress.getServerAdress());
            OutputStream fileOut = new FileOutputStream(file);
            InputStream dataIn = sslsocket.getInputStream();
            while ((sizeRead = dataIn.read(block)) >= 0) {
                totalRead += sizeRead;
                fileOut.write(block, 0, sizeRead);
                propertyChangeSupport.firePropertyChange("fileByte", 0, totalRead);
            }
            fileOut.close();
            dataIn.close();
            sslsocket.close();
            if (fileDescriptor.getName().contains(".snapshot")) {
                try {
                    File fileData = new File(Constants.DOWNLOAD_DIR + fileDescriptor.getLocation() + fileDescriptor.getName());
                    File dirData = new File(Constants.PREVAYLER_DATA_DIRETORY + Constants.FILE_SEPARATOR);
                    File destino = new File(dirData, fileData.getName());
                    boolean success = fileData.renameTo(destino);
                    if (!success) {
                        deleteDir(Constants.DOWNLOAD_DIR);
                        return false;
                    }
                    deleteDir(Constants.DOWNLOAD_DIR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (Server.isServerOpen()) {
                    FileChannel inFileChannel = new FileInputStream(file).getChannel();
                    File dirServer = new File(Constants.DOWNLOAD_DIR + fileDescriptor.getLocation());
                    if (!dirServer.exists()) {
                        dirServer.mkdirs();
                    }
                    File fileServer = new File(Constants.DOWNLOAD_DIR + fileDescriptor.getName());
                    if (!fileServer.exists()) {
                        fileServer.createNewFile();
                    }
                    inFileChannel.transferTo(0, inFileChannel.size(), new FileOutputStream(fileServer).getChannel());
                    inFileChannel.close();
                }
            }
            if (totalRead == fileDescriptor.getSize()) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Receive File:", e);
        }
        return false;
    }
