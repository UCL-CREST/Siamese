    Document createDocument(String jnlpUrl) throws Exception {
        return new Document(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(jnlpUrl));
    }
