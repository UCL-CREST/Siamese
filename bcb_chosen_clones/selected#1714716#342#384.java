    private void zipFolder(ZipOutputStream zout, String folder, String zipinPrefix) throws IOException {
        byte[] buf = new byte[1024];
        File folderDir = new File(folder);
        File[] files = folderDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (".svn".equals(file.getName())) {
                    continue;
                }
                if (file.isDirectory()) {
                    String newPrefix = null;
                    if (zipinPrefix.equals("")) {
                        newPrefix = file.getName();
                    } else {
                        newPrefix = zipinPrefix + "/" + file.getName();
                    }
                    zipFolder(zout, file.getCanonicalPath(), newPrefix);
                } else {
                    String entryName = null;
                    if (zipinPrefix.equals("")) {
                        entryName = file.getName();
                    } else {
                        entryName = zipinPrefix + "/" + file.getName();
                    }
                    ZipEntry entry = new ZipEntry(entryName);
                    zout.putNextEntry(entry);
                    FileInputStream fin = null;
                    try {
                        fin = new FileInputStream(file);
                        int bytesRead;
                        while ((bytesRead = fin.read(buf)) >= 0) {
                            zout.write(buf, 0, bytesRead);
                        }
                    } finally {
                        if (fin != null) {
                            fin.close();
                        }
                    }
                    zout.closeEntry();
                }
            }
        }
    }
