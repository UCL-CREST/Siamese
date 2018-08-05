    public static boolean copyTree(String sourceDir, String targetRoot) {
        boolean result;
        try {
            File source = new File(sourceDir);
            File root = new File(targetRoot);
            if (source.exists() == false || source.isDirectory() == false) {
                log.error("Source path dosn't exsist (\"" + source.getCanonicalPath() + "\"). Can't copy files.");
                return false;
            }
            if (root.exists() == false) {
                log.error("Destination path dosn't exsist (\"" + root.getCanonicalPath() + "\").");
                log.info("Creating destination directory.");
                if (!root.mkdirs()) {
                    log.equals("Creating destination directory faild!");
                    return false;
                }
            }
            String targetRootName = Paths.checkPathEnding(root.getCanonicalPath());
            ArrayList<File> fileNames = listAllFiles(source, true);
            result = true;
            File target;
            for (File f : fileNames) {
                String fullName = f.getCanonicalPath();
                int pos = fullName.indexOf(sourceDir);
                String subName = null;
                if (sourceDir.endsWith("/")) subName = fullName.substring(pos + sourceDir.length()); else subName = fullName.substring(pos + sourceDir.length() + 1);
                String targetName = targetRootName + subName;
                target = new File(targetName);
                if (f.isDirectory()) {
                    if (target.exists() == false) {
                        boolean st = target.mkdir();
                        if (st == false) result = false;
                    }
                    continue;
                }
                boolean st = fileCopy(f, target);
                if (st == false) result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
