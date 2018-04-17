    static String appendRequestingPage(String content, String requestingPage) {
        requestingPage = UrlUtil.getPagePath(requestingPage);
        String pageReferrer = null;
        int index = requestingPage.lastIndexOf("/");
        if (index < requestingPage.length() - 1) {
            pageReferrer = requestingPage.substring(index + 1).trim().toLowerCase();
        }
        if (pageReferrer == null) {
            return content;
        }
        StringBuilder result = new StringBuilder();
        Pattern formPattern = Pattern.compile("\\s*</form>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = formPattern.matcher(content);
        int lastMatchEnd = 0;
        int matchStart = 0;
        while (matcher.find()) {
            matchStart = matcher.start();
            if (matchStart > lastMatchEnd) {
                result.append(content.substring(lastMatchEnd, matchStart));
            }
            result.append("\r\n<input type=\"hidden\" name=\"hula_page_referrer\" value=\"" + requestingPage + "\">");
            lastMatchEnd = matcher.end();
            result.append(content.substring(matchStart, lastMatchEnd));
        }
        result.append(content.substring(lastMatchEnd));
        content = result.toString();
        result = new StringBuilder();
        Pattern linkPattern = Pattern.compile("<(a|area)\\s[\\s\\S]*?href[\\s\\r\\n]*?=[\\s\\r\\n]*?\"?([\\w\\./:#=\\?]+)\"?[\\s\\S]*?>", Pattern.CASE_INSENSITIVE);
        matcher = linkPattern.matcher(content);
        matchStart = 0;
        lastMatchEnd = 0;
        while (matcher.find()) {
            matchStart = matcher.start();
            if (matchStart > lastMatchEnd) {
                result.append(content.substring(lastMatchEnd, matchStart));
            }
            lastMatchEnd = matcher.end();
            String linkTag = matcher.group();
            String linkUrl = matcher.group(2).trim();
            String linkResource = "";
            String linkQuery = "";
            int queryIndex = linkUrl.lastIndexOf("?");
            if (queryIndex > 0 && queryIndex < linkUrl.length() - 1) {
                linkResource = linkUrl.substring(0, queryIndex);
                linkQuery = linkUrl.substring(queryIndex + 1);
            } else {
                linkResource = linkUrl;
                linkQuery = "";
            }
            if (linkResource.endsWith("/") || linkResource.toLowerCase().endsWith(pageReferrer)) {
                if (linkQuery.length() > 0) {
                    linkQuery = "&" + linkQuery;
                }
                linkQuery = "hula_page_referrer=" + requestingPage + linkQuery;
            }
            if (linkQuery.length() > 0) {
                linkResource = linkResource + "?" + linkQuery;
            }
            linkTag = linkTag.replace(linkUrl, linkResource);
            result.append(linkTag);
        }
        result.append(content.substring(lastMatchEnd));
        return result.toString();
    }
