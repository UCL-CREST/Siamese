    private void insertContent(ImageData imageData, Element element) {
        URL url = getClass().getResource(imageData.getURL());
        try {
            File imageFileRead = new File(url.toURI());
            FileInputStream inputStream = new FileInputStream(imageFileRead);
            String imageFileWritePath = "htmlReportFiles" + "/" + imageData.getURL();
            File imageFileWrite = new File(imageFileWritePath);
            String[] filePathTokens = imageFileWritePath.split("/");
            String directoryPathCreate = filePathTokens[0];
            int i = 1;
            while (i < filePathTokens.length - 1) {
                directoryPathCreate = directoryPathCreate + "/" + filePathTokens[i];
                i++;
            }
            File fileDirectoryPathCreate = new File(directoryPathCreate);
            if (!fileDirectoryPathCreate.exists()) {
                boolean successfulFileCreation = fileDirectoryPathCreate.mkdirs();
                if (successfulFileCreation == false) {
                    throw new ExplanationException("Unable to create folders in path " + directoryPathCreate);
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(imageFileWrite);
            byte[] data = new byte[1024];
            int readDataNumberOfBytes = 0;
            while (readDataNumberOfBytes != -1) {
                readDataNumberOfBytes = inputStream.read(data, 0, data.length);
                if (readDataNumberOfBytes != -1) {
                    fileOutputStream.write(data, 0, readDataNumberOfBytes);
                }
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception ex) {
            throw new ExplanationException(ex.getMessage());
        }
        String caption = imageData.getCaption();
        Element imageElement = element.addElement("img");
        if (imageData.getURL().charAt(0) != '/') imageElement.addAttribute("src", "htmlReportFiles" + "/" + imageData.getURL()); else imageElement.addAttribute("src", "htmlReportFiles" + imageData.getURL());
        imageElement.addAttribute("alt", "image not available");
        if (caption != null) {
            element.addElement("br");
            element.addText(caption);
        }
    }
