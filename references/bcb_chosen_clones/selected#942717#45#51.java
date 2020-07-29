    void downloadImage(String filename, File imageFile) throws Exception {
        String URL = Constants.IMAGE_URL + "/" + filename;
        SiteResponse response = stratSite.getResponse(URL);
        InputStream inputStream = response.getInputStream();
        OutputStream outputStream = new FileOutputStream(imageFile);
        IOUtils.copy(inputStream, outputStream);
    }
