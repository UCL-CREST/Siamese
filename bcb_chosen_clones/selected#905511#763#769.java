    private static void exportMetadataItem(ZipOutputStream aZOS, JStellaGame aMetadata) throws IOException {
        ZipEntry zEntry = new ZipEntry(aMetadata.getGameFilename());
        aZOS.putNextEntry(zEntry);
        byte[] zData = toByteArray(aMetadata);
        aZOS.write(zData);
        aZOS.closeEntry();
    }
