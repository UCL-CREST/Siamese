        private void handleFile(ZipOutputStream zos, String name, InputStream stream) throws IOException {
            if (stream == null) {
                ZipEntry entry = new ZipEntry(name + "/");
                zos.putNextEntry(entry);
                zos.closeEntry();
                return;
            }
            ZipEntry entry = new ZipEntry(name);
            zos.putNextEntry(entry);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (stream.available() > 0) {
                int bytesRead = stream.read(buffer, 0, BUFFER_SIZE);
                if (bytesRead == -1) {
                    break;
                }
                zos.write(buffer, 0, bytesRead);
            }
            stream.close();
            zos.closeEntry();
        }
