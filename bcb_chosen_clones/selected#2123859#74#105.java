    private static String fetchImageViaHttp(URL imgUrl) throws IOException {
        String sURL = imgUrl.toString();
        String imgFile = imgUrl.getPath();
        HttpURLConnection cnx = (HttpURLConnection) imgUrl.openConnection();
        String uri = null;
        try {
            cnx.setAllowUserInteraction(false);
            cnx.setDoOutput(true);
            cnx.addRequestProperty("Cache-Control", "no-cache");
            RequestContext ctx = RequestContext.get();
            if (ctx != null) cnx.addRequestProperty("User-Agent", ctx.header("user-agent")); else cnx.addRequestProperty("User-Agent", user_agent);
            cnx.addRequestProperty("Referer", sURL.substring(0, sURL.indexOf('/', sURL.indexOf('.')) + 1));
            cnx.connect();
            if (cnx.getResponseCode() != HttpURLConnection.HTTP_OK) return null;
            InputStream imgData = cnx.getInputStream();
            String ext = FilenameUtils.getExtension(imgFile).toLowerCase();
            if (!Multimedia.isImageFile("aa." + ext)) ext = "jpg";
            uri = FMT_FN.format(new Date()) + RandomStringUtils.randomAlphanumeric(4) + '.' + ext;
            File fileDest = new File(img_path + uri);
            if (!fileDest.getParentFile().exists()) fileDest.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(fileDest);
            try {
                IOUtils.copy(imgData, fos);
            } finally {
                IOUtils.closeQuietly(imgData);
                IOUtils.closeQuietly(fos);
            }
        } finally {
            cnx.disconnect();
        }
        return RequestContext.get().contextPath() + "/uploads/img/" + uri;
    }
