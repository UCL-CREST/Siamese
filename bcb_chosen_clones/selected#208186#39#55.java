    @Test
    public void testWrite() throws Exception {
        MrstkXmlFileReader reader = new MrstkXmlFileReader();
        reader.setFileName("..//data//MrstkXML//prototype3.xml");
        reader.read();
        SpectrumArray sp = reader.getOutput();
        File tmp = File.createTempFile("mrstktest", ".xml");
        System.out.println("Writing temp file: " + tmp.getAbsolutePath());
        MrstkXmlFileWriter writer = new MrstkXmlFileWriter(sp);
        writer.setFile(tmp);
        writer.write();
        MrstkXmlFileReader reader2 = new MrstkXmlFileReader();
        reader2.setFileName(writer.getFile().getAbsolutePath());
        reader2.read();
        SpectrumArray sp2 = reader2.getOutput();
        assertTrue(sp.equals(sp2));
    }
