    private void zipTempDir(String dir2zip, ZipOutputStream zos) {
        try {
            File zipDir = new File(dir2zip);
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipTempDir(filePath, zos);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                String filePath = f.getPath();
                filePath = getZipPath(filePath);
                ZipEntry anEntry = new ZipEntry(filePath);
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
            System.out.println("Could not do the zipDir process. debug zipDir()");
            System.out.println("This would be an internal variable configuration error. I guess you'll have to debug it");
            e.printStackTrace();
            System.exit(1);
        }
    }
