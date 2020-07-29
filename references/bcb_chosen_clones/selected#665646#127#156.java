    public String getMessageofTheDay(String id) {
        StringBuffer mod = new StringBuffer();
        int serverModId = 0;
        int clientModId = 0;
        BufferedReader input = null;
        try {
            URL url = new URL(FlyShareApp.BASE_WEBSITE_URL + "/mod.txt");
            input = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            inputLine = input.readLine();
            try {
                clientModId = Integer.parseInt(id);
                serverModId = Integer.parseInt(inputLine);
            } catch (NumberFormatException e) {
            }
            if (clientModId < serverModId || clientModId == 0) {
                mod.append(serverModId);
                mod.append('|');
                while ((inputLine = input.readLine()) != null) mod.append(inputLine);
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
        return mod.toString();
    }
