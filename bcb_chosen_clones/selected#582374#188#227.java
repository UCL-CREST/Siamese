    private boolean zip(ZipOutputStream zos, File sourceFile, String entryPath) {
        if (entryPath == null) {
            entryPath = "";
        }
        BufferedInputStream bis = null;
        try {
            if (sourceFile.isDirectory()) {
                if (entryPath.length() != 0) {
                    entryPath += "/";
                }
                ZipEntry entry = new ZipEntry(entryPath);
                zos.putNextEntry(entry);
                File[] folder = sourceFile.listFiles();
                for (File file : folder) {
                    if (!zip(zos, file, entryPath + file.getName())) {
                        throw new Exception();
                    }
                }
            } else {
                ZipEntry entry = new ZipEntry(entryPath);
                zos.putNextEntry(entry);
                FileInputStream fis = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fis, BUFFER);
                int byteNum;
                byte[] datas = new byte[BUFFER];
                while ((byteNum = bis.read(datas, 0, BUFFER)) != -1) {
                    zos.write(datas, 0, byteNum);
                }
                bis.close();
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (bis != null) bis.close();
            } catch (IOException ioe) {
            }
        }
        return true;
    }
