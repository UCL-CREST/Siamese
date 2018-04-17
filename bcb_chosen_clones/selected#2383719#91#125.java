    public Font loadFontFromName(final String fontName) {
        if (fontName == null) {
            return Font.decode("Arial");
        }
        synchronized (fontCache) {
            if (fontCache.containsKey(fontName)) {
                return fontCache.get(fontName);
            }
            try {
                URL serverUrl = toServerFontURL(serverInfos.getServerAddress(), String.valueOf(serverInfos.getPort()), serverInfos.getPath(), URLEncoder.encode(fontName, "utf-8"));
                HttpParams params = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(params, 2000);
                DefaultHttpClient httpClient = new DefaultHttpClient(params);
                httpClient.setCookieStore(WabitClientSession.getCookieStore());
                httpClient.getCredentialsProvider().setCredentials(new AuthScope(serverUrl.getHost(), AuthScope.ANY_PORT), new UsernamePasswordCredentials(serverInfos.getUsername(), serverInfos.getPassword()));
                HttpUriRequest request = new HttpGet(serverUrl.toURI());
                ByteArrayInputStream bais = new ByteArrayInputStream(httpClient.execute(request, handler));
                Font font = null;
                try {
                    font = Font.createFont(Font.TRUETYPE_FONT, bais);
                } catch (Exception e) {
                    try {
                        font = Font.createFont(Font.TYPE1_FONT, bais);
                    } catch (Exception e2) {
                        throw new IOException(e2);
                    }
                }
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                fontCache.put(fontName, font);
                return font;
            } catch (Exception e) {
                throw new RuntimeException("Failed to load a font from the server.", e);
            }
        }
    }
