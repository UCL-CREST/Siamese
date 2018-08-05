    private static void loadEmoticons() {
        emoticons = new Hashtable();
        URL url = ChatPanel.class.getResource("/resources/text/emoticon.properties");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() == 0 || line.charAt(0) == '#') continue;
                int i0 = line.indexOf('=');
                if (i0 != -1) {
                    String key = line.substring(0, i0).trim();
                    String value = line.substring(i0 + 1).trim();
                    value = StringUtil.replaceString(value, "\\n", "\n");
                    URL eUrl = ChatPanel.class.getResource("/resources/emoticon/" + value);
                    if (eUrl != null) emoticons.put(key, new ImageIcon(eUrl));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
    }
