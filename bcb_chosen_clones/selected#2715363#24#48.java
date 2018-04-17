    public static void copyFile(File srcFile, File destFolder) {
        try {
            File destFile = new File(destFolder, srcFile.getName());
            if (destFile.exists()) {
                throw new BuildException("Could not copy " + srcFile + " to " + destFolder + " as " + destFile + " already exists");
            }
            FileChannel srcChannel = null;
            FileChannel destChannel = null;
            try {
                srcChannel = new FileInputStream(srcFile).getChannel();
                destChannel = new FileOutputStream(destFile).getChannel();
                destChannel.transferFrom(srcChannel, 0, srcChannel.size());
            } finally {
                if (srcChannel != null) {
                    srcChannel.close();
                }
                if (destChannel != null) {
                    destChannel.close();
                }
            }
            destFile.setLastModified((srcFile.lastModified()));
        } catch (IOException e) {
            throw new BuildException("Could not copy " + srcFile + " to " + destFolder + ": " + e, e);
        }
    }
