    private String getPlayerName(String id) throws UnsupportedEncodingException, IOException {
        String result = "";
        Map<String, String> players = (Map<String, String>) sc.getAttribute("players");
        if (players.containsKey(id)) {
            result = players.get(id);
            System.out.println("skip name:" + result);
        } else {
            String palyerURL = "http://goal.2010worldcup.163.com/player/" + id + ".html";
            URL url = new URL(palyerURL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            String line = null;
            String nameFrom = "英文名:";
            String nameTo = "</dd>";
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(nameFrom) != -1) {
                    result = line.substring(line.indexOf(nameFrom) + nameFrom.length(), line.indexOf(nameTo));
                    break;
                }
            }
            reader.close();
            players.put(id, result);
        }
        return result;
    }
