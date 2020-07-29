    private void copyResourceToDir(String ondexDir, String resource) {
        InputStream inputStream = OndexGraphImpl.class.getClassLoader().getResourceAsStream(resource);
        try {
            FileWriter fileWriter = new FileWriter(new File(ondexDir, resource));
            IOUtils.copy(inputStream, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            logger.error("Unable to copy '" + resource + "' file to " + ondexDir + "'");
        }
    }
