    private void saveImages(ZipOutputStream zOut) throws IOException {
        List<String> added = new ArrayList<String>();
        for (Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext(); ) {
            EObject eObject = iter.next();
            if (eObject instanceof IDiagramModelImageProvider) {
                IDiagramModelImageProvider imageProvider = (IDiagramModelImageProvider) eObject;
                String imagePath = imageProvider.getImagePath();
                if (imagePath != null && !added.contains(imagePath)) {
                    byte[] bytes = BYTE_ARRAY_STORAGE.getEntry(imagePath);
                    if (bytes != null) {
                        ZipEntry zipEntry = new ZipEntry(imagePath);
                        zOut.putNextEntry(zipEntry);
                        zOut.write(bytes);
                        zOut.closeEntry();
                        added.add(imagePath);
                    }
                }
            }
        }
    }
