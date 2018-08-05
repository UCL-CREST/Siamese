    public void zip(OutputStream out) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(out);
        zipOut.setMethod(ZipOutputStream.DEFLATED);
        for (Iterator it = this.zipFileList.iterator(); it.hasNext(); ) {
            File file = (File) it.next();
            byte[] rgb = new byte[1000];
            int n;
            CRC32 crc32 = new CRC32();
            FileInputStream fileIn = new FileInputStream(file);
            while ((n = fileIn.read(rgb)) > -1) {
                crc32.update(rgb, 0, n);
            }
            fileIn.close();
            ZipEntry zipentry = new ZipEntry(file.getName());
            zipentry.setMethod(ZipEntry.STORED);
            zipentry.setSize(file.length());
            zipentry.setTime(file.lastModified());
            zipentry.setCrc(crc32.getValue());
            zipOut.putNextEntry(zipentry);
            fileIn = new FileInputStream(file);
            while ((n = fileIn.read(rgb)) > -1) {
                zipOut.write(rgb, 0, n);
            }
            fileIn.close();
            zipOut.closeEntry();
        }
        zipOut.close();
    }
