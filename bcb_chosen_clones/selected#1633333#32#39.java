    private void getImage(String filename) throws MalformedURLException, IOException, SAXException, FileNotFoundException {
        String url = Constants.STRATEGICDOMINATION_URL + "/images/gameimages/" + filename;
        WebRequest req = new GetMethodWebRequest(url);
        SiteResponse response = getSiteResponse(req);
        File file = new File("etc/images/" + filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        IOUtils.copy(response.getInputStream(), outputStream);
    }
