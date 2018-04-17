    private static void addFileToZip(ZipOutputStream zos, File file, File rootDir) throws IOException {
        if (file.isFile()) {
            File relativeFile = new File(file.getAbsolutePath().substring(rootDir.getPath().length() + 1));
            ZipEntry ze = new ZipEntry(relativeFile.getPath());
            zos.putNextEntry(ze);
            int count;
            byte data[] = new byte[BUF_SIZE];
            BufferedInputStream origin = new BufferedInputStream(new FileInputStream(file));
            while ((count = origin.read(data, 0, BUF_SIZE)) != -1) {
                zos.write(data, 0, count);
            }
            origin.close();
        } else {
            for (File f : file.listFiles()) {
                addFileToZip(zos, f, rootDir);
            }
        }
    }
