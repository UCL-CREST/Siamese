    public void testRetrieve() throws DigitalObjectManager.DigitalObjectNotFoundException, URISyntaxException, IOException {
        DigitalObjectManager man = new FedoraObjectManager("fedoraAdmin", "fedoraAdminPass", "http://localhost:7910/fedora");
        DigitalObject r = man.retrieve(new URI("demo:dc2mods.1"));
        String title = r.getTitle();
        List<Metadata> met = r.getMetadata();
        InputStream content = r.getContent().read();
        StringWriter theString = new StringWriter();
        IOUtils.copy(content, theString);
        assertNotNull(theString.toString(), "Content should not be null");
        assertNotNull(title, "The title should be set");
        assertNotNull(met.get(0).getContent(), "There should be some metadata");
    }
