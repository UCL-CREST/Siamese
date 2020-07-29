    protected boolean removeAllFiles(File file) {
        if (file == null) {
            logger.error("Cannot delete a null query file !");
            return false;
        }
        String fileName = file.getAbsolutePath();
        String savedDirFileName = DbToolConfiguration.getInstance().getQueryManagerConfig().getSavedQueryDirectory();
        if (fileName.equals(savedDirFileName)) {
            logger.error("Cannot delete the saved queries directory ! " + savedDirFileName);
            return false;
        }
        if (fileName.indexOf(savedDirFileName) < 0) {
            logger.error("The file you're attempting to delete is not under the root directory of saved queries. " + fileName);
            return false;
        }
        boolean result = true;
        if (file.isDirectory()) {
            for (File myFile : file.listFiles()) {
                result = removeAllFiles(myFile);
            }
        }
        result = file.delete();
        return result;
    }
