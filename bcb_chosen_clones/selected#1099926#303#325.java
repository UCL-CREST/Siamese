    protected static List<Pattern> getBotPatterns() {
        List<Pattern> patterns = new ArrayList<Pattern>();
        try {
            Enumeration<URL> urls = AbstractPustefixRequestHandler.class.getClassLoader().getResources("META-INF/org/pustefixframework/http/bot-user-agents.txt");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                InputStream in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.startsWith("#")) {
                        Pattern pattern = Pattern.compile(line);
                        patterns.add(pattern);
                    }
                }
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading bot user-agent configuration", e);
        }
        return patterns;
    }
