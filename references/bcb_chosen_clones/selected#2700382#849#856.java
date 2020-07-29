    private void addDocumentToZip(ZipOutputStream out, Document doc) throws IOException {
        if (!savedDocuments.contains(doc)) {
            ZipEntry entry = new ZipEntry(FDDPMA.DOCUMENT_FILE_PREFIX + doc.getId());
            out.putNextEntry(entry);
            out.write(doc.getContent());
            savedDocuments.add(doc);
        }
    }
