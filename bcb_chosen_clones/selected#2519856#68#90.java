    private void doImageProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/" + type + "");
        Point imgSize = null;
        if (width > 0 || height > 0) {
            imgSize = new Point(width, height);
        }
        if (fmt != null && imageFormats.containsKey(fmt)) {
            imgSize = imageFormats.get(fmt);
        }
        InputStream imageInputStream = inputStream != null ? inputStream : imageUrl.openStream();
        if (imageInputStream == null) {
            throw new RuntimeException("File " + imageUrl + " does not exist!");
        }
        if (imgSize == null) {
            IOUtils.copy(imageInputStream, response.getOutputStream());
        } else {
            byte[] imageBytes = getImageBytes(type, imgSize, imageInputStream);
            response.setContentLength(imageBytes.length);
            response.getOutputStream().write(imageBytes);
        }
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
