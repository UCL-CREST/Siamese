    public static void writeNodeInZIPFile(Node node, File zipFile, int compressionLevel, String entryName, String header) throws IOException {
        File tempFolder = null;
        for (int i = 0; i < 10 && tempFolder == null; i++) {
            tempFolder = File.createTempFile("obj", "tmp");
            tempFolder.delete();
            if (!tempFolder.mkdirs()) {
                tempFolder = null;
            }
        }
        if (tempFolder == null) {
            throw new IOException("Couldn't create a temporary folder");
        }
        ZipOutputStream zipOut = null;
        try {
            OBJWriter writer = new OBJWriter(new File(tempFolder, entryName), header, -1);
            writer.writeNode(node);
            writer.close();
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.setLevel(compressionLevel);
            for (File tempFile : tempFolder.listFiles()) {
                if (tempFile.isFile()) {
                    InputStream tempIn = null;
                    try {
                        zipOut.putNextEntry(new ZipEntry(tempFile.getName()));
                        tempIn = new FileInputStream(tempFile);
                        byte[] buffer = new byte[8096];
                        int size;
                        while ((size = tempIn.read(buffer)) != -1) {
                            zipOut.write(buffer, 0, size);
                        }
                        zipOut.closeEntry();
                    } finally {
                        if (tempIn != null) {
                            tempIn.close();
                        }
                    }
                }
            }
        } finally {
            if (zipOut != null) {
                zipOut.close();
            }
            for (File tempFile : tempFolder.listFiles()) {
                if (tempFile.isFile()) {
                    tempFile.delete();
                }
            }
            tempFolder.delete();
        }
    }
