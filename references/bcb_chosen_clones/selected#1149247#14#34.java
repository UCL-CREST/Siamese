    public TapdocContextImpl(Registry registry, FileObject javaDom, List<String> javadocLinks, List<String> libraryLocations, FileObject outputDirectory, List<String> tapdocLinks, DocumentGenerator documentGenerator) {
        this.registry = registry;
        this.documentGenerator = documentGenerator;
        try {
            if (javaDom == null) {
                javaDom = outputDirectory.resolveFile("tapdoc-javadom.xml");
            }
            if (!javaDom.exists()) {
                javaDom.createFile();
                javaDom.close();
                IOUtils.copy(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><tapdoc-javadom></tapdoc-javadom>"), javaDom.getContent().getOutputStream());
            }
            this.javaDom = javaDom;
            this.javadocLinks = javadocLinks;
            this.tapdocLinks = tapdocLinks;
            this.libraryLocations = libraryLocations;
            this.outputDirectory = outputDirectory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
