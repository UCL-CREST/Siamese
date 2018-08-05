    private boolean emptyFolder(File folder) {
        Boolean emptyResult = false;
        if (folder.exists()) {
            File filesInFolder[] = folder.listFiles();
            if (filesInFolder != null) {
                for (File aFile : filesInFolder) {
                    if (aFile.isDirectory()) {
                        emptyFolder(aFile);
                    } else {
                        boolean checkDelete = aFile.delete();
                        if (!checkDelete) {
                            log.warn("Could not delete " + aFile.toString());
                        }
                    }
                }
            }
            emptyResult = folder.delete();
        } else {
            emptyResult = true;
        }
        return emptyResult;
    }
