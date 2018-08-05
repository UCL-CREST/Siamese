    public void test_1562() throws Exception {
        Manifest man = new Manifest();
        Attributes att = man.getMainAttributes();
        att.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        att.put(Attributes.Name.MAIN_CLASS, "foo.bar.execjartest.Foo");
        File outputZip = File.createTempFile("hyts_", ".zip");
        outputZip.deleteOnExit();
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(outputZip));
        File resources = Support_Resources.createTempFolder();
        for (String zipClass : new String[] { "Foo", "Bar" }) {
            zout.putNextEntry(new ZipEntry("foo/bar/execjartest/" + zipClass + ".class"));
            zout.write(getResource(resources, "hyts_" + zipClass + ".ser"));
        }
        zout.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        man.write(zout);
        zout.close();
        String[] args = new String[] { "-jar", outputZip.getAbsolutePath() };
        String res = Support_Exec.execJava(args, null, false);
        assertTrue("Error executing ZIP : result returned was incorrect.", res.startsWith("FOOBAR"));
    }
