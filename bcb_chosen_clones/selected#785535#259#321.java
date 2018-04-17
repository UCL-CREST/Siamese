    private void innerJob(String inURL, String matchId, Map<String, Match> result) throws UnsupportedEncodingException, IOException {
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
                result.get(matchId).setHomeScore(inLine.substring(inLine.indexOf(scoreFrom) + scoreFrom.length(), inLine.indexOf(homeTo)));
                homeStart = true;
            }
            if (homeStart && inLine.indexOf(goalInclud) != -1) {
                MatchEvent me = new MatchEvent();
                me.setPlayerName(getPlayerName(inLine.substring(inLine.indexOf(playerFrom) + playerFrom.length(), inLine.indexOf(playerTo))));
                me.setTime(inLine.substring(inLine.indexOf(timeFrom) + timeFrom.length(), inLine.indexOf(timeTo)));
                List<MatchEvent> mes = result.get(matchId).getHomeEvents();
                boolean exist = false;
                for (MatchEvent _me : mes) {
                    if (_me.getPlayerName().equals(me.getPlayerName()) && _me.getTime().equals(me.getTime())) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    mes.add(me);
                }
            }
            if (inLine.indexOf(awayTo) != -1) {
                result.get(matchId).setAwayScore(inLine.substring(inLine.indexOf(scoreFrom) + scoreFrom.length(), inLine.indexOf(awayTo)));
                awayStart = true;
            }
            if (awayStart && inLine.indexOf(goalInclud) != -1) {
                MatchEvent me = new MatchEvent();
                me.setPlayerName(getPlayerName(inLine.substring(inLine.indexOf(playerFrom) + playerFrom.length(), inLine.indexOf(playerTo))));
                me.setTime(inLine.substring(inLine.indexOf(timeFrom) + timeFrom.length(), inLine.indexOf(timeTo)));
                List<MatchEvent> mes = result.get(matchId).getAwayEvents();
                boolean exist = false;
                for (MatchEvent _me : mes) {
                    if (_me.getPlayerName().equals(me.getPlayerName()) && _me.getTime().equals(me.getTime())) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    mes.add(me);
                }
            }
        }
        reader.close();
    }
