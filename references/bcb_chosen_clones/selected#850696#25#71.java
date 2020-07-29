    public Image storeImage(String title, String pathToImage, Map<String, Object> additionalProperties) {
        File collectionFolder = ProjectManager.getInstance().getFolder(PropertyHandler.getInstance().getProperty("_default_collection_name"));
        File imageFile = new File(pathToImage);
        String filename = "";
        String format = "";
        File copiedImageFile;
        while (true) {
            filename = "image" + UUID.randomUUID().hashCode();
            if (!DbEntryProvider.INSTANCE.idExists(filename)) {
                Path path = new Path(pathToImage);
                format = path.getFileExtension();
                copiedImageFile = new File(collectionFolder.getAbsolutePath() + File.separator + filename + "." + format);
                if (!copiedImageFile.exists()) break;
            }
        }
        try {
            copiedImageFile.createNewFile();
        } catch (IOException e1) {
            ExceptionHandlingService.INSTANCE.handleException(e1);
            return null;
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(imageFile), 4096);
            out = new BufferedOutputStream(new FileOutputStream(copiedImageFile), 4096);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return null;
        } catch (IOException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return null;
        }
        Image image = new ImageImpl();
        image.setId(filename);
        image.setFormat(format);
        image.setEntryDate(new Date());
        image.setTitle(title);
        image.setAdditionalProperties(additionalProperties);
        boolean success = DbEntryProvider.INSTANCE.storeNewImage(image);
        if (success) return image;
        return null;
    }
