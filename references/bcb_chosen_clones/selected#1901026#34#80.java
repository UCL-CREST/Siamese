    public void readPage(String search) {
        InputStream is = null;
        try {
            URL url = new URL("http://www.english-german-dictionary.com/index.php?search=" + search.trim());
            is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is, "ISO-8859-15");
            Scanner scan = new Scanner(isr);
            String str = new String();
            String translate = new String();
            String temp;
            while (scan.hasNextLine()) {
                temp = (scan.nextLine());
                if (temp.contains("<td style='padding-top:4px;' class='ergebnisse_res'>")) {
                    int anfang = temp.indexOf("-->") + 3;
                    temp = temp.substring(anfang);
                    temp = temp.substring(0, temp.indexOf("<!--"));
                    translate = temp.trim();
                } else if (temp.contains("<td style='' class='ergebnisse_art'>") || temp.contains("<td style='' class='ergebnisse_art_dif'>") || temp.contains("<td style='padding-top:4px;' class='ergebnisse_art'>")) {
                    if (searchEnglish == false && searchGerman == false) {
                        searchEnglish = temp.contains("<td style='' class='ergebnisse_art'>");
                        searchGerman = temp.contains("<td style='' class='ergebnisse_art_dif'>");
                    }
                    int anfang1 = temp.lastIndexOf("'>") + 2;
                    temp = temp.substring(anfang1, temp.lastIndexOf("</td>"));
                    String to = temp.trim() + " ";
                    temp = scan.nextLine();
                    int anfang2 = temp.lastIndexOf("\">") + 2;
                    temp = (to != null ? to : "") + temp.substring(anfang2, temp.lastIndexOf("</a>"));
                    str += translate + " - " + temp + "\n";
                    germanList.add(translate);
                    englishList.add(temp.trim());
                }
            }
            if (searchEnglish) {
                List<String> temp2 = englishList;
                englishList = germanList;
                germanList = temp2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException e) {
            }
        }
    }
