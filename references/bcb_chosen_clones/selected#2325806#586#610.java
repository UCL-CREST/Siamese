    public static boolean copyFiles(File theSource, File theTarget, boolean theDescendFlag) {
        boolean ok = true;
        if (theSource.exists() && theSource.isDirectory() && theSource.canRead()) {
            if (!theTarget.exists()) {
                theTarget.mkdir();
            }
            File[] files = theSource.listFiles();
            for (File source : files) {
                String name = source.getName();
                File target = new File(theTarget.getAbsolutePath() + File.separator + name);
                if (source.isDirectory()) {
                    if (theDescendFlag) {
                        if (!copyFiles(source, target, theDescendFlag)) {
                            ok = false;
                        }
                    }
                } else {
                    if (!copyFile(source, target)) {
                        ok = false;
                    }
                }
            }
        }
        return ok;
    }
