        private void compressFile(File file, String path) throws IOException {
            if (!file.isDirectory()) {
                byte[] buf = new byte[BUFFER_SIZE];
                int len;
                FileInputStream in = new FileInputStream(file);
                zos.putNextEntry(new ZipEntry(path + "/" + file.getName()));
                while ((len = in.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                in.close();
                return;
            }
            if (file.list() == null) {
                return;
            }
            for (String fileName : file.list()) {
                File f = new File(file.getAbsolutePath() + File.separator + fileName);
                compressFile(f, path + File.separator + file.getName());
                isCompressed++;
                progressDialog.setProgress((isCompressed * 100) / fileCount);
            }
        }
