    public static void zipDir(String dir2zip, String baseDir, ZipOutputStream zos) {
        try {
            File fileObj = new File(dir2zip);
            String[] dirList = fileObj.list();
            byte[] readBuffer = new byte[1000];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(fileObj, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDir(filePath, baseDir, zos);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                String entryPath = f.getPath();
                int beginIndex = baseDir.length();
                entryPath = entryPath.substring(beginIndex + 1);
                ZipEntry anEntry = new ZipEntry(entryPath);
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
        }
    }
