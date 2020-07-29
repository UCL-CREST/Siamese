    private static void copySmallFile(File sourceFile, File targetFile) throws BusinessException {
        LOG.debug("Copying SMALL file '" + sourceFile.getAbsolutePath() + "' to '" + targetFile.getAbsolutePath() + "'.");
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(sourceFile).getChannel();
            outChannel = new FileOutputStream(targetFile).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw new BusinessException("Could not copy file from '" + sourceFile.getAbsolutePath() + "' to '" + targetFile.getAbsolutePath() + "'!", e);
        } finally {
            try {
                if (inChannel != null) inChannel.close();
            } catch (IOException e) {
                LOG.error("Could not close input stream!", e);
            }
            try {
                if (outChannel != null) outChannel.close();
            } catch (IOException e) {
                LOG.error("Could not close output stream!", e);
            }
        }
    }
