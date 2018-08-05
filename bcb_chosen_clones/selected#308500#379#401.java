    public static boolean copyFileRecursively(File src, File dest, boolean overwrite) {
        String destAbsPath = dest.getAbsolutePath();
        if (!src.exists()) return false;
        if (src.isDirectory()) {
            if (!dest.exists()) {
                if (!dest.mkdirs()) {
                    return false;
                }
            }
            File[] children = src.listFiles();
            for (int i = 0; i < children.length; i++) {
                String destPath = destAbsPath + "/" + children[i].getName();
                MiscUtils.copyFileRecursively(children[i], new File(destPath), overwrite);
            }
        } else {
            try {
                MiscUtils.copy(src.getAbsolutePath(), dest.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }
