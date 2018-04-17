    public static String markupStringReplacement(RenderRequest req, RenderResponse res, HttpServletRequest httpReq, HttpServletResponse httpRes, DefinableEntity entity, String inputString, String type, Long binderId, Long entryId) {
        String outputString = new String(inputString);
        outputString = outputString.replaceAll("%20", " ");
        outputString = outputString.replaceAll("%7B", "{");
        outputString = outputString.replaceAll("%7D", "}");
        int loopDetector;
        try {
            if (httpReq != null && binderId != null) {
                Pattern p1 = Pattern.compile("(\\{\\{attachmentUrl: ([^}]*)\\}\\})");
                Matcher m1 = p1.matcher(outputString);
                loopDetector = 0;
                while (m1.find()) {
                    if (loopDetector > 2000) {
                        logger.error("Error processing markup: " + inputString);
                        return outputString;
                    }
                    loopDetector++;
                    String url = m1.group(2);
                    String webUrl = WebUrlUtil.getServletRootURL(httpReq) + WebKeys.SERVLET_VIEW_FILE + "?";
                    if (entity != null) {
                        FileAttachment fa = entity.getFileAttachment(url.trim());
                        if (fa != null) {
                            webUrl += WebKeys.URL_FILE_ID + "=" + fa.getId().toString() + "&amp;";
                        } else {
                            webUrl += WebKeys.URL_FILE_TITLE + "=" + url.trim() + "&amp;";
                        }
                    } else {
                        webUrl += WebKeys.URL_FILE_TITLE + "=" + url.trim() + "&amp;";
                    }
                    webUrl += WebKeys.URL_FILE_VIEW_TYPE + "=" + WebKeys.FILE_VIEW_TYPE_ATTACHMENT_FILE + "&amp;";
                    webUrl += WebKeys.URL_BINDER_ID + "=" + binderId.toString() + "&amp;";
                    if (entryId != null) {
                        webUrl += WebKeys.URL_ENTRY_ID + "=" + entryId.toString() + "&amp;";
                    }
                    outputString = m1.replaceFirst(webUrl);
                }
            }
            if (type.equals(WebKeys.MARKUP_VIEW) || type.equals(WebKeys.MARKUP_FORM)) {
                Pattern p2 = Pattern.compile("(\\{\\{attachmentFileId: ([^}]*)\\}\\})");
                Matcher m2 = p2.matcher(outputString);
                loopDetector = 0;
                while (m2.find()) {
                    if (loopDetector > 2000) {
                        logger.error("Error processing markup: " + inputString);
                        return outputString;
                    }
                    loopDetector++;
                    String fileIds = m2.group(2).trim();
                    String webUrl = WebUrlUtil.getServletRootURL(httpReq) + WebKeys.SERVLET_VIEW_FILE + "?";
                    webUrl += WebKeys.URL_FILE_VIEW_TYPE + "=" + WebKeys.FILE_VIEW_TYPE_ATTACHMENT_FILE + "&amp;";
                    webUrl += fileIds;
                    outputString = m2.replaceFirst(webUrl);
                    m2 = p2.matcher(outputString);
                }
            }
            if (type.equals(WebKeys.MARKUP_VIEW)) {
                Pattern p2 = Pattern.compile("(\\{\\{titleUrl: ([^\\}]*)\\}\\})");
                Matcher m2 = p2.matcher(outputString);
                loopDetector = 0;
                while (m2.find()) {
                    if (loopDetector > 2000) {
                        logger.error("Error processing markup: " + inputString);
                        return outputString;
                    }
                    loopDetector++;
                    String urlParts = m2.group(2).trim();
                    String s_binderId = "";
                    Pattern p3 = Pattern.compile("binderId=([^ ]*)");
                    Matcher m3 = p3.matcher(urlParts);
                    if (m3.find() && m3.groupCount() >= 1) s_binderId = m3.group(1).trim();
                    String normalizedTitle = "";
                    Pattern p4 = Pattern.compile("title=([^ ]*)");
                    Matcher m4 = p4.matcher(urlParts);
                    if (m4.find() && m4.groupCount() >= 1) normalizedTitle = m4.group(1).trim();
                    String title = "";
                    Pattern p5 = Pattern.compile("text=(.*)$");
                    Matcher m5 = p5.matcher(urlParts);
                    if (m5.find() && m5.groupCount() >= 1) title = m5.group(1).trim();
                    String titleLink = "";
                    String action = WebKeys.ACTION_VIEW_FOLDER_ENTRY;
                    Map params = new HashMap();
                    params.put(WebKeys.URL_BINDER_ID, new String[] { s_binderId });
                    params.put(WebKeys.URL_NORMALIZED_TITLE, new String[] { normalizedTitle });
                    String webUrl = getPortletUrl(req, res, httpReq, httpRes, action, true, params);
                    titleLink = "<a href=\"" + webUrl + "\" ";
                    titleLink += "onClick=\"if (self.ss_openTitleUrl) return self.ss_openTitleUrl(this);\">";
                    titleLink += "<span class=\"ss_title_link\">";
                    titleLink += title + "</span></a>";
                    titleLink = titleLink.replaceAll("&", "&amp;");
                    outputString = outputString.substring(0, m2.start(0)) + titleLink + outputString.substring(m2.end(), outputString.length());
                    m2 = p2.matcher(outputString);
                }
            }
            if (binderId != null && (type.equals(WebKeys.MARKUP_VIEW) || type.equals(WebKeys.MARKUP_FILE))) {
                String action = WebKeys.ACTION_VIEW_FOLDER_ENTRY;
                Pattern p3 = Pattern.compile("(\\[\\[([^\\]]*)\\]\\])");
                Matcher m3 = p3.matcher(outputString);
                loopDetector = 0;
                while (m3.find()) {
                    if (loopDetector > 2000) {
                        logger.error("Error processing markup: " + inputString);
                        return outputString;
                    }
                    loopDetector++;
                    String title = m3.group(2).trim();
                    String normalizedTitle = getNormalizedTitle(title);
                    if (!normalizedTitle.equals("")) {
                        String titleLink = "";
                        if (type.equals(WebKeys.MARKUP_VIEW)) {
                            Map params = new HashMap();
                            params.put(WebKeys.URL_BINDER_ID, binderId.toString());
                            params.put(WebKeys.URL_NORMALIZED_TITLE, normalizedTitle);
                            String webUrl = getPortletUrl(req, res, httpReq, httpRes, action, true, params);
                            titleLink = "<a href=\"" + webUrl + "\" ";
                            titleLink += "onClick=\"if (self.ss_openTitleUrl) return self.ss_openTitleUrl(this);\">";
                            titleLink += "<span class=\"ss_title_link\">";
                            titleLink += title + "</span></a>";
                        } else {
                            titleLink = "{{titleUrl: " + WebKeys.URL_BINDER_ID + "=" + binderId.toString();
                            titleLink += " " + WebKeys.URL_NORMALIZED_TITLE + "=" + normalizedTitle;
                            titleLink += " text=" + title + "}}";
                        }
                        outputString = outputString.substring(0, m3.start(0)) + titleLink + outputString.substring(m3.end(), outputString.length());
                        m3 = p3.matcher(outputString);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error processing markup: " + inputString);
            return inputString;
        }
        return outputString;
    }
