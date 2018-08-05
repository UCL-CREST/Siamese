    public void copyFolder(File srcFolder, File destFolder) throws IOException {
        if (srcFolder.isDirectory()) {
            if (!destFolder.exists()) {
                destFolder.mkdir();
            }
            String[] oChildren = srcFolder.list();
            for (int i = 0; i < oChildren.length; i++) {
                copyFolder(new File(srcFolder, oChildren[i]), new File(destFolder, oChildren[i]));
            }
        } else {
            if (destFolder.isDirectory()) {
                copyFile(srcFolder, new File(destFolder, srcFolder.getName()));
            } else {
                copyFile(srcFolder, destFolder);
            }
        }
    }
