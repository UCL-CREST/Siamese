    public boolean extract(File f, String folder) {
        Enumeration entries;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(f);
            entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
                if (entry == null) continue;
                String path = folder + "/" + entry.getName().replace('\\', '/');
                if (!entry.isDirectory()) {
                    File destFile = new File(path);
                    String parent = destFile.getParent();
                    if (parent != null) {
                        File parentFile = new File(parent);
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                    }
                    copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(destFile)));
                }
            }
            zipFile.close();
        } catch (IOException ioe) {
            this.errMsg = ioe.getMessage();
            Malgn.errorLog("{Zip.unzip} " + ioe.getMessage());
            return false;
        }
        return true;
    }
