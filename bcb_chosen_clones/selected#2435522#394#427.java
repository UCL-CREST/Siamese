        private void zipContainer(String containerName, ZipOutputStream out) throws NxqdException, IOException {
            NxqdContainer container = manager.getContainer(containerName);
            java.util.List contents = container.listDocuments();
            if (contents.size() == 0) {
                out.putNextEntry(new ZipEntry(containerName + '/'));
                out.closeEntry();
            } else {
                String documentId;
                byte[] buf;
                for (int i = 0; i < contents.size(); i++) {
                    documentId = contents.get(i).toString();
                    out.putNextEntry(new ZipEntry(containerName + '/' + documentId));
                    String document = container.getDocument(documentId).asString();
                    buf = document.getBytes();
                    out.write(buf, 0, buf.length);
                    out.closeEntry();
                }
            }
            contents = container.listBlobs();
            if (contents.size() == 0) {
                out.putNextEntry(new ZipEntry(containerName + '/'));
                out.closeEntry();
            } else {
                String documentId;
                byte[] buf;
                for (int i = 0; i < contents.size(); i++) {
                    documentId = contents.get(i).toString();
                    out.putNextEntry(new ZipEntry(containerName + '/' + documentId + BLOB_SUFFIX));
                    buf = container.getBlob(documentId).getBlob();
                    out.write(buf, 0, buf.length);
                    out.closeEntry();
                }
            }
        }
