    public static void zipDir(String dir2zip, ZipOutputStream zos, String topLevelDirectory, String topLevelPrefix) {
        try {
            File zipDir = new File(dir2zip);
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDir(filePath, zos, topLevelDirectory, topLevelPrefix);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                String filePath = f.getPath();
                logger.debug("filepath: " + filePath);
                int length = topLevelDirectory.length() + 1;
                String shortFilePath = topLevelPrefix + "/" + filePath.substring(length);
                logger.debug(shortFilePath);
                ZipEntry anEntry = new ZipEntry(shortFilePath);
                logger.debug(f.getPath());
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
            logger.error("unable to zip up directory: " + e);
        }
    }
