    private static Pattern getBotPattern() {
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration<URL> urls = AbstractPustefixRequestHandler.class.getClassLoader().getResources(CONFIG);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                InputStream in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.startsWith("#") && !line.equals("")) {
                        if (sb.length() > 0) sb.append("|");
                        sb.append("(").append(line).append(")");
                    }
                }
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading bot user-agent configuration", e);
        }
        return Pattern.compile(sb.toString());
    }
