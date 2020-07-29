        public void visitFile(File f) {
            String path = f.getPath();
            if (transformer != null) {
                path = transformer.transform(path);
            }
            ZipEntry ze = new ZipEntry(path);
            try {
                zipOutputStream.putNextEntry(ze);
                InputStream is = new BufferedInputStream(new FileInputStream(f));
                byte[] buffer = new byte[4096];
                int bytesRead = is.read(buffer);
                while (bytesRead > 0) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                    bytesRead = is.read(buffer);
                }
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                ok = false;
                caughtException = e;
            }
        }
