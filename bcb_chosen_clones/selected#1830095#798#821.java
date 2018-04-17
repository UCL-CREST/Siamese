    private static void recursivelyZipFiles(final String extension, String currentRelativePath, File currentDir, ZipOutputStream zipOutputStream) throws IOException {
        String[] files = currentDir.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                File f = new File(dir, name);
                if (extension == null) return true; else return f.isDirectory() || name.endsWith(extension);
            }
        });
        for (String filename : files) {
            File f = new File(currentDir, filename);
            String fileRelativePath = currentRelativePath + (Helper.isNullOrEmpty(currentRelativePath) ? "" : File.separator) + filename;
            if (f.isDirectory()) recursivelyZipFiles(extension, fileRelativePath, f, zipOutputStream); else {
                BufferedInputStream in = null;
                byte[] data = new byte[1024];
                in = new BufferedInputStream(new FileInputStream(f), 1000);
                zipOutputStream.putNextEntry(new ZipEntry(fileRelativePath));
                int count;
                while ((count = in.read(data, 0, data.length)) != -1) {
                    zipOutputStream.write(data, 0, count);
                }
                zipOutputStream.closeEntry();
            }
        }
    }
