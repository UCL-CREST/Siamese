    private void sendToServer(String fichaID, String respostas) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, URISyntaxException {
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("xml", respostas));
        params.add(new BasicNameValuePair("idForm", fichaID));
        URI uri = URIUtils.createURI("http", "172.20.9.144", 8080, "/PSFServer/SaveAnswers", URLEncodedUtils.format(params, "UTF-8"), null);
        HttpPost request = new HttpPost(uri);
        request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse = client.execute(request);
        BufferedReader in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line + NL);
        }
        in.close();
        String resposta = sb.toString();
        if (resposta != null || resposta != "") {
            new DatabaseManager(this).getWritableDatabase().execSQL("delete from " + DatabaseManager.getTableDados());
        }
        backToMain();
    }
