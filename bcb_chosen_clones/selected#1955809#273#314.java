    public IntactOntology parseOboFile(URL url, boolean keepTemporaryFile) throws PsiLoaderException {
        if (url == null) {
            throw new IllegalArgumentException("Please give a non null URL.");
        }
        StringBuffer buffer = new StringBuffer(1024 * 8);
        try {
            System.out.println("Loading URL: " + url);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()), 1024);
            String line;
            int lineCount = 0;
            while ((line = in.readLine()) != null) {
                lineCount++;
                buffer.append(line).append(NEW_LINE);
                if ((lineCount % 20) == 0) {
                    System.out.print(".");
                    System.out.flush();
                    if ((lineCount % 500) == 0) {
                        System.out.println("   " + lineCount);
                    }
                }
            }
            in.close();
            File tempDirectory = new File(System.getProperty("java.io.tmpdir", "tmp"));
            if (!tempDirectory.exists()) {
                if (!tempDirectory.mkdirs()) {
                    throw new IOException("Cannot create temp directory: " + tempDirectory.getAbsolutePath());
                }
            }
            System.out.println("Using temp directory: " + tempDirectory.getAbsolutePath());
            File tempFile = File.createTempFile("psimi.v25.", ".obo", tempDirectory);
            tempFile.deleteOnExit();
            tempFile.deleteOnExit();
            System.out.println("The OBO file is temporary store as: " + tempFile.getAbsolutePath());
            BufferedWriter out = new BufferedWriter(new FileWriter(tempFile), 1024);
            out.write(buffer.toString());
            out.flush();
            out.close();
            return parseOboFile(tempFile);
        } catch (IOException e) {
            throw new PsiLoaderException("Error while loading URL (" + url + ")", e);
        }
    }
