    public static String getRandomUserAgent() {
        if (USER_AGENT_CACHE == null) {
            Collection<String> userAgentsCache = new ArrayList<String>();
            try {
                URL url = Tools.getResource(UserAgent.class.getClassLoader(), "user-agents-browser.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    userAgentsCache.add(str);
                }
                in.close();
                USER_AGENT_CACHE = userAgentsCache.toArray(new String[userAgentsCache.size()]);
            } catch (Exception e) {
                System.err.println("Can not read file; using default user-agent; error message: " + e.getMessage());
                return DEFAULT_USER_AGENT;
            }
        }
        return USER_AGENT_CACHE[new Random().nextInt(USER_AGENT_CACHE.length)];
    }
