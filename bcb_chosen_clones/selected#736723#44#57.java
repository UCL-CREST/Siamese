    private String saveStringToFile(String fileContents, String fileName) {
        if (fileContents == null) return "";
        try {
            ZipOutputStream zipoutputstream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            zipoutputstream.putNextEntry(new ZipEntry("specification.xml"));
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(zipoutputstream);
            outputstreamwriter.write(fileContents);
            outputstreamwriter.close();
            zipoutputstream.close();
            return fileName;
        } catch (Exception exception) {
            return null;
        }
    }
