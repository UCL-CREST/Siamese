    public void testGetManifest() throws Exception {
        Support_Resources.copyFile(resources, null, JAR1);
        JarFile jarFile = new JarFile(new File(resources, JAR1));
        InputStream is = jarFile.getInputStream(jarFile.getEntry(JAR1_ENTRY1));
        assertTrue(is.available() > 0);
        assertNotNull("Error--Manifest not returned", jarFile.getManifest());
        jarFile.close();
        Support_Resources.copyFile(resources, null, JAR2);
        jarFile = new JarFile(new File(resources, JAR2));
        assertNull("Error--should have returned null", jarFile.getManifest());
        jarFile.close();
        Support_Resources.copyFile(resources, null, JAR3);
        jarFile = new JarFile(new File(resources, JAR3));
        assertNotNull("Should find manifest without verifying", jarFile.getManifest());
        jarFile.close();
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(new Attributes.Name("Manifest-Version"), "1.0");
        ByteArrayOutputStream manOut = new ByteArrayOutputStream();
        manifest.write(manOut);
        byte[] manBytes = manOut.toByteArray();
        File file = new File(Support_PlatformFile.getNewPlatformFile("hyts_manifest1", ".jar"));
        JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(file.getAbsolutePath()));
        ZipEntry entry = new ZipEntry("META-INF/");
        entry.setSize(0);
        jarOut.putNextEntry(entry);
        entry = new ZipEntry(JarFile.MANIFEST_NAME);
        entry.setSize(manBytes.length);
        jarOut.putNextEntry(entry);
        jarOut.write(manBytes);
        entry = new ZipEntry("myfile");
        entry.setSize(1);
        jarOut.putNextEntry(entry);
        jarOut.write(65);
        jarOut.close();
        JarFile jar = new JarFile(file.getAbsolutePath(), false);
        assertNotNull("Should find manifest without verifying", jar.getManifest());
        jar.close();
        file.delete();
        try {
            Support_Resources.copyFile(resources, null, JAR2);
            JarFile jF = new JarFile(new File(resources, JAR2));
            jF.close();
            jF.getManifest();
            fail("IllegalStateException expected");
        } catch (IllegalStateException ise) {
        }
    }
