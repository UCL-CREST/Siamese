    protected void syncFile(File src, File dest) throws IOException {
        if (src.exists()) {
            if (!dest.exists()) {
                copyFile(src, dest);
            } else {
                if (src.isDirectory()) {
                    if (dest.isDirectory()) {
                        int i, j;
                        File[] srcChildren = src.listFiles();
                        File[] destChildren = dest.listFiles();
                        for (i = 0; i < srcChildren.length; i++) {
                            File srcChild = srcChildren[i];
                            File destChild = new File(dest, srcChild.getName());
                            syncFile(srcChild, destChild);
                        }
                        for (j = 0; j < destChildren.length; j++) {
                            File destChild = destChildren[j];
                            File srcChild = null;
                            boolean found = false;
                            for (i = 0; i < srcChildren.length; i++) {
                                srcChild = srcChildren[i];
                                if (srcChild.getName().equals(destChild.getName())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                removeFile(destChild);
                            }
                        }
                    } else {
                        removeFile(dest);
                        copyFile(src, dest);
                    }
                } else if (dest.isDirectory() || (src.lastModified() > dest.lastModified())) {
                    removeFile(dest);
                    copyFile(src, dest);
                }
            }
        }
    }
