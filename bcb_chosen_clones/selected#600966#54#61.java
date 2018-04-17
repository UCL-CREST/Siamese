    public static boolean remove(File folder) {
        boolean bDelete = true;
        boolean bRecursive = true;
        for (File child : folder.listFiles()) {
            if (SvnChecker.isSvnFolder(child)) bDelete = FileUtils.deleteDir(child); else if (child.isDirectory()) bRecursive = remove(child);
        }
        return (bDelete && bRecursive);
    }
