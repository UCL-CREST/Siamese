    private void zipDirIntern(String layoutRootDir, String dir2zip, ZipOutputStream zos) throws IOException {
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                String filePath = f.getPath();
                zipDirIntern(layoutRootDir, filePath, zos);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            String _path = StringUtils.replace(f.getPath(), layoutRootDir, "");
            if (_path.startsWith("/") || _path.startsWith("\\")) _path = _path.substring(1);
            ZipEntry anEntry = new ZipEntry(_path);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
