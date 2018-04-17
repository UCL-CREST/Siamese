    protected void packRec(File file, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++) {
                packRec(new File(file, children[i]), zos);
            }
        } else {
            String filePath = file.getPath();
            String entryPath = filePath.substring(dir.length() + 1).replace('\\', '/');
            ZipEntry entry = new ZipEntry(entryPath);
            zos.putNextEntry(entry);
            FileInputStream fis = new FileInputStream(file);
            byte[] b = new byte[2048];
            int tmpLen;
            while ((tmpLen = fis.read(b)) != -1) {
                zos.write(b, 0, tmpLen);
            }
            zos.flush();
            fis.close();
        }
    }
