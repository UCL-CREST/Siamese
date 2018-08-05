    @Test
    public void testWrite() {
        System.out.println("write");
        final File[] files = { new File(sharePath) };
        System.out.println("Creating hash...");
        String initHash = MD5File.MD5Directory(files[0]);
        System.out.println("Hash: " + initHash);
        Share readShare = ShareUtility.createShare(files, "TestShare");
        System.out.println("Creating shares...");
        final ShareFolder[] readers = ShareUtility.cropShareToParts(readShare, PARTS);
        System.out.println("Reading and writing shares...");
        done = 0;
        for (int i = 0; i < PARTS; i++) {
            final int j = i;
            new Thread() {

                public void run() {
                    ShareFolder part = (ShareFolder) ObjectClone.clone(readers[j]);
                    ShareFileReader reader = new ShareFileReader(readers[j], files[0]);
                    ShareFileWriter writer = new ShareFileWriter(part, new File("Downloads/" + readers[j].getName()));
                    long tot = 0;
                    byte[] b = new byte[(int) (Math.random() * 10000)];
                    while (tot < readers[j].getSize()) {
                        reader.read(b);
                        byte[] bwrite = new byte[(int) (Math.random() * 10000) + b.length];
                        System.arraycopy(b, 0, bwrite, 0, b.length);
                        writer.write(bwrite, b.length);
                        tot += b.length;
                    }
                    done++;
                    System.out.println((int) (done * 100.0 / PARTS) + "% Complete");
                }
            }.start();
        }
        while (done < PARTS) {
            Thread.yield();
        }
        File resultFile = new File("Downloads/" + readShare.getName());
        System.out.println("Creating hash of written share...");
        String resultHash = MD5File.MD5Directory(resultFile);
        System.out.println("Init hash:   " + initHash);
        System.out.println("Result hash: " + resultHash);
        assertEquals(initHash, resultHash);
    }
