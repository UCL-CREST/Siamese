    public void saveZipDocumentPreservingZipContents(OutputStream out, ZipFileSystem zfs) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(out);
        zos.putNextEntry(new ZipEntry(name + ".jclic"));
        saveDocument(zos);
        zos.closeEntry();
        String[] entries = zfs.getEntries(null);
        if (entries != null) {
            for (int i = 0; i < entries.length; i++) {
                if (!entries[i].endsWith(".jclic")) {
                    zos.putNextEntry(new ZipEntry(entries[i]));
                    zos.write(zfs.getBytes(entries[i]));
                    zos.closeEntry();
                }
            }
        }
        zos.close();
        out.close();
    }
