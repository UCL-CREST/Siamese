    private void deleteAll() throws IOException {
        FTPFile[] children = ftp.listFiles();
        for (int i = 0; i < children.length; i++) {
            FTPFile child = children[i];
            if (child.isDirectory()) {
                ftp.changeWorkingDirectory(child.getName());
                deleteAll();
                ftp.changeToParentDirectory();
                log.debug("Removing directory: " + child.getName());
                ftp.removeDirectory(child.getName());
            } else if (child.isFile()) {
                log.debug("Removing file: " + child.getName());
                ftp.deleteFile(child.getName());
            }
        }
    }
