    private static void addDir(String root, File dirObj, ZipOutputStream out) throws IOException {
        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[1024];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addDir(root, files[i], out);
                continue;
            }
            if (root.equals(dirObj.getAbsolutePath()) && files[i].getName().equals("JavaSnoop.jar")) {
                continue;
            }
            FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
            String fileWithoutRootDir = files[i].getAbsolutePath();
            fileWithoutRootDir = fileWithoutRootDir.substring(root.length() + 1);
            fileWithoutRootDir = fileWithoutRootDir.replaceAll("\\\\", "/");
            ZipEntry entry = new ZipEntry(fileWithoutRootDir);
            out.putNextEntry(entry);
            int len;
            while ((len = in.read(tmpBuf)) > 0) {
                out.write(tmpBuf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
    }
