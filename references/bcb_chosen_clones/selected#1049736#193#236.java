    public void zipDir(String dir2zip, ZipOutputStream zos, String callDir) {
        try {
            File zipDir = new File(dir2zip);
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            if (manifest == false) {
                File f = new File(zipDir, "META-INF/manifest.xml");
                FileInputStream fis = new FileInputStream(f);
                String n = "META-INF/manifest.xml";
                ZipEntry anEntry = new ZipEntry(n);
                System.out.println("Archiving:" + n);
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                manifest = true;
            }
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDir(filePath, zos, callDir);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                String relativePath = f.getPath();
                relativePath = relativePath.replace(callDir + File.separator, "");
                relativePath = relativePath.replace("\\", "/");
                if (relativePath.contains("manifest") == true) {
                    continue;
                }
                ZipEntry anEntry = new ZipEntry(relativePath);
                zos.putNextEntry(anEntry);
                System.out.println("Archiving:" + relativePath);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
            System.err.println("Error during archiving.");
        }
    }
