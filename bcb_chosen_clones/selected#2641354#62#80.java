    public void migrateTo(String newExt) throws IOException {
        DigitalObject input = new DigitalObject.Builder(Content.byReference(new File(AllJavaSEServiceTestsuite.TEST_FILE_LOCATION + "PlanetsLogo.png").toURI().toURL())).build();
        System.out.println("Input: " + input);
        FormatRegistry format = FormatRegistryFactory.getFormatRegistry();
        MigrateResult mr = dom.migrate(input, format.createExtensionUri("png"), format.createExtensionUri(newExt), null);
        ServiceReport sr = mr.getReport();
        System.out.println("Got Report: " + sr);
        DigitalObject doOut = mr.getDigitalObject();
        assertTrue("Resulting digital object is null.", doOut != null);
        System.out.println("Output: " + doOut);
        System.out.println("Output.content: " + doOut.getContent());
        File out = new File("services/java-se/test/results/test." + newExt);
        FileOutputStream fo = new FileOutputStream(out);
        IOUtils.copyLarge(doOut.getContent().getInputStream(), fo);
        fo.close();
        System.out.println("Recieved service report: " + mr.getReport());
        System.out.println("Recieved service properties: ");
        ServiceProperties.printProperties(System.out, mr.getReport().getProperties());
    }
