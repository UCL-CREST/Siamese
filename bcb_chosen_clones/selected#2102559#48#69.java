    private Drawable fetchImage(String iconUrl, Context ctx) {
        URL url;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            if (PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("use.urlimg.com", true)) {
                iconUrl = iconUrl.substring(iconUrl.indexOf("//") + 2);
                iconUrl = "http://urlimg.com/width/100/" + iconUrl;
            }
            Log.d(ImageCache.class.getName(), "Loading image from: " + iconUrl);
            HttpGet httpGet = new HttpGet(iconUrl);
            HttpResponse response = httpClient.execute(httpGet);
            InputStream content = response.getEntity().getContent();
            Drawable d = Drawable.createFromStream(content, "src");
            content.close();
            httpGet.abort();
            return d;
        } catch (IOException e) {
            Log.e(ImageCache.class.getName(), "IOException while fetching: " + iconUrl);
            return TELKA;
        } finally {
        }
    }
