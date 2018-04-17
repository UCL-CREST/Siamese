        public void run() {
            if (currentNode == null || currentNode.equals("")) {
                JOptionPane.showMessageDialog(null, "Please select a genome to download first", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String localFile = parameter.getTemporaryFilesPath() + currentNode;
            String remotePath = NCBI_FTP_PATH + currentPath;
            String remoteFile = remotePath + "/" + currentNode;
            try {
                ftp.connect(NCBI_FTP_HOST);
                int reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    JOptionPane.showMessageDialog(null, "FTP server refused connection", "Error", JOptionPane.ERROR_MESSAGE);
                }
                ftp.login("anonymous", "anonymous@big.ac.cn");
                inProgress = true;
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                long size = getFileSize(remotePath, currentNode);
                if (size == -1) throw new FileNotFoundException();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
                BufferedInputStream in = new BufferedInputStream(ftp.retrieveFileStream(remoteFile), ftp.getBufferSize());
                byte[] b = new byte[1024];
                long bytesTransferred = 0;
                int tick = 0;
                int oldTick = 0;
                int len;
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                    bytesTransferred += 1024;
                    if ((tick = new Long(bytesTransferred * 100 / size).intValue()) > oldTick) {
                        progressBar.setValue(tick < 100 ? tick : 99);
                        oldTick = tick;
                    }
                }
                in.close();
                out.close();
                ftp.completePendingCommand();
                progressBar.setValue(100);
                fileDownloaded = localFile;
                JOptionPane.showMessageDialog(null, "File successfully downloaded", "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
                ftp.logout();
            } catch (SocketException ex) {
                JOptionPane.showMessageDialog(null, "Error occurs while trying to connect server", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "This file is not found on the server", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error occurs while fetching data", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                inProgress = false;
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException ioe) {
                    }
                }
            }
        }
