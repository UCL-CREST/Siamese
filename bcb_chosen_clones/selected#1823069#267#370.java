    private void addTopicToZip(ZipOutputStream zipout, HashMap containingTopics, SearchEngine sedb, String defaultTopic, List ignoreTheseTopicsList, String topicname) throws Exception, IOException {
        WikiBase wb = WikiBase.getInstance();
        Template tpl;
        tpl = new Template(this.getServletContext().getRealPath("/WEB-INF/classes/export2html/mastertemplate.tpl"));
        tpl.setFieldGlobal("VERSION", WikiBase.WIKI_VERSION);
        StringBuffer oneline = new StringBuffer();
        if (!ignoreTheseTopicsList.contains(topicname)) {
            oneline.append(topicname);
            tpl.setFieldGlobal("TOPICNAME", topicname);
            Topic topicObject = new Topic(topicname);
            logger.debug("Adding topic " + topicname);
            String author = null;
            java.util.Date lastRevisionDate = null;
            if (Environment.getBooleanValue(Environment.PROP_TOPIC_VERSIONING_ON)) {
                lastRevisionDate = topicObject.getMostRecentRevisionDate(virtualWiki);
                author = topicObject.getMostRecentAuthor(virtualWiki);
                if (author != null || lastRevisionDate != null) {
                    tpl.setField("SHOWVERSIONING1", "-->");
                    if (author != null) tpl.setField("AUTHOR", author);
                    if (lastRevisionDate != null) tpl.setField("MODIFYDATE", Utilities.formatDate(lastRevisionDate));
                    tpl.setField("SHOWVERSIONING2", "<!--");
                }
            }
            StringBuffer content = new StringBuffer();
            content.append(Topic.readCooked(virtualWiki, topicname, Environment.getValue(Environment.PROP_PARSER_FORMAT_LEXER), Environment.getValue(Environment.PROP_PARSER_LAYOUT_LEXER), "vqwiki.lex.HTMLLinkLex", true));
            String redirect = "redirect:";
            if (content.toString().startsWith(redirect)) {
                StringBuffer link = new StringBuffer(content.toString().substring(redirect.length()).trim());
                while (link.toString().indexOf("<") != -1) {
                    int startpos = link.toString().indexOf("<");
                    int endpos = link.toString().indexOf(">");
                    if (endpos == -1) {
                        endpos = link.length();
                    } else {
                        endpos++;
                    }
                    link.delete(startpos, endpos);
                }
                link = new StringBuffer(safename(link.toString().trim()));
                link = link.append(".html");
                String nl = System.getProperty("line.separator");
                tpl.setFieldGlobal("REDIRECT", "<script>" + nl + "location.replace(\"" + link.toString() + "\");" + nl + "</script>" + nl + "<meta http-equiv=\"refresh\" content=\"1; " + link.toString() + "\">" + nl);
            } else {
                tpl.setFieldGlobal("REDIRECT", "");
            }
            Collection searchresult = sedb.find(virtualWiki, topicname, false);
            if (searchresult != null && searchresult.size() > 0) {
                Iterator it = searchresult.iterator();
                String divider = "";
                StringBuffer backlinks = new StringBuffer();
                for (; it.hasNext(); ) {
                    SearchResultEntry result = (SearchResultEntry) it.next();
                    if (!result.getTopic().equals(topicname)) {
                        backlinks.append(divider);
                        backlinks.append("<a href=\"");
                        backlinks.append(safename(result.getTopic()));
                        backlinks.append(".html\">");
                        backlinks.append(result.getTopic());
                        backlinks.append("</a>");
                        divider = " | ";
                        List l = (List) containingTopics.get(result.getTopic());
                        if (l == null) {
                            l = new ArrayList();
                        }
                        if (!l.contains(topicname)) {
                            l.add(topicname);
                        }
                        containingTopics.put(result.getTopic(), l);
                    }
                }
                if (backlinks.length() > 0) {
                    ResourceBundle messages = ResourceBundle.getBundle("ApplicationResources", locale);
                    content.append("<br /><br /><span class=\"backlinks\">");
                    content.append(topicname);
                    content.append(" ");
                    content.append(messages.getString("topic.ismentionedon"));
                    content.append(" ");
                    content.append(backlinks.toString());
                    content.append("</span>");
                }
            }
            tpl.setFieldGlobal("CONTENTS", content.toString());
            ZipEntry entry = new ZipEntry(safename(topicname) + ".html");
            StringReader in = new StringReader(tpl.getContent());
            zipout.putNextEntry(entry);
            int read = 0;
            while ((read = in.read()) != -1) {
                zipout.write(read);
            }
            zipout.closeEntry();
            zipout.flush();
            if (topicname.equals(defaultTopic)) {
                entry = new ZipEntry("index.html");
                in = new StringReader(tpl.getContent());
                zipout.putNextEntry(entry);
                read = 0;
                while ((read = in.read()) != -1) {
                    zipout.write(read);
                }
                zipout.closeEntry();
                zipout.flush();
            }
        }
    }
