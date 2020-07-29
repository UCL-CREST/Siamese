    public static List<String> getServers() throws Exception {
        List<String> servers = new ArrayList<String>();
        URL url = new URL("http://tfast.org/en/servers.php");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = null;
        while ((line = in.readLine()) != null) {
            if (line.contains("serv=")) {
                int i = line.indexOf("serv=");
                servers.add(line.substring(i + 5, line.indexOf("\"", i)));
            }
        }
        in.close();
        return servers;
    }
