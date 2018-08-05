    public TVRageShowInfo(String xmlShowName) {
        String[] tmp, tmp2;
        String line = "";
        this.usrShowName = xmlShowName;
        try {
            URL url = new URL("http://www.tvrage.com/quickinfo.php?show=" + xmlShowName.replaceAll(" ", "%20"));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = in.readLine()) != null) {
                tmp = line.split("@");
                if (tmp[0].equals("Show Name")) showName = tmp[1];
                if (tmp[0].equals("Show URL")) showURL = tmp[1];
                if (tmp[0].equals("Latest Episode")) {
                    StringTokenizer st = new StringTokenizer(tmp[1], "^");
                    for (int i = 0; st.hasMoreTokens(); i++) {
                        if (i == 0) {
                            tmp2 = st.nextToken().split("x");
                            latestSeasonNum = tmp2[0];
                            latestEpisodeNum = tmp2[1];
                            if (latestSeasonNum.charAt(0) == '0') latestSeasonNum = latestSeasonNum.substring(1);
                        } else if (i == 1) latestTitle = st.nextToken().replaceAll("&", "and"); else latestAirDate = st.nextToken();
                    }
                }
                if (tmp[0].equals("Next Episode")) {
                    StringTokenizer st = new StringTokenizer(tmp[1], "^");
                    for (int i = 0; st.hasMoreTokens(); i++) {
                        if (i == 0) {
                            tmp2 = st.nextToken().split("x");
                            nextSeasonNum = tmp2[0];
                            nextEpisodeNum = tmp2[1];
                            if (nextSeasonNum.charAt(0) == '0') nextSeasonNum = nextSeasonNum.substring(1);
                        } else if (i == 1) nextTitle = st.nextToken().replaceAll("&", "and"); else nextAirDate = st.nextToken();
                    }
                }
                if (tmp[0].equals("Status")) status = tmp[1];
                if (tmp[0].equals("Airtime")) airTime = tmp[1];
            }
            in.close();
            url = new URL(showURL);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = in.readLine()) != null) {
                if (line.indexOf("<b>Latest Episode: </b>") > -1) {
                    tmp = line.split("'>");
                    if (tmp[2].indexOf(':') > -1) {
                        tmp = tmp[2].split(":");
                        latestSeriesNum = tmp[0];
                    }
                } else if (line.indexOf("<b>Next Episode: </b>") > -1) {
                    tmp = line.split("'>");
                    if (tmp[2].indexOf(':') > -1) {
                        tmp = tmp[2].split(":");
                        nextSeriesNum = tmp[0];
                    }
                }
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
