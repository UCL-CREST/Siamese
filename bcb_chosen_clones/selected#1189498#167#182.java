    private static File copyJarToPool(File file) {
        File outFile = new File(RizzToolConstants.TOOL_POOL_FOLDER.getAbsolutePath() + File.separator + file.getName());
        if (file != null && file.exists() && file.canRead()) {
            try {
                FileChannel inChan = new FileInputStream(file).getChannel();
                FileChannel outChan = new FileOutputStream(outFile).getChannel();
                inChan.transferTo(0, inChan.size(), outChan);
                return outFile;
            } catch (Exception ex) {
                RizzToolConstants.DEFAULT_LOGGER.error("Exception while copying jar file to tool pool [inFile=" + file.getAbsolutePath() + "] [outFile=" + outFile.getAbsolutePath() + ": " + ex);
            }
        } else {
            RizzToolConstants.DEFAULT_LOGGER.error("Could not copy jar file. File does not exist or can't read file. [inFile=" + file.getAbsolutePath() + "]");
        }
        return null;
    }
