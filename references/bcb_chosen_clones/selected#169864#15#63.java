    public static void extractZip(Resource zip, FileObject outputDirectory) {
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(zip.getResourceURL().openStream());
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String[] pathElements = entry.getName().split("/");
                FileObject extractDir = outputDirectory;
                for (int i = 0; i < pathElements.length - 1; i++) {
                    String pathElementName = pathElements[i];
                    FileObject pathElementFile = extractDir.resolveFile(pathElementName);
                    if (!pathElementFile.exists()) {
                        pathElementFile.createFolder();
                    }
                    extractDir = pathElementFile;
                }
                String fileName = entry.getName();
                if (fileName.endsWith("/")) {
                    fileName = fileName.substring(0, fileName.length() - 1);
                }
                if (fileName.contains("/")) {
                    fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                }
                if (entry.isDirectory()) {
                    extractDir.resolveFile(fileName).createFolder();
                } else {
                    FileObject file = extractDir.resolveFile(fileName);
                    file.createFile();
                    int size = (int) entry.getSize();
                    byte[] unpackBuffer = new byte[size];
                    zis.read(unpackBuffer, 0, size);
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = new ByteArrayInputStream(unpackBuffer);
                        out = file.getContent().getOutputStream();
                        IOUtils.copy(in, out);
                    } finally {
                        IOUtils.closeQuietly(in);
                        IOUtils.closeQuietly(out);
                    }
                }
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        } finally {
            IOUtils.closeQuietly(zis);
        }
    }
