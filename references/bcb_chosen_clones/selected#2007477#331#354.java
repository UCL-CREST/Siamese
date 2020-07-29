    public static String setErrorServer(String newServer) {
        String old = errorServerURL;
        try {
            URL url = new URL(newServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder page = new StringBuilder();
            String line = null;
            while ((line = rd.readLine()) != null) {
                page.append(line);
            }
            rd.close();
            if (!page.toString().equals("maRla")) throw new ConfigurationException("URL given for error server is invalid", ConfigType.ErrorServer);
        } catch (UnknownHostException ex) {
            System.out.println("Accepting setting for error sever, unable to check");
        } catch (MalformedURLException ex) {
            throw new ConfigurationException("URL given for error server ('" + newServer + "') appears invalid", ConfigType.ErrorServer, ex);
        } catch (IOException ex) {
            throw new ConfigurationException("URL given for error server could not be reached", ConfigType.ErrorServer, ex);
        }
        errorServerURL = newServer;
        return old;
    }
