    public void getGenresFromWebsite() {
        try {
            URL shoutcast = new URL("http://classic.shoutcast.com");
            readGenresStream = shoutcast.openStream();
            bw = new BufferedReader(new InputStreamReader(readGenresStream));
            while (!stopSearching && (text = bw.readLine()) != null) {
                if (text.trim().startsWith("<td class=\"SearchBox\"")) {
                    while (!stopSearching && (text = bw.readLine()) != null) {
                        if (text.contains("<form action=")) {
                            Scanner f = new Scanner(text).useDelimiter("\\s*\"\\s*");
                            f.next();
                            subpage = f.next();
                            f.close();
                        }
                        if (text.contains("<OPTION VALUE=")) {
                            Scanner f = new Scanner(text).useDelimiter("\\s*\"\\s*");
                            f.next();
                            f.next();
                            String tmp = f.next();
                            if (!tmp.startsWith("> - ")) {
                                if (tmpGenres.capacity() > 0) addGenre.add(tmpGenres);
                                tmpGenres = new Vector<String>(0, 1);
                                tmpGenres.add(tmp.substring(1));
                            } else {
                                tmpGenres.add(tmp.substring(4));
                            }
                            f.close();
                        }
                        if (text.contains("</SELECT>")) {
                            stopSearching = true;
                        }
                    }
                }
            }
            if (tmpGenres.capacity() > 0) {
                addGenre.add(tmpGenres);
            }
            if (addGenre.get(0).get(0).equals("lass=")) {
                addGenre.remove(0);
                addGenre.trimToSize();
            }
            tmpGenres = new Vector<String>(0, 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopSearching = false;
            if (readGenresStream != null) {
                try {
                    readGenresStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
