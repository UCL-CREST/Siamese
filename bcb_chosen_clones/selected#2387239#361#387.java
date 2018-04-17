    public boolean backupFile(File oldFile, File newFile) {
        boolean isBkupFileOK = false;
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;
        try {
            sourceChannel = new FileInputStream(oldFile).getChannel();
            targetChannel = new FileOutputStream(newFile).getChannel();
            targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception occurred while copying file", e);
        } finally {
            if ((newFile != null) && (newFile.exists()) && (newFile.length() > 0)) {
                isBkupFileOK = true;
            }
            try {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
                if (targetChannel != null) {
                    targetChannel.close();
                }
            } catch (IOException e) {
                logger.log(Level.INFO, "closing channels failed");
            }
        }
        return isBkupFileOK;
    }
