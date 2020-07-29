    public static void copyFolder(String srcPath, String destPath, boolean overwrite) {
        File srcFolder = new File(srcPath);
        if (srcFolder.exists() && srcFolder.isDirectory()) {
            File destFolder = new File(destPath);
            if (destFolder.exists() && destFolder.isDirectory()) {
            } else {
                try {
                    destFolder.mkdirs();
                } catch (Exception e) {
                }
            }
            FileFilter filter = new FileFilter() {

                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        String name = file.getName();
                        if (name.equals("CVS")) {
                            return false;
                        }
                    }
                    if (file.getAbsolutePath().endsWith(".swp")) {
                        return false;
                    }
                    return true;
                }
            };
            File[] files = srcFolder.listFiles(filter);
            for (int i = 0; i < files.length; i++) {
                File file = new File(destFolder, files[i].getName());
                if (files[i].isDirectory()) {
                    copyFolder(files[i].getAbsolutePath(), file.getAbsolutePath(), overwrite);
                } else {
                    if (!file.exists() || overwrite) {
                        streamCopyFile(files[i], file);
                    }
                }
            }
        }
    }
