    @Override
    public void setDocumentSpace(DocumentSpace space) {
        for (Document doc : space) {
            File result = new File(parent, doc.getName());
            if (doc instanceof XMLDOMDocument) {
                new PlainXMLDocumentWriter(result).writeDocument(doc);
            } else if (doc instanceof BinaryDocument) {
                BinaryDocument bin = (BinaryDocument) doc;
                try {
                    IOUtils.copy(bin.getContent().getInputStream(), new FileOutputStream(result));
                } catch (IOException e) {
                    throw ManagedIOException.manage(e);
                }
            } else {
                System.err.println("Unkown Document type");
            }
        }
    }
