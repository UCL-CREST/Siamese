        private void transfer(String srcPath, String destPath) {
            File srcFile = new File(srcPath);
            File destFile = new File(destPath);
            if (srcFile.isFile()) {
                long fileSize = srcFile.length();
                if (sourceDescr.mySrcAndDest.minfilesize != -1 && fileSize < sourceDescr.mySrcAndDest.minfilesize) {
                    System.out.println(srcPath + " not copied because of minFileSize violation");
                    return;
                }
                if (sourceDescr.mySrcAndDest.maxfilesize != -1 && fileSize > sourceDescr.mySrcAndDest.maxfilesize) {
                    System.out.println(srcPath + " not copied because of maxFileSize violation");
                    return;
                }
                long spaceLeft = destFile.getUsableSpace();
                if (destDescr.mySrcAndDest.minSpaceLeft != -1 && spaceLeft - fileSize < destDescr.mySrcAndDest.minSpaceLeft) {
                    System.out.println(srcPath + " not copied because of minSpaceLeft violation");
                    return;
                }
                long lastModified = srcFile.lastModified();
                long now = Calendar.getInstance().getTimeInMillis();
                long timeFromNow = now - lastModified;
                if (destDescr.mySrcAndDest.olderThanNDays != -1 && timeFromNow / (1000 * 60 * 60 * 24) < destDescr.mySrcAndDest.olderThanNDays) {
                    System.out.println(srcPath + " not copied: not older than violoation");
                    return;
                }
                if (destDescr.mySrcAndDest.youngerThanNDays != -1 && timeFromNow / (1000 * 60 * 60 * 24) > destDescr.mySrcAndDest.youngerThanNDays) {
                    System.out.println(srcPath + " not copied: not younger than violation");
                    return;
                }
                if (!srcPath.matches(sourceDescr.mySrcAndDest.includes)) {
                    System.out.println(srcPath + " not copied: not in includes");
                    return;
                }
                if (srcPath.matches(sourceDescr.mySrcAndDest.excludes)) {
                    System.out.println(srcPath + " not copied: is in excludes");
                    return;
                }
                copyFile(srcFile, destPath);
            } else {
                if (srcFile.isDirectory() & sourceDescr.mySrcAndDest.includeSubdirs) {
                    File destDir = new File(destPath + File.separator + srcFile.getName());
                    destDir.mkdirs();
                    String[] filename = srcFile.list();
                    for (int i = 0; i < filename.length; i++) {
                        transfer(srcPath + File.separator + filename[i], destDir.getPath());
                    }
                }
            }
        }
