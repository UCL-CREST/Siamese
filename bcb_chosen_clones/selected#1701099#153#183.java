    protected static void store(FTPClient ftp, File localFile, String remotePath, boolean createDir, final IProgressMonitor monitor) throws FileNotFoundException, IOException, FtpException {
        if (localFile.isFile()) {
            if (monitor != null) {
                monitor.subTask(localFile.getName());
            }
            boolean stored = ftp.storeFile(remotePath, new FileInputStream(localFile), new CopyStreamListener() {

                public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                    if (monitor != null) {
                        monitor.worked(1);
                    }
                }

                public void bytesTransferred(CopyStreamEvent event) {
                }
            });
            if (!stored) {
                throw new FtpException("Cannot store " + localFile.getName() + " to " + remotePath);
            }
        } else if (localFile.isDirectory()) {
            String newDirectory = remotePath;
            if (createDir) {
                newDirectory = remotePath;
                ftp.makeDirectory(newDirectory);
            }
            String[] files = localFile.list();
            for (int i = 0; i < files.length; i++) {
                store(ftp, new File(localFile, files[i]), newDirectory + FtpClient.PATH_SEPARATOR + files[i], true, monitor);
            }
        }
    }
