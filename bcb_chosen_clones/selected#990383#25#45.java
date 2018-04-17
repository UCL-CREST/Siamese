    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("JarCrypt 0.1");
            System.out.println("");
            System.out.println("Usage: java -jar jarcrypt.jar [output jar] [input file]");
            System.exit(0);
        }
        outputFile = args[0];
        inputFile = args[1];
        ZipOutputStream myZip = new ZipOutputStream(new FileOutputStream(outputFile));
        myZip.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        OutputStreamWriter osw = new OutputStreamWriter(myZip);
        osw.write("Manifest-Version: 1.0\n");
        osw.write("Main-Class: org.ea.Extractor\n");
        osw.write("Created-By: Woden\n");
        osw.flush();
        myZip.closeEntry();
        writeJarFile(myZip);
        writeDataFile(myZip, inputFile);
        myZip.close();
    }
