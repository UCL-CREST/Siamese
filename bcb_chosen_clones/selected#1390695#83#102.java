    protected static void zipDir(File zipDir, ZipOutputStream zos, File root) throws IOException {
        String[] dirList = zipDir.list();
        if (dirList != null) {
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    zipDir(f, zos, root);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                String relPath = f.getPath().substring(root.getPath().length() + 1);
                ZipEntry anEntry = new ZipEntry(relPath);
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) zos.write(readBuffer, 0, bytesIn);
                fis.close();
            }
        }
    }
