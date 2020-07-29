    @Test
    public void unacceptableMimeTypeTest() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://localhost:8080/alfresco/sword/deposit/company_home");
        File file = new File("/Library/Application Support/Apple/iChat Icons/Planets/Mars.gif");
        FileEntity entity = new FileEntity(file, "text/xml");
        entity.setChunked(true);
        httppost.setEntity(entity);
        Date date = new Date();
        Long time = date.getTime();
        httppost.addHeader("content-disposition", "filename=x" + time + "x.gif");
        System.out.println("Executing request...." + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            InputStream is = resEntity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) System.out.println(line);
            }
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
        httpclient.getConnectionManager().shutdown();
    }
