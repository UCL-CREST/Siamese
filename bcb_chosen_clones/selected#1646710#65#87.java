    private static void recurseFiles(File file, String baseDirectory, ZipOutputStream zos) throws IOException, FileNotFoundException {
        if (file.isDirectory()) {
            String[] fileNames = file.list();
            if (fileNames != null) {
                for (int i = 0; i < fileNames.length; i++) {
                    recurseFiles(new File(file, fileNames[i]), baseDirectory, zos);
                }
            }
        } else {
            byte[] buf = new byte[1024];
            int len;
            String fileRelativePath = file.getPath().substring(baseDirectory.length() + 1);
            ZipEntry zipEntry = new ZipEntry(fileRelativePath);
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);
            zos.putNextEntry(zipEntry);
            while ((len = in.read(buf)) >= 0) {
                zos.write(buf, 0, len);
            }
            in.close();
            zos.closeEntry();
        }
    }
