    private void compressAuxFiles(String path, ZipOutputStream zip) throws IOException {
        String[] dirList = AgentFilesystem.listDir(path);
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (String filename : dirList) {
            File file = new File(path, filename);
            if (file.isDirectory()) {
                if (file.getPath().contains("_images") || file.getPath().contains("_styles")) compressAuxFiles(file.getPath(), zip);
                continue;
            }
            FileInputStream input = new FileInputStream(file);
            String entryPath = file.getCanonicalPath().substring(themePath.length() + 1, file.getCanonicalPath().length());
            ZipEntry entry = new ZipEntry(entryPath);
            zip.putNextEntry(entry);
            while ((bytesIn = input.read(readBuffer)) != -1) {
                zip.write(readBuffer, 0, bytesIn);
            }
            input.close();
        }
    }
