    private void loadServers() {
        try {
            URL url = new URL(VirtualDeckConfig.SERVERS_URL);
            cmbServer.addItem("Local");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            if (in.readLine().equals("[list]")) {
                while ((str = in.readLine()) != null) {
                    String[] host_line = str.split(";");
                    Host h = new Host();
                    h.setIp(host_line[0]);
                    h.setPort(Integer.parseInt(host_line[1]));
                    h.setName(host_line[2]);
                    getServers().add(h);
                    cmbServer.addItem(h.getName());
                }
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
