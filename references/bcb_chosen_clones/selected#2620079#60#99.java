    @SuppressWarnings("unchecked")
    public void removeFromZip(String zipFilePath, String entryNames[]) throws Exception {
        File tmpFile = new File("temp1.zip");
        ZipFile zipFile = new ZipFile(new File(zipFilePath));
        ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(tmpFile));
        ZipEntry entry = null;
        ZipEntry outEntry = null;
        boolean needRemove = false;
        java.util.Enumeration e = zipFile.getEntries();
        while (e.hasMoreElements()) {
            needRemove = false;
            entry = (org.apache.tools.zip.ZipEntry) e.nextElement();
            for (int i = 0; i < entryNames.length; i++) {
                String entryName = entry.getName();
                if (entryNames[i].equals(entryName)) {
                    needRemove = true;
                } else {
                    int length = entryNames[i].length();
                    if (entryName.startsWith(entryNames[i]) && (entryName.substring(length, length + 1).equals("/") || entryName.substring(length, length + 1).equals("\\"))) needRemove = true;
                }
            }
            if (needRemove) continue;
            outEntry = new ZipEntry(entry.getName());
            zo.putNextEntry(outEntry);
            InputStream zi = zipFile.getInputStream(entry);
            while (true) {
                int n = zi.read(buffer);
                if (n < 0) break;
                zo.write(buffer, 0, n);
            }
            zo.closeEntry();
            zi.close();
        }
        zipFile.close();
        zo.close();
        File oldZipFile = new File(zipFilePath);
        String zipFileName = oldZipFile.getName();
        oldZipFile.delete();
        tmpFile.renameTo(new File(zipFileName));
    }
