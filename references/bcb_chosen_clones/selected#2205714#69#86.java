    private void copyFile(String fileName, String messageID, boolean isError) {
        try {
            File inputFile = new File(fileName);
            File outputFile = null;
            if (isError) {
                outputFile = new File(provider.getErrorDataLocation(folderName) + messageID + ".xml");
            } else {
                outputFile = new File(provider.getDataProcessedLocation(folderName) + messageID + ".xml");
            }
            FileReader in = new FileReader(inputFile);
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (Exception e) {
        }
    }
