    public void uploadFile(File inputFile, String targetFile) throws IOException {
        System.out.println("Uploading " + inputFile.getName() + " to " + targetFile);
        File outputFile = new File(targetFile);
        if (targetFile.endsWith("/")) {
            outputFile = new File(outputFile, inputFile.getName());
        } else if (outputFile.getParentFile().exists() == false) {
            outputFile.getParentFile().mkdirs();
        }
        if (inputFile.renameTo(outputFile) == false) {
            InputStream in = new FileInputStream(inputFile);
            OutputStream out = new FileOutputStream(outputFile);
            byte[] line = new byte[16384];
            int bytes = -1;
            while ((bytes = in.read(line)) != -1) out.write(line, 0, bytes);
            in.close();
            out.close();
        }
    }
