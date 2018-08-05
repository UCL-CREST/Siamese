    public String preprocess(String wikitext) {
        Pattern linkInALink = Pattern.compile("\\[\\[[^\\]]*(\\[\\[.*\\]\\]).*\\]\\]");
        Pattern innerlink = Pattern.compile("\\[\\[[^\\[]*?\\]\\]");
        Matcher m = linkInALink.matcher(wikitext);
        String processed = new String("");
        int mindex = 0;
        while (m.find()) {
            String toreplace = m.group();
            Matcher m2 = innerlink.matcher(toreplace);
            processed = processed + wikitext.substring(mindex, m.start());
            int currentindex = 0;
            String replaced = new String("");
            while (m2.find()) {
                String linktext = m2.group().substring(2, m2.group().length() - 2);
                String[] sp = linktext.split("\\|");
                if (sp.length == 2) {
                    linktext = sp[1];
                }
                replaced = replaced + toreplace.substring(currentindex, m2.start()) + linktext;
                currentindex = m2.end();
            }
            replaced = replaced + toreplace.substring(currentindex);
            processed = processed + replaced;
            mindex = m.end();
        }
        processed = processed + wikitext.substring(mindex);
        Pattern linksequence = Pattern.compile("(\\[\\[[^\\[\\]]*\\]\\][\r\n]*)+");
        Matcher fm = linksequence.matcher(wikitext.substring(mindex));
        int tail = 0;
        while (fm.find()) {
            if (fm.hitEnd()) {
                tail = fm.group().length();
            }
        }
        processed = processed.substring(0, processed.length() - tail);
        return processed;
    }
