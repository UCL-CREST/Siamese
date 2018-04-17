    private void packageFile(ZipOutputStream zos, String filename) throws IOException {
        String filepath = environment.toProjectFile(projectName, filename);
        File file = new File(filepath);
        byte[] data = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        int bytesRead = 0;
        while (bytesRead < data.length) {
            int n = bis.read(data, bytesRead, data.length - bytesRead);
            bytesRead += n;
        }
        zos.putNextEntry(new ZipEntry(filename));
        zos.write(data, 0, data.length);
        zos.closeEntry();
    }
