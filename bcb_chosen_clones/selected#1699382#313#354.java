        public void run() {
            try {
                File tempFile = File.createTempFile("ZIPAnywhere", ".tmp");
                zipOutStream = new ZipOutputStream(new FileOutputStream(tempFile));
                if (zipFile.length() != 0) {
                    ZipFile newZip = new ZipFile(zipFile);
                    for (Enumeration eZip = newZip.entries(); eZip.hasMoreElements(); ) {
                        ZipEntry entry = (ZipEntry) eZip.nextElement();
                        try {
                            zipOutStream.putNextEntry(entry);
                            InputStream inputStream = newZip.getInputStream(entry);
                            do {
                                int j = inputStream.read(buffer, 0, buffer.length);
                                if (j <= 0) {
                                    break;
                                }
                                zipOutStream.write(buffer, 0, j);
                            } while (true);
                            inputStream.close();
                        } catch (IOException e) {
                            continue;
                        }
                    }
                    newZip.close();
                }
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        addDir(files[i]);
                    } else {
                        addFile(files[i]);
                    }
                }
                zipOutStream.close();
                String filename = zipFile.getAbsolutePath().replace(File.separatorChar, '/');
                if (zipFile.exists()) {
                    new File(filename).delete();
                }
                tempFile.renameTo(new File(filename));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
