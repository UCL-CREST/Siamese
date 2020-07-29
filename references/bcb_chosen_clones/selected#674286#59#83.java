    public void testRoundTrip_1(String resource) throws Exception {
        long start1 = System.currentTimeMillis();
        File originalFile = File.createTempFile("RoundTripTest", "testRoundTrip_1");
        FileOutputStream fos = new FileOutputStream(originalFile);
        IOUtils.copy(getClass().getResourceAsStream(resource), fos);
        fos.close();
        long start2 = System.currentTimeMillis();
        IsoFile isoFile = new IsoFile(new FileInputStream(originalFile).getChannel());
        long start3 = System.currentTimeMillis();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel wbc = Channels.newChannel(baos);
        long start4 = System.currentTimeMillis();
        Walk.through(isoFile);
        long start5 = System.currentTimeMillis();
        isoFile.getBox(wbc);
        wbc.close();
        long start6 = System.currentTimeMillis();
        System.err.println("Preparing tmp copy took: " + (start2 - start1) + "ms");
        System.err.println("Parsing took           : " + (start3 - start2) + "ms");
        System.err.println("Writing took           : " + (start6 - start3) + "ms");
        System.err.println("Walking took           : " + (start5 - start4) + "ms");
        byte[] a = IOUtils.toByteArray(getClass().getResourceAsStream(resource));
        byte[] b = baos.toByteArray();
        Assert.assertArrayEquals(a, b);
    }
