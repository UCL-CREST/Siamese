    public static XMLShowInfo NzbSearch(TVRageShowInfo tvrage, XMLShowInfo xmldata, int latestOrNext) {
        String newzbin_query = "", csvData = "", hellaQueueDir = "", newzbinUsr = "", newzbinPass = "";
        String[] tmp;
        DateFormat tvRageDateFormat = new SimpleDateFormat("MMM/dd/yyyy");
        DateFormat tvRageDateFormatFix = new SimpleDateFormat("yyyy-MM-dd");
        newzbin_query = "?q=" + xmldata.showName + "+";
        if (latestOrNext == 0) {
            if (xmldata.searchBy.equals("ShowName Season x Episode")) newzbin_query += tvrage.latestSeasonNum + "x" + tvrage.latestEpisodeNum; else if (xmldata.searchBy.equals("Showname SeriesNum")) newzbin_query += tvrage.latestSeriesNum; else if (xmldata.searchBy.equals("Showname YYYY-MM-DD")) {
                try {
                    Date airTime = tvRageDateFormat.parse(tvrage.latestAirDate);
                    newzbin_query += tvRageDateFormatFix.format(airTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (xmldata.searchBy.equals("Showname EpisodeTitle")) newzbin_query += tvrage.latestTitle;
        } else {
            if (xmldata.searchBy.equals("ShowName Season x Episode")) newzbin_query += tvrage.nextSeasonNum + "x" + tvrage.nextEpisodeNum; else if (xmldata.searchBy.equals("Showname SeriesNum")) newzbin_query += tvrage.nextSeriesNum; else if (xmldata.searchBy.equals("Showname YYYY-MM-DD")) {
                try {
                    Date airTime = tvRageDateFormat.parse(tvrage.nextAirDate);
                    newzbin_query += tvRageDateFormatFix.format(airTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (xmldata.searchBy.equals("Showname EpisodeTitle")) newzbin_query += tvrage.nextTitle;
        }
        newzbin_query += "&searchaction=Search";
        newzbin_query += "&fpn=p";
        newzbin_query += "&category=8category=11";
        newzbin_query += "&area=-1";
        newzbin_query += "&u_nfo_posts_only=0";
        newzbin_query += "&u_url_posts_only=0";
        newzbin_query += "&u_comment_posts_only=0";
        newzbin_query += "&u_v3_retention=1209600";
        newzbin_query += "&ps_rb_language=" + xmldata.language;
        newzbin_query += "&sort=ps_edit_date";
        newzbin_query += "&order=desc";
        newzbin_query += "&areadone=-1";
        newzbin_query += "&feed=csv";
        newzbin_query += "&ps_rb_video_format=" + xmldata.format;
        newzbin_query = newzbin_query.replaceAll(" ", "%20");
        System.out.println("http://v3.newzbin.com/search/" + newzbin_query);
        try {
            URL url = new URL("http://v3.newzbin.com/search/" + newzbin_query);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            csvData = in.readLine();
            if (csvData != null) {
                JavaNZB.searchCount++;
                if (searchCount == 6) {
                    searchCount = 0;
                    System.out.println("Sleeping for 60 seconds");
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tmp = csvData.split(",");
                tmp[2] = tmp[2].substring(1, tmp[2].length() - 1);
                tmp[3] = tmp[3].substring(1, tmp[3].length() - 1);
                Pattern p = Pattern.compile("[\\\\</:>?\\[|\\]\"]");
                Matcher matcher = p.matcher(tmp[3]);
                tmp[3] = matcher.replaceAll(" ");
                tmp[3] = tmp[3].replaceAll("&", "and");
                URLConnection urlConn;
                DataOutputStream printout;
                url = new URL("http://v3.newzbin.com/api/dnzb/");
                urlConn = url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                printout = new DataOutputStream(urlConn.getOutputStream());
                String content = "username=" + JavaNZB.newzbinUsr + "&password=" + JavaNZB.newzbinPass + "&reportid=" + tmp[2];
                printout.writeBytes(content);
                printout.flush();
                printout.close();
                BufferedReader nzbInput = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                File f = new File(JavaNZB.hellaQueueDir, tmp[3] + ".nzb");
                BufferedWriter out = new BufferedWriter(new FileWriter(f));
                String str;
                System.out.println("--Downloading " + tmp[3] + ".nzb" + " to queue directory--");
                while (null != ((str = nzbInput.readLine()))) out.write(str);
                nzbInput.close();
                out.close();
                if (latestOrNext == 0) {
                    xmldata.episode = tvrage.latestEpisodeNum;
                    xmldata.season = tvrage.latestSeasonNum;
                } else {
                    xmldata.episode = tvrage.nextEpisodeNum;
                    xmldata.season = tvrage.nextSeasonNum;
                }
            } else System.out.println("No new episode posted");
            System.out.println();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            System.out.println("IO Exception from NzbSearch");
        }
        return xmldata;
    }
