    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            usage(System.out);
            System.exit(1);
        }
        final File tmpFile = File.createTempFile("sej", null);
        tmpFile.deleteOnExit();
        final FileOutputStream destination = new FileOutputStream(tmpFile);
        final String mainClass = args[1];
        final Collection jars = new LinkedList();
        for (int i = 2; i < args.length; i++) {
            String arg = args[i];
            jars.add(arg);
        }
        JarInterpretted interpretted = new JarInterpretted(destination);
        JarCat rowr = new JarCat(destination, createManifest(mainClass), jars);
        interpretted.write();
        rowr.write();
        destination.close();
        final File finalDestinationFile = new File(args[0]);
        final FileOutputStream finalDestination = new FileOutputStream(finalDestinationFile);
        IOUtils.copy(new FileInputStream(tmpFile), finalDestination);
        finalDestination.close();
        Chmod chmod = new Chmod("a+rx", new File[] { finalDestinationFile });
        chmod.invoke();
    }
