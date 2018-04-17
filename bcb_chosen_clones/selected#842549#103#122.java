    public static void compressWithZip(Vector fileList, String zipFileName) throws IOException {
        if (fileList == null || fileList.size() == 0) return;
        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        Iterator iter = fileList.iterator();
        while (iter.hasNext()) {
            String fileName = (String) iter.next();
            int ind = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
            String shortName = "unknown";
            if (ind < fileName.length() - 1) shortName = fileName.substring(ind + 1);
            zos.putNextEntry(new ZipEntry(shortName));
            FileInputStream fis = new FileInputStream(fileName);
            byte[] buf = new byte[10000];
            int bytesRead;
            while ((bytesRead = fis.read(buf)) > 0) zos.write(buf, 0, bytesRead);
            fis.close();
            zos.closeEntry();
        }
        zos.close();
    }
