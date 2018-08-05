    private void parseTemplate(File templateFile, Map dataMap) throws ContainerException {
        Debug.log("Parsing template : " + templateFile.getAbsolutePath(), module);
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(templateFile));
        } catch (FileNotFoundException e) {
            throw new ContainerException(e);
        }
        String targetDirectoryName = args.length > 1 ? args[1] : null;
        if (targetDirectoryName == null) {
            targetDirectoryName = target;
        }
        String targetDirectory = ofbizHome + targetDirectoryName + args[0];
        File targetDir = new File(targetDirectory);
        if (!targetDir.exists()) {
            boolean created = targetDir.mkdirs();
            if (!created) {
                throw new ContainerException("Unable to create target directory - " + targetDirectory);
            }
        }
        if (!targetDirectory.endsWith("/")) {
            targetDirectory = targetDirectory + "/";
        }
        Writer writer = null;
        try {
            writer = new FileWriter(targetDirectory + templateFile.getName());
        } catch (IOException e) {
            throw new ContainerException(e);
        }
        try {
            FreeMarkerWorker.renderTemplate(templateFile.getAbsolutePath(), reader, dataMap, writer);
        } catch (Exception e) {
            throw new ContainerException(e);
        }
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new ContainerException(e);
        }
    }
