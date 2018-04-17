    private List<? extends Attachment> createZipAttachment(List<FileSystemItem> nodes, boolean withFolders) throws IOException {
        File tmp = File.createTempFile("securus", null);
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(tmp));
        try {
            for (FileSystemItem node : nodes) {
                String path;
                if (withFolders) {
                    path = node.getFile();
                } else {
                    path = node.getFile();
                }
                zip.putNextEntry(new ZipEntry(path));
                InputStream in = new BufferedInputStream(node.getContent());
                int b;
                while ((b = in.read()) != -1) {
                    zip.write(b);
                }
                zip.closeEntry();
            }
        } finally {
            zip.close();
        }
        return Arrays.asList(new ZipAttachment(tmp));
    }
