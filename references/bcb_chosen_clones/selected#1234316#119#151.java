    public Archive save(OutputStream dest) throws IOException {
        ZipOutputStream out;
        InputStream in;
        List<Node> content;
        List<Node> files;
        out = new ZipOutputStream(dest);
        if (manifest != null) {
            out.putNextEntry(new ZipEntry(MANIFEST));
            manifest.write(out);
            out.closeEntry();
        }
        content = data.find("**/*");
        files = new ArrayList<Node>();
        for (Node node : content) {
            if (isManifest(node)) {
                throw new ArchiveException("manifest file not allowed");
            } else if (node.isFile()) {
                files.add(node);
            } else {
                out.putNextEntry(new ZipEntry(node.getPath() + "/"));
                out.closeEntry();
            }
        }
        for (Node file : files) {
            in = file.createInputStream();
            out.putNextEntry(new ZipEntry(file.getPath()));
            file.getWorld().getBuffer().copy(in, out);
            out.closeEntry();
            in.close();
        }
        out.close();
        return this;
    }
