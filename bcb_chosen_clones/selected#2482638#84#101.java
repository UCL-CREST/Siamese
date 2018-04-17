    private boolean recursiveDirDeletion(String path) throws IOException {
        boolean erfolg = true;
        FTPFile[] ftpFiles = this.filterDotDirs(this.ftpClient.listFiles(path));
        for (int xx = 0; xx < ftpFiles.length; xx++) {
            StringBuffer actEntry = new StringBuffer(path).append('/');
            actEntry.append(ftpFiles[xx].getName());
            if (!ftpFiles[xx].isDirectory()) {
                erfolg = this.deleteFile(actEntry.toString());
            } else {
                erfolg = this.recursiveDirDeletion(actEntry.toString());
            }
            if (!erfolg) {
                break;
            }
        }
        erfolg = this.removeDirectory(path);
        return (erfolg);
    }
