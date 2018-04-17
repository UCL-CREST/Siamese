    public static List<PluginInfo> getPluginInfos(String urlRepo) throws MalformedURLException, IOException {
        XStream xStream = new XStream();
        xStream.alias("plugin", PluginInfo.class);
        xStream.alias("plugins", List.class);
        List<PluginInfo> infos = null;
        URL url;
        BufferedReader in = null;
        StringBuilder buffer = new StringBuilder();
        try {
            url = new URL(urlRepo);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                buffer.append(inputLine);
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(RemotePluginsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        infos = (List<PluginInfo>) xStream.fromXML(buffer.toString());
        return infos;
    }
