    public String wikify(String s, WikiPageHandler handler) {
        s = s.replace("\\\\[", "_BRACKETOPEN_");
        if (getReplaceNewlineWithP()) {
            s = s.replaceAll("\r\n\r\n", "\n<p>\n");
            s = s.replaceAll("\r\r", "\n<p>\n");
        }
        s = s.replaceAll("'''''([^']+)'''''", "<b><i>$1</i></b>");
        s = s.replaceAll("'''([^']+)'''", "<b>$1</b>");
        s = s.replaceAll("''([^']+)''", "<i>$1</i>");
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("\\[\\[([^\\]|]+)\\|?([^\\]]*)\\]\\]");
        matcher = pattern.matcher(s);
        while (matcher.find()) {
            String name = matcher.group(1);
            String label = matcher.group(2);
            int start = matcher.start(0);
            int end = matcher.end(0);
            String link;
            if (handler == null) {
                if (label.trim().length() == 0) {
                    label = name;
                }
                link = "<a href=\"" + name + "\">" + label + "</a>";
            } else {
                link = handler.getWikiLink(this, name, label);
            }
            s = s.substring(0, start) + link + s.substring(end);
            matcher = pattern.matcher(s);
        }
        int cnt = 0;
        pattern = Pattern.compile("\\[([^\\]]+)\\]");
        matcher = pattern.matcher(s);
        while (matcher.find()) {
            String name = matcher.group(1).trim();
            int idx = name.indexOf(" ");
            int start = matcher.start(0);
            int end = matcher.end(0);
            if (idx > 0) {
                String label = name.substring(idx);
                name = name.substring(0, idx);
                String ahref = "<a title=\"" + name + "\" class=\"wiki-link-external\" target=\"externalpage\" href=\"" + name + "\">";
                s = s.substring(0, start) + ahref + label + "</a>" + s.substring(end);
            } else {
                cnt++;
                String ahref = "<a title=\"" + name + "\" class=\"wiki-link-external\" target=\"externalpage\" href=\"" + name + "\">";
                s = s.substring(0, start) + ahref + "_BRACKETOPEN_" + cnt + "_BRACKETCLOSE_</a>" + s.substring(end);
            }
            matcher = pattern.matcher(s);
        }
        List headings = new ArrayList();
        pattern = Pattern.compile("(?m)^\\s*(==+)([^=]+)(==+)\\s*$");
        matcher = pattern.matcher(s);
        while (matcher.find()) {
            String prefix = matcher.group(1).trim();
            String label = matcher.group(2).trim();
            int start = matcher.start(0);
            int end = matcher.end(0);
            int level = prefix.length();
            String value;
            if (label.startsWith("{{")) {
                value = "<div class=\"wiki-h" + level + "\">" + label + "</div>";
            } else {
                value = "<a name=\"" + label + "\"></a><div class=\"wiki-h" + level + "\">" + label + "</div>";
                headings.add(new Object[] { new Integer(level), label });
            }
            s = s.substring(0, start) + value + s.substring(end);
            matcher = pattern.matcher(s);
        }
        int ulCnt = 0;
        int olCnt = 0;
        StringBuffer buff = new StringBuffer();
        for (String line : (List<String>) StringUtil.split(s, "\n", false, false)) {
            String tline = line.trim();
            if (tline.equals("----")) {
                buff.append("<hr>");
                buff.append("\n");
                continue;
            }
            int starCnt = 0;
            while (tline.startsWith("*")) {
                tline = tline.substring(1);
                starCnt++;
            }
            if (starCnt > 0) {
                if (starCnt > ulCnt) {
                    while (starCnt > ulCnt) {
                        buff.append("<ul>\n");
                        ulCnt++;
                    }
                } else {
                    while ((starCnt < ulCnt) && (ulCnt > 0)) {
                        buff.append("</ul>\n");
                        ulCnt--;
                    }
                }
                buff.append("<li> ");
                buff.append(tline);
                buff.append("</li> ");
                buff.append("\n");
                continue;
            }
            while (ulCnt > 0) {
                buff.append("</ul>\n");
                ulCnt--;
            }
            int hashCnt = 0;
            while (tline.startsWith("#")) {
                tline = tline.substring(1);
                hashCnt++;
            }
            if (hashCnt > 0) {
                if (hashCnt > olCnt) {
                    while (hashCnt > olCnt) {
                        buff.append("<ol>\n");
                        olCnt++;
                    }
                } else {
                    while ((hashCnt < olCnt) && (olCnt > 0)) {
                        buff.append("</ol>\n");
                        olCnt--;
                    }
                }
                buff.append("<li> ");
                buff.append(tline);
                buff.append("\n");
                continue;
            }
            while (olCnt > 0) {
                buff.append("</ol>\n");
                olCnt--;
            }
            buff.append(line);
            buff.append("\n");
        }
        while (ulCnt > 0) {
            buff.append("</ul>\n");
            ulCnt--;
        }
        while (olCnt > 0) {
            buff.append("</ol>\n");
            olCnt--;
        }
        s = buff.toString();
        StringBuffer sb = new StringBuffer();
        int baseIdx = 0;
        while (true) {
            int idx1 = s.indexOf("{{", baseIdx);
            if (idx1 < 0) {
                sb.append(s.substring(baseIdx));
                break;
            }
            int idx2 = s.indexOf("}}", idx1);
            if (idx2 <= idx1) {
                sb.append(s.substring(baseIdx));
                break;
            }
            sb.append(s.substring(baseIdx, idx1));
            String property = s.substring(idx1 + 2, idx2);
            baseIdx = idx2 + 2;
            if (property.equals("noheading")) {
                makeHeadings = false;
            } else {
                String value = null;
                if (handler != null) {
                    value = handler.getWikiPropertyValue(this, property);
                }
                if (value == null) {
                    value = "Unknown property:" + property;
                }
                sb.append(value);
            }
        }
        s = sb.toString();
        sb = new StringBuffer();
        while (true) {
            int idx1 = s.indexOf("<block");
            if (idx1 < 0) {
                break;
            }
            int idx2 = s.indexOf(">", idx1);
            if (idx2 < 0) {
                break;
            }
            int idx3 = s.indexOf("</block>", idx2);
            if (idx3 < 0) {
                break;
            }
            String first = s.substring(0, idx1);
            String attrs = s.substring(idx1 + 6, idx2);
            String inner = s.substring(idx2 + 1, idx3);
            Hashtable props = StringUtil.parseHtmlProperties(attrs);
            boolean open = Misc.getProperty(props, "open", true);
            String title = Misc.getProperty(props, "title", "");
            sb.append(first);
            sb.append(HtmlUtil.makeShowHideBlock(title, inner, open, HtmlUtil.cssClass("wiki-blockheader"), HtmlUtil.cssClass("wiki-block")));
            s = s.substring(idx3 + "</block>".length());
        }
        sb.append(s);
        s = sb.toString();
        s = s.replace("_BRACKETOPEN_", "[");
        s = s.replace("_BRACKETCLOSE_", "]");
        if (getMakeHeadings()) {
            if (headings.size() >= 2) {
                StringBuffer toc = new StringBuffer();
                makeHeadings(headings, toc, -1, "");
                String block = HtmlUtil.makeShowHideBlock("Contents", toc.toString(), true, HtmlUtil.cssClass("wiki-tocheader"), HtmlUtil.cssClass("wiki-toc"));
                floatBoxes.add(block);
                String blocks = "<table class=\"wiki-toc-wrapper\" align=\"right\" width=\"30%\"><tr><td>" + StringUtil.join("<br>", floatBoxes) + "</td></tr></table>";
                s = blocks + s;
            }
        }
        if (categoryLinks.size() > 0) {
            s = s + HtmlUtil.div("<b>Categories:</b> " + StringUtil.join("&nbsp;|&nbsp; ", categoryLinks), HtmlUtil.cssClass("wiki-categories"));
        }
        return s;
    }
