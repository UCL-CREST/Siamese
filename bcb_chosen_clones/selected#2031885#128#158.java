    protected void copyFile(File sourceFile, File destFile) {
        FileChannel in = null;
        FileChannel out = null;
        try {
            if (!verifyOrCreateParentPath(destFile.getParentFile())) {
                throw new IOException("Parent directory path " + destFile.getAbsolutePath() + " did not exist and could not be created");
            }
            if (destFile.exists() || destFile.createNewFile()) {
                in = new FileInputStream(sourceFile).getChannel();
                out = new FileOutputStream(destFile).getChannel();
                in.transferTo(0, in.size(), out);
            } else {
                throw new IOException("Couldn't create file for " + destFile.getAbsolutePath());
            }
        } catch (IOException ioe) {
            if (destFile.exists() && destFile.length() < sourceFile.length()) {
                destFile.delete();
            }
            ioe.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Throwable t) {
            }
            try {
                out.close();
            } catch (Throwable t) {
            }
            destFile.setLastModified(sourceFile.lastModified());
        }
    }
