    public static String httpGetJson(final List<NameValuePair> nameValuePairs) {
        HttpClient httpclient = null;
        String data = "";
        URI uri = null;
        try {
            final String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            if (HTTPS) {
                final SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                final HttpParams params = new BasicHttpParams();
                final SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);
                httpclient = new DefaultHttpClient(mgr, params);
                uri = new URI(DEADDROPS_SERVER_URL_HTTPS + "?" + paramString);
            } else {
                httpclient = new DefaultHttpClient();
                uri = new URI(DEADDROPS_SERVER_URL + "?" + paramString);
            }
            final HttpGet request = new HttpGet();
            request.setURI(uri);
            final HttpResponse response = httpclient.execute(request);
            final BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) data += inputLine;
            in.close();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
            return null;
        } catch (final ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }
