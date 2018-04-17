    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        ImagesService imgService = ImagesServiceFactory.getImagesService();
        InputStream stream = request.getInputStream();
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        int b = 0;
        while ((b = stream.read()) != -1) {
            bytes.add((byte) b);
        }
        byte img[] = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            img[i] = bytes.get(i);
        }
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String urlBlobstore = blobstoreService.createUploadUrl("/blobstore-servlet?action=upload");
        URL url = new URL("http://localhost:8888" + urlBlobstore);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=29772313");
        OutputStream out = connection.getOutputStream();
        out.write(img);
        out.flush();
        out.close();
        System.out.println(connection.getResponseCode());
        System.out.println(connection.getResponseMessage());
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String responseText = "";
        String line;
        while ((line = rd.readLine()) != null) {
            responseText += line;
        }
        out.close();
        rd.close();
        response.sendRedirect("/blobstore-servlet?action=getPhoto&" + responseText);
    }
