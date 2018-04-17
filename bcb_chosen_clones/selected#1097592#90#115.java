    private void copyFileToStream(String filename, String dirname, ZipOutputStream zipper, String comment) throws IOException {
        File file = new File(filename);
        String entryName;
        if (dirname != null) {
            entryName = dirname + "/" + file.getName();
        } else {
            entryName = file.getName();
        }
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipEntry.setTime(file.lastModified());
        zipEntry.setSize(file.length());
        if (comment != null) {
            zipEntry.setComment(comment);
        }
        zipper.putNextEntry(zipEntry);
        FileInputStream fis = new FileInputStream(file);
        try {
            int c;
            while ((c = fis.read()) != -1) {
                zipper.write(c);
            }
        } finally {
            fis.close();
        }
        zipper.closeEntry();
    }
