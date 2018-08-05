    public ArrayList loadData(String address) {
        URL url;
        BufferedReader bf_in = null;
        ArrayList a = new ArrayList();
        String line;
        try {
            url = new URL(address);
            bf_in = new BufferedReader(new InputStreamReader(url.openStream()));
            while (((line = bf_in.readLine()) != null)) {
                if (line.startsWith("Date")) {
                    continue;
                }
                if (line != null && line.length() > 0) {
                    a.add(line);
                }
            }
            bf_in.close();
        } catch (Exception e) {
            System.out.println("StockValumeHistory:loadData:Error:" + e);
        }
        return a;
    }
