    protected void copyOfflineView(String sourcePath, String targetPath, Integer userRecordID) {
        int BUFSIZE = 65536;
        try {
            File inputFile = new File(sourcePath);
            if (inputFile.isDirectory()) {
                File targetFile = new File(targetPath);
                if (!targetFile.exists()) {
                    targetFile.mkdir();
                }
                File listFileSource[] = inputFile.listFiles();
                if (listFileSource != null) {
                    for (int i = 0; i < listFileSource.length; i++) {
                        if (listFileSource[i].isFile()) {
                            this.copyPhysicalFiles(listFileSource[i].getPath(), targetPath + "/" + listFileSource[i].getName(), userRecordID);
                        }
                    }
                }
            } else {
                this.copyPhysicalFiles(inputFile.getPath(), targetPath, userRecordID);
            }
        } catch (Exception e) {
            this.handleException(userRecordID, e);
        }
    }
