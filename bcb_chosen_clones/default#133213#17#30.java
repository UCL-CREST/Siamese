    public boolean open() {
        try {
            URL url = new URL(resource);
            conn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (MalformedURLException e) {
            System.out.println("Uable to connect URL:" + resource);
            return false;
        } catch (IOException e) {
            System.out.println("IOExeption when connecting to URL" + resource);
            return false;
        }
        return true;
    }
