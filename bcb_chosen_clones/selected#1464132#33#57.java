    @BeforeClass
    public static void setUpOnce() throws OWLOntologyCreationException {
        dbManager = (OWLDBOntologyManager) OWLDBManager.createOWLOntologyManager(OWLDataFactoryImpl.getInstance());
        dbIRI = IRI.create(ontoUri);
        System.out.println("copying ontology to work folder...");
        try {
            final File directory = new File("./resources/LUBM10-DB-forUpdate/");
            final File[] filesToDelete = directory.listFiles();
            if (filesToDelete != null && filesToDelete.length > 0) {
                for (final File file : filesToDelete) {
                    if (!file.getName().endsWith(".svn")) Assert.assertTrue(file.delete());
                }
            }
            final File original = new File("./resources/LUBM10-DB/LUBM10.h2.db");
            final File copy = new File("./resources/LUBM10-DB-forUpdate/LUBM10.h2.db");
            final FileChannel inChannel = new FileInputStream(original).getChannel();
            final FileChannel outChannel = new FileOutputStream(copy).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (final IOException ioe) {
            System.err.println(ioe.getMessage());
            Assert.fail();
        }
        onto = (OWLMutableOntology) dbManager.loadOntology(dbIRI);
        factory = dbManager.getOWLDataFactory();
    }
