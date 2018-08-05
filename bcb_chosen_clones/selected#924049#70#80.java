    private static void writeBinaryFile(String filename, String target) throws IOException {
        File outputFile = new File(target);
        AgentFilesystem.forceDir(outputFile.getParent());
        FileOutputStream output = new FileOutputStream(new File(target));
        FileInputStream inputStream = new FileInputStream(filename);
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer)) > -1) output.write(buffer, 0, bytesRead);
        inputStream.close();
        output.close();
    }
