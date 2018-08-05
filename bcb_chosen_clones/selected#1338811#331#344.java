    private Metadata readMetadataIndexFileFromNetwork(String mediaMetadataURI) throws IOException {
        Metadata tempMetadata = new Metadata();
        URL url = new URL(mediaMetadataURI);
        BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
        String tempLine = null;
        while ((tempLine = input.readLine()) != null) {
            Property tempProperty = PropertyList.splitStringIntoKeyAndValue(tempLine);
            if (tempProperty != null) {
                tempMetadata.addIfNotNull(tempProperty.getKey(), tempProperty.getValue());
            }
        }
        input.close();
        return tempMetadata;
    }
