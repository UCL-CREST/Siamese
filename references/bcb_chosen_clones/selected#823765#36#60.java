    public void addEntry(String zipEntryName, InputStream iStream, boolean compress) throws IOException {
        zipEntryName = zipEntryName.replace('\\', '/');
        if (iStream == null) {
            if (zipEntryName.length() < 1 || zipEntryName.charAt(zipEntryName.length() - 2) != '/') {
                zipEntryName = zipEntryName + "/";
            }
            ZipEntry zEntry = new ZipEntry(zipEntryName);
            try {
                zOut.putNextEntry(zEntry);
            } catch (Exception e) {
            }
            return;
        }
        ZipEntry zEntry = new ZipEntry(zipEntryName);
        if (compress == true) zEntry.setMethod(ZipEntry.DEFLATED); else zEntry.setMethod(ZipEntry.STORED);
        zOut.putNextEntry(zEntry);
        byte[] data = new byte[10000];
        int inByte = iStream.read(data);
        while (inByte != -1) {
            zOut.write(data, 0, inByte);
            inByte = iStream.read(data);
        }
        zOut.closeEntry();
        iStream.close();
    }
