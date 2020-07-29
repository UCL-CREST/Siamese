    private static void setMembers() {
        try {
            URL url = new URL(getTracUrl() + "newticket");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String buffer = reader.readLine();
            while (buffer != null) {
                if (buffer.contains("<select id=\"component\" name=\"component\">")) {
                    Pattern pattern = Pattern.compile(">[^<]+?<");
                    Matcher matcher = pattern.matcher(buffer);
                    Vector<String> erg = new Vector<String>();
                    int start = 0;
                    while (matcher.find(start)) {
                        int von = matcher.start() + 1;
                        int bis = matcher.end() - 1;
                        erg.add(Recoder.recode(buffer.substring(von, bis), "UTF-8", Recoder.getDefaultEncoding()));
                        start = bis;
                    }
                    m_strComponents = new String[erg.size()];
                    erg.toArray(m_strComponents);
                }
                if (buffer.contains("<select id=\"priority\" name=\"priority\">")) {
                    Pattern pattern = Pattern.compile(">[^<]+?<");
                    Matcher matcher = pattern.matcher(buffer);
                    Vector<String> erg = new Vector<String>();
                    int start = 0;
                    while (matcher.find(start)) {
                        int von = matcher.start() + 1;
                        int bis = matcher.end() - 1;
                        erg.add(Recoder.recode(buffer.substring(von, bis), "UTF-8", Recoder.getDefaultEncoding()));
                        start = bis;
                    }
                    m_strPriorities = new String[erg.size()];
                    erg.toArray(m_strPriorities);
                }
                buffer = reader.readLine();
            }
        } catch (MalformedURLException e) {
            System.out.println("e1");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
