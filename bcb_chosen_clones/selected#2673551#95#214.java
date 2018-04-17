    private void addAllTopics(Environment en, ZipOutputStream zipout, int progressStart, int progressLength) throws Exception, IOException {
        HashMap containingTopics = new HashMap();
        if (virtualWiki == null || virtualWiki.length() < 1) {
            virtualWiki = WikiBase.DEFAULT_VWIKI;
        }
        SearchEngine sedb = WikiBase.getInstance().getSearchEngineInstance();
        Collection all = sedb.getAllTopicNames(virtualWiki);
        String defaultTopic = en.getDefaultTopic();
        if (defaultTopic == null || defaultTopic.length() < 2) {
            defaultTopic = "StartingPoints";
        }
        Template tpl;
        logger.debug("Logging Wiki " + virtualWiki + " starting at " + defaultTopic);
        List ignoreTheseTopicsList = new ArrayList();
        ignoreTheseTopicsList.add("WikiSearch");
        ignoreTheseTopicsList.add("RecentChanges");
        ignoreTheseTopicsList.add("WikiSiteMap");
        ignoreTheseTopicsList.add("WikiSiteMapIE");
        ignoreTheseTopicsList.add("WikiSiteMapNS");
        Iterator allIterator = all.iterator();
        int count = 0;
        while (allIterator.hasNext()) {
            progress = Math.min(progressStart + (int) ((double) count * (double) progressLength / (double) all.size()), 99);
            count++;
            String topicname = (String) allIterator.next();
            try {
                addTopicToZip(en, zipout, containingTopics, sedb, defaultTopic, ignoreTheseTopicsList, topicname);
            } catch (Exception e) {
                logger.fatal("Exception while adding a topic ", e);
            }
        }
        logger.debug("Done adding all topics.");
        List sitemapLines = new ArrayList();
        Vector visitedPages = new Vector();
        List startingList = new ArrayList(1);
        startingList.add(SitemapThread.LAST_IN_LIST);
        parsePages(defaultTopic, containingTopics, startingList, "1", sitemapLines, visitedPages);
        StringBuffer ieView = new StringBuffer();
        StringBuffer nsView = new StringBuffer();
        Vector childNodes = new Vector();
        for (Iterator lineIterator = sitemapLines.iterator(); lineIterator.hasNext(); ) {
            SitemapLineBean line = (SitemapLineBean) lineIterator.next();
            if (childNodes.size() > 0) {
                String myGroup = line.getGroup();
                String lastNode = (String) childNodes.get(childNodes.size() - 1);
                while (myGroup.length() <= (lastNode.length() + 1) && childNodes.size() > 0) {
                    ieView.append("</div><!-- " + lastNode + "-->");
                    childNodes.remove(childNodes.size() - 1);
                    if (childNodes.size() > 0) {
                        lastNode = (String) childNodes.get(childNodes.size() - 1);
                    }
                }
            }
            ieView.append("<div id=\"node_" + line.getGroup() + "_Parent\" class=\"parent\">");
            for (Iterator levelsIterator = line.getLevels().iterator(); levelsIterator.hasNext(); ) {
                String level = (String) levelsIterator.next();
                if (line.isHasChildren()) {
                    if ("x".equalsIgnoreCase(level)) {
                        ieView.append("<a href=\"#\" onClick=\"expandIt('node_" + line.getGroup() + "_'); return false;\"><img src=\"images/x-.png\" widht=\"30\" height=\"30\" align=\"top\"  name=\"imEx\" border=\"0\"></a>");
                    } else if ("e".equalsIgnoreCase(level)) {
                        ieView.append("<a href=\"#\" onClick=\"expandItE('node_" + line.getGroup() + "_'); return false;\"><img src=\"images/e-.png\" widht=\"30\" height=\"30\" align=\"top\"  name=\"imEx\" border=\"0\"></a>");
                    } else {
                        ieView.append("<img src=\"images/" + level + ".png\" widht=\"30\" height=\"30\" align=\"top\">");
                    }
                } else {
                    ieView.append("<img src=\"images/" + level + ".png\" widht=\"30\" height=\"30\" align=\"top\">");
                }
            }
            ieView.append("<a href=\"" + safename(line.getTopic()) + ".html\">" + line.getTopic() + "</a></div>\n");
            if (line.isHasChildren()) {
                ieView.append("<div id=\"node_" + line.getGroup() + "_Child\" class=\"child\">");
                childNodes.add(line.getGroup());
            }
        }
        for (int i = childNodes.size() - 1; i >= 0; i--) {
            ieView.append("</div><!-- " + (String) childNodes.get(i) + "-->");
        }
        nsView.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n");
        for (Iterator lineIterator = sitemapLines.iterator(); lineIterator.hasNext(); ) {
            SitemapLineBean line = (SitemapLineBean) lineIterator.next();
            nsView.append("<tr><td height=\"30\" valign=\"top\">");
            for (Iterator levelsIterator = line.getLevels().iterator(); levelsIterator.hasNext(); ) {
                String level = (String) levelsIterator.next();
                nsView.append("<img src=\"images/" + level + ".png\" widht=\"30\" height=\"30\" align=\"top\">");
            }
            nsView.append("<a href=\"" + safename(line.getTopic()) + ".html\">" + line.getTopic() + "</a></td></tr>\n");
        }
        nsView.append("</table>\n");
        tpl = getTemplateFilledWithContent("sitemap");
        tpl.setFieldGlobal("TOPICNAME", "WikiSiteMap");
        ZipEntry entry = new ZipEntry("WikiSiteMap.html");
        StringReader strin = new StringReader(Utilities.replaceString(tpl.getContent(), "@@NSVIEW@@", nsView.toString()));
        zipout.putNextEntry(entry);
        int read;
        while ((read = strin.read()) != -1) {
            zipout.write(read);
        }
        zipout.closeEntry();
        zipout.flush();
        tpl = getTemplateFilledWithContent("sitemap_ie");
        tpl.setFieldGlobal("TOPICNAME", "WikiSiteMap");
        entry = new ZipEntry("WikiSiteMapIE.html");
        strin = new StringReader(Utilities.replaceString(tpl.getContent(), "@@IEVIEW@@", ieView.toString()));
        zipout.putNextEntry(entry);
        while ((read = strin.read()) != -1) {
            zipout.write(read);
        }
        zipout.closeEntry();
        zipout.flush();
        tpl = getTemplateFilledWithContent("sitemap_ns");
        tpl.setFieldGlobal("TOPICNAME", "WikiSiteMap");
        entry = new ZipEntry("WikiSiteMapNS.html");
        strin = new StringReader(Utilities.replaceString(tpl.getContent(), "@@NSVIEW@@", nsView.toString()));
        zipout.putNextEntry(entry);
        while ((read = strin.read()) != -1) {
            zipout.write(read);
        }
        zipout.closeEntry();
        zipout.flush();
    }
