    public void zipDir(File zipDir, ZipOutputStream zos, File root) {
        try {
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[1024];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    zipDir(f, zos, root);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                ZipEntry anEntry = new ZipEntry(f.getPath().replace(root + File.separator, ""));
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
