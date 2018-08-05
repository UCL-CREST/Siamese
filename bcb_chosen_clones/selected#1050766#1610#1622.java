    public static void copyDir(File pSourceDir, File pDestinationDir) {
        if (pSourceDir.isDirectory()) {
            if (!pDestinationDir.exists()) {
                pDestinationDir.mkdir();
            }
            String[] children = pSourceDir.list();
            for (int i = 0; i < children.length; i++) {
                copyDir(new File(pSourceDir, children[i]), new File(pDestinationDir, children[i]));
            }
        } else {
            copyFile(pSourceDir, pDestinationDir);
        }
    }
