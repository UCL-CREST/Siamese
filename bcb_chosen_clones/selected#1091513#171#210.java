    public void doInsertImage() {
        logger.debug(">>> Inserting image...");
        logger.debug(" fullFileName : #0", uploadedFileName);
        String fileName = uploadedFileName.substring(uploadedFileName.lastIndexOf(File.separator) + 1);
        logger.debug(" fileName : #0", fileName);
        String newFileName = System.currentTimeMillis() + "_" + fileName;
        String filePath = ImageResource.getResourceDirectory() + File.separator + newFileName;
        logger.debug(" filePath : #0", filePath);
        try {
            File file = new File(filePath);
            file.createNewFile();
            FileChannel srcChannel = null;
            FileChannel dstChannel = null;
            try {
                srcChannel = new FileInputStream(uploadedFile).getChannel();
                dstChannel = new FileOutputStream(file).getChannel();
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            } finally {
                closeChannel(srcChannel);
                closeChannel(dstChannel);
            }
            StringBuilder imageTag = new StringBuilder();
            imageTag.append("<img src=\"");
            imageTag.append(getRequest().getContextPath());
            imageTag.append("/seam/resource");
            imageTag.append(ImageResource.RESOURCE_PATH);
            imageTag.append("/");
            imageTag.append(newFileName);
            imageTag.append("\"/>");
            if (getQuestionDefinition().getDescription() == null) {
                getQuestionDefinition().setDescription("");
            }
            getQuestionDefinition().setDescription(getQuestionDefinition().getDescription() + imageTag);
        } catch (IOException e) {
            logger.error("Error during saving image file", e);
        }
        uploadedFile = null;
        uploadedFileName = null;
        logger.debug("<<< Inserting image...Ok");
    }
