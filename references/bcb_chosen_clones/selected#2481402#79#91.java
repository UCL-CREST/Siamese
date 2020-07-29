    static ZipEntry save(ZipOutputStream zip_out, ZipNode node, ZipStreamFilter filter) {
        ZipEntry entry = new ZipEntry(node.getEntryName());
        try {
            entry.setTime(0);
            zip_out.putNextEntry(entry);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            filter.writeNode(baos, node);
            zip_out.write(baos.toByteArray());
        } catch (Exception err) {
            err.printStackTrace();
        }
        return entry;
    }
