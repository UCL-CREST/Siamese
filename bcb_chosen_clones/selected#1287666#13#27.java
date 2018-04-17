    public static List<String> list() throws IOException {
        List<String> providers = new ArrayList<String>();
        Enumeration<URL> urls = ClassLoader.getSystemResources("sentrick.classifiers");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String provider = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((provider = in.readLine()) != null) {
                provider = provider.trim();
                if (provider.length() > 0) providers.add(provider);
            }
            in.close();
        }
        return providers;
    }
