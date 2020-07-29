    public static void zipDirectory(File file, ZipOutputStream out) throws Exception {
        String[] dirList = file.list();
        byte[] readBuffer = new byte[1024];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(file, dirList[i]);
            if (f.isDirectory()) {
                zipDirectory(f, out);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            ZipEntry zipEntry = new ZipEntry(f.getPath());
            out.putNextEntry(zipEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                out.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
