    public static void main(String argv[]) {
        String cp = System.getProperty("java.class.path");
        String sep = System.getProperty("file.separator");
        String dirpath = cp.substring(0, cp.lastIndexOf(sep));
        String jarpath = dirpath + "/WiiRemoteJ.jar";
        if (!new File(jarpath).exists()) {
            try {
                System.out.println("getting WiiRemoteJ file from remote source...");
                URL wiiRemLoc = new URL("http://www.world-of-cha0s.hostrocket.com/WiiRemoteJ/WiiRemoteJ%20v1.5.zip.gz");
                GZIPInputStream input = new GZIPInputStream(wiiRemLoc.openStream());
                String temploc = copyInputStreamToTmpFile(input, ".zip");
                ZipInputStream zipIn = new ZipInputStream(new FileInputStream(temploc));
                ZipEntry i = null;
                while ((i = zipIn.getNextEntry()) != null) {
                    if (i.getName().endsWith("WiiRemoteJ.jar")) {
                        File outjar = new File(jarpath);
                        BufferedOutputStream fs = new BufferedOutputStream(new FileOutputStream(outjar));
                        int bufferSize = 8192;
                        byte[] buffer = new byte[bufferSize];
                        int readSize = 0;
                        while ((readSize = zipIn.read(buffer, 0, bufferSize)) != -1) {
                            fs.write(buffer, 0, readSize);
                        }
                        fs.close();
                    }
                }
                System.out.println("unpacked into classpath.");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Boogiepants.main(argv);
    }
