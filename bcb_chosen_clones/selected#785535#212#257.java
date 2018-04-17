    private void innerJob(String inFrom, String inTo, String line, Map<String, Match> result) throws UnsupportedEncodingException, IOException {
        String subline = line.substring(line.indexOf(inTo) + inTo.length());
        String tempStr = subline.substring(subline.indexOf(inFrom) + inFrom.length(), subline.indexOf(inTo));
        String inURL = "http://goal.2010worldcup.163.com/data/match/general/" + tempStr.substring(tempStr.indexOf("/") + 1) + ".xml";
        URL url = new URL(inURL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
        String inLine = null;
        String scoreFrom = "score=\"";
        String homeTo = "\" side=\"Home";
        String awayTo = "\" side=\"Away";
        String goalInclud = "Stat";
        String playerFrom = "playerId=\"";
        String playerTo = "\" position=";
        String timeFrom = "time=\"";
        String timeTo = "\" period";
        String teamFinish = "</Team>";
        boolean homeStart = false;
        boolean awayStart = false;
        while ((inLine = reader.readLine()) != null) {
            if (inLine.indexOf(teamFinish) != -1) {
                homeStart = false;
                awayStart = false;
            }
            if (inLine.indexOf(homeTo) != -1) {
                result.get(key).setHomeScore(inLine.substring(inLine.indexOf(scoreFrom) + scoreFrom.length(), inLine.indexOf(homeTo)));
                homeStart = true;
            }
            if (homeStart && inLine.indexOf(goalInclud) != -1) {
                MatchEvent me = new MatchEvent();
                me.setPlayerName(getPlayerName(inLine.substring(inLine.indexOf(playerFrom) + playerFrom.length(), inLine.indexOf(playerTo))));
                me.setTime(inLine.substring(inLine.indexOf(timeFrom) + timeFrom.length(), inLine.indexOf(timeTo)));
                result.get(key).getHomeEvents().add(me);
            }
            if (inLine.indexOf(awayTo) != -1) {
                result.get(key).setAwayScore(inLine.substring(inLine.indexOf(scoreFrom) + scoreFrom.length(), inLine.indexOf(awayTo)));
                awayStart = true;
            }
            if (awayStart && inLine.indexOf(goalInclud) != -1) {
                MatchEvent me = new MatchEvent();
                me.setPlayerName(getPlayerName(inLine.substring(inLine.indexOf(playerFrom) + playerFrom.length(), inLine.indexOf(playerTo))));
                me.setTime(inLine.substring(inLine.indexOf(timeFrom) + timeFrom.length(), inLine.indexOf(timeTo)));
                result.get(key).getAwayEvents().add(me);
            }
        }
        reader.close();
    }
