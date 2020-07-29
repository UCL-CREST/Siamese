    @SuppressWarnings("unchecked")
    public void addToZip(String zipFilePath, String files[]) throws Exception {
        File tmpFile = new File("temp.zip");
        ZipFile zipFile = new ZipFile(new File(zipFilePath));
        ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(tmpFile));
        ZipEntry entry = null;
        ZipEntry outEntry = null;
        java.util.Enumeration e = zipFile.getEntries();
        while (e.hasMoreElements()) {
            entry = (ZipEntry) e.nextElement();
            outEntry = new ZipEntry(entry.getName());
            InputStream zi = zipFile.getInputStream(entry);
            zo.putNextEntry(outEntry);
            while (true) {
                int n = zi.read(buffer);
                if (n < 0) break;
                zo.write(buffer, 0, n);
            }
            zi.close();
            zo.closeEntry();
        }
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]).getAbsoluteFile();
            prefix = file.getParent();
            if (!prefix.endsWith(File.separator)) prefix = prefix + File.separator;
            doZip(zo, file);
        }
        zo.close();
        zipFile.close();
        File oldZipFile = new File(zipFilePath);
        String zipFileName = oldZipFile.getName();
        oldZipFile.delete();
        tmpFile.renameTo(new File(zipFileName));
    }
