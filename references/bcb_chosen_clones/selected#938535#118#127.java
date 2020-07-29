    public <T extends Document> T addDocument(Class<T> documentClass, String path) throws DocumentException {
        try {
            T document = (T) documentClass.getConstructor().newInstance();
            document.open(path);
            addDocument(document);
            return document;
        } catch (Exception e) {
            throw new DocumentException(String.format("Nepodarilo sa nájsť požadovaný konštruktor pre dokument typu %s, cesta: %s, error: %s", documentClass.getName(), path, e));
        }
    }
