    public static void loadPlugins() {
        Logger.trace("Loading plugins");
        Enumeration<URL> urls = null;
        try {
            urls = Play.classloader.getResources("play.plugins");
        } catch (Exception e) {
        }
        while (urls != null && urls.hasMoreElements()) {
            URL url = urls.nextElement();
            Logger.trace("Found one plugins descriptor, %s", url);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] infos = line.split(":");
                    PlayPlugin plugin = (PlayPlugin) Play.classloader.loadClass(infos[1].trim()).newInstance();
                    Logger.trace("Loaded plugin %s", plugin);
                    plugin.index = Integer.parseInt(infos[0]);
                    plugins.add(plugin);
                }
            } catch (Exception ex) {
                Logger.error(ex, "Cannot load %s", url);
            }
        }
        Collections.sort(plugins);
        for (PlayPlugin plugin : new ArrayList<PlayPlugin>(plugins)) {
            plugin.onLoad();
        }
    }
