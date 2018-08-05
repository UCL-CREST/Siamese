    private void addToZip(String path, String srcFile, ZipOutputStream zip) {
        File folder = new File(srcFile);
        if (folder.isDirectory()) {
            this.addFolderToZip(path, srcFile, zip);
        } else {
            try {
                boolean includeFile = false;
                if (this.excludePattern == null) {
                    includeFile = true;
                } else {
                    if (srcFile.contains(this.excludePattern)) {
                        includeFile = false;
                    } else {
                        includeFile = true;
                    }
                }
                if (includeFile == true) {
                    this.zipMonitor.setNumberNextFile();
                    this.zipMonitor.setCurrentJobFile(srcFile);
                    if (zipMonitor.isCanceled()) {
                        return;
                    }
                    FileInputStream in = new FileInputStream(srcFile);
                    zip.putNextEntry(new ZipEntry(path + File.separator + folder.getName()));
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        zip.write(buf, 0, len);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
