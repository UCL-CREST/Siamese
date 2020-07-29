    private static boolean copyFilesRecusively(final File toCopy, final File destDir) {
        assert destDir.isDirectory();
        if (!toCopy.isDirectory()) {
            return FileUtil.copyFile(toCopy, new File(destDir, toCopy.getName()));
        } else {
            final File newDestDir = new File(destDir, toCopy.getName());
            if (!newDestDir.exists() && !newDestDir.mkdir()) {
                return false;
            }
            for (final File child : toCopy.listFiles()) {
                if (!FileUtil.copyFilesRecusively(child, newDestDir)) {
                    return false;
                }
            }
        }
        return true;
    }
