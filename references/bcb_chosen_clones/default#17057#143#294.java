    public void getStreamsPerGenre(String genre) {
        streams.removeAllElements();
        streams.trimToSize();
        try {
            URL shoutcast = new URL("http://classic.shoutcast.com" + subpage + "?sgenre=" + genre + "&numresult=100");
            readGenresStream = shoutcast.openStream();
            bw = new BufferedReader(new InputStreamReader(readGenresStream));
            String[] streamInfo = new String[8];
            while (!stopSearching && (text = bw.readLine()) != null) {
                if (text.contains("<b>" + numberOfStreams + "</b>")) {
                    if (numberOfStreams > 1) {
                        streams.add(streamInfo);
                        streamInfo = new String[8];
                    }
                    int x = streamInfo.length;
                    for (int fx = 0; fx < x; fx++) {
                        streamInfo[fx] = "";
                    }
                    numberOfStreams++;
                }
                try {
                    if (text.contains("/sbin/shoutcast-playlist")) {
                        Scanner f = new Scanner(text).useDelimiter("\\s*href=\"\\s*");
                        f.next();
                        String tmpA = f.next();
                        f = new Scanner(tmpA).useDelimiter("\\s*\"\\s*");
                        streamInfo[7] = f.next();
                        f.close();
                    }
                    if (text.contains("<b>[")) {
                        Scanner f = new Scanner(text).useDelimiter("\\s*<b>\\s*");
                        f.next();
                        String tmpA = f.next();
                        f = new Scanner(tmpA).useDelimiter("\\s*]\\s*");
                        streamInfo[2] = f.next().substring(1);
                        f.close();
                    }
                    if (text.contains("_scurl\"")) {
                        Scanner f = new Scanner(text).useDelimiter("\\s*href=\"\\s*");
                        f.next();
                        String tmpA = f.next();
                        f = new Scanner(tmpA).useDelimiter("\\s*\"\\s*");
                        streamInfo[1] = f.next();
                        f.close();
                        f = new Scanner(text).useDelimiter("\\s*<a\\s*");
                        f.next();
                        tmpA = f.next();
                        f = new Scanner(tmpA).useDelimiter("\\s*\">\\s*");
                        f.next();
                        tmpA = f.next();
                        f = new Scanner(tmpA).useDelimiter("\\s*</a\\s*");
                        streamInfo[0] = f.next();
                        f.close();
                    }
                    if (text.contains("Now Playing")) {
                        Scanner f = new Scanner(text).useDelimiter("\\s*</font>\\s*");
                        f.next();
                        streamInfo[3] = f.next();
                        f.close();
                        bw.readLine();
                        text = bw.readLine();
                        f = new Scanner(text).useDelimiter("\\s*font\\s*");
                        f.next();
                        String tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
                        streamInfo[4] = f.next();
                        bw.readLine();
                        text = bw.readLine();
                        f = new Scanner(text).useDelimiter("\\s*font\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
                        streamInfo[5] = f.next();
                        f.close();
                        bw.readLine();
                        bw.readLine();
                        bw.readLine();
                        bw.readLine();
                        text = bw.readLine();
                        f = new Scanner(text).useDelimiter("\\s*font\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
                        streamInfo[6] = f.next();
                        f.close();
                    }
                    if (text.trim().equals("</font></td>")) {
                        bw.readLine();
                        text = bw.readLine();
                        Scanner f = new Scanner(text).useDelimiter("\\s*font\\s*");
                        f.next();
                        String tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
                        streamInfo[4] = f.next();
                        bw.readLine();
                        text = bw.readLine();
                        f = new Scanner(text).useDelimiter("\\s*font\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
                        streamInfo[5] = f.next().trim();
                        f.close();
                        bw.readLine();
                        bw.readLine();
                        bw.readLine();
                        bw.readLine();
                        text = bw.readLine();
                        f = new Scanner(text).useDelimiter("\\s*font\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*\">\\s*");
                        f.next();
                        tmpB = f.next();
                        f = new Scanner(tmpB).useDelimiter("\\s*<\\s*");
                        streamInfo[6] = f.next();
                        f.close();
                    }
                } catch (NoSuchElementException f) {
                    System.out.println("Cant find everything the the html");
                }
            }
        } catch (Exception e) {
            System.out.println("HHHIIIIIIIIERRR");
            if (e.getMessage().startsWith("stream is closed")) {
                stopSearching = true;
            } else e.printStackTrace();
        } finally {
            stopSearching = false;
            numberOfStreams = 1;
            if (readGenresStream != null) {
                try {
                    readGenresStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
