    @Override
    public void execute() {
        File currentModelDirectory = new File(this.globalData.getData(String.class, GlobalData.MODEL_INSTALL_DIRECTORY));
        File mobileDirectory = new File(currentModelDirectory, "mobile");
        mobileDirectory.mkdir();
        File mobileModelFile = new File(mobileDirectory, "mobile_model.zip");
        FileOutputStream outputStream = null;
        ZipOutputStream zipStream = null;
        BusinessModel businessModel = BusinessUnit.getInstance().getBusinessModel();
        try {
            mobileModelFile.createNewFile();
            outputStream = new FileOutputStream(mobileModelFile);
            zipStream = new ZipOutputStream(outputStream);
            Dictionary dictionary = businessModel.getDictionary();
            for (Definition definition : dictionary.getAllDefinitions()) {
                ZipEntry entry = new ZipEntry(definition.getRelativeFileName());
                zipStream.putNextEntry(entry);
                writeBinary(definition.getAbsoluteFileName(), zipStream);
            }
            final String modelFilename = "model.xml";
            ZipEntry entry = new ZipEntry(modelFilename);
            zipStream.putNextEntry(entry);
            writeBinary(businessModel.getAbsoluteFilename(modelFilename), zipStream);
            final String logoFilename = dictionary.getModel().getImageSource(LogoType.mobile);
            entry = new ZipEntry(logoFilename);
            zipStream.putNextEntry(entry);
            writeBinary(businessModel.getAbsoluteFilename(logoFilename), zipStream);
        } catch (IOException e) {
            agentLogger.error(e);
        } finally {
            StreamHelper.close(zipStream);
            StreamHelper.close(outputStream);
        }
    }
