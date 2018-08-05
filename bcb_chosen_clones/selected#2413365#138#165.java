    public static void addToZip(ZipOutputStream zos, String filename, String nameInArchive) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File does not exist, skipping: " + filename);
            return;
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int bytesRead;
        byte[] buffer = new byte[1024];
        CRC32 crc = new CRC32();
        crc.reset();
        while ((bytesRead = bis.read(buffer)) != -1) {
            crc.update(buffer, 0, bytesRead);
        }
        bis.close();
        bis = new BufferedInputStream(new FileInputStream(file));
        String nameInArchiveFixed = nameInArchive.replace("\\", "/");
        ZipEntry entry = new ZipEntry(nameInArchiveFixed);
        entry.setMethod(ZipEntry.STORED);
        entry.setCompressedSize(file.length());
        entry.setSize(file.length());
        entry.setCrc(crc.getValue());
        zos.putNextEntry(entry);
        while ((bytesRead = bis.read(buffer)) != -1) {
            zos.write(buffer, 0, bytesRead);
        }
        bis.close();
    }
