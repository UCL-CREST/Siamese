    public String storeImage(InputStream inStream, String fileName, boolean resize) throws Exception {
        Calendar rightNow = Calendar.getInstance();
        String dayNamedFolderName = "" + rightNow.get(Calendar.YEAR) + StringUtil.getPaddedIntWithZeros(2, rightNow.get(Calendar.MONTH) + 1) + StringUtil.getPaddedIntWithZeros(2, rightNow.get(Calendar.DATE));
        String uploadDirRoot = props.getProperty("uploaded.files.root");
        File file = new File(uploadDirRoot + System.getProperty("file.separator") + dayNamedFolderName);
        if (!file.exists()) file.mkdirs();
        String extension = FilenameUtils.getExtension(fileName);
        String outFileName;
        if (Boolean.parseBoolean(props.getPropertiesInstance().getProperty(IFConsts.USEORIGINALFILENAME, "true"))) {
            outFileName = StringUtil.removeSpecChars(StringUtil.unaccent(FilenameUtils.getBaseName(fileName)));
        } else {
            outFileName = StringUtil.hash(fileName + Long.toString(System.currentTimeMillis()));
        }
        if (Boolean.parseBoolean(props.getPropertiesInstance().getProperty(IFConsts.USEEXTENSION, "true"))) {
            outFileName = outFileName + DOT + extension;
        }
        String outPathAndName = uploadDirRoot + System.getProperty("file.separator") + dayNamedFolderName + System.getProperty("file.separator") + props.getProperty("uploaded.files.prefix") + outFileName;
        File uploadedFile = new File(outPathAndName);
        _logger.info("uploadedFile.getAbsolutePath() = {}", uploadedFile.getAbsolutePath());
        uploadedFile.createNewFile();
        OutputStream outStream = new FileOutputStream(outPathAndName);
        IOUtils.copyLarge(inStream, outStream);
        IOUtils.closeQuietly(inStream);
        outStream.close();
        if (resize) {
            writeResizedImage(outPathAndName, extension, "imgSize_xs");
            writeResizedImage(outPathAndName, extension, "imgSize_s");
            writeResizedImage(outPathAndName, extension, "imgSize_m");
            writeResizedImage(outPathAndName, extension, "imgSize_l");
            writeResizedImage(outPathAndName, extension, "imgSize_xl");
        }
        String retVal = dayNamedFolderName + "/" + props.getProperty("uploaded.files.prefix") + outFileName;
        return retVal;
    }
