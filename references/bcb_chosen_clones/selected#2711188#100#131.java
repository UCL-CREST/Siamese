    private void enumerateMatches(HttpPanel reqPanel, HttpPanel resPanel) {
        matches = new ArrayList<SearchMatch>();
        Pattern p = Pattern.compile(regEx, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher m;
        if (ExtensionSearch.Type.URL.equals(type)) {
            m = p.matcher(reqPanel.getTxtHeader().getText());
            if (m.find()) {
                matches.add(new SearchMatch(SearchMatch.Locations.REQUEST_HEAD, m.start(), m.end()));
            }
            return;
        }
        if (ExtensionSearch.Type.All.equals(type) || ExtensionSearch.Type.Request.equals(type)) {
            m = p.matcher(reqPanel.getTxtHeader().getText());
            while (m.find()) {
                matches.add(new SearchMatch(SearchMatch.Locations.REQUEST_HEAD, m.start(), m.end()));
            }
            m = p.matcher(reqPanel.getTxtBody().getText());
            while (m.find()) {
                matches.add(new SearchMatch(SearchMatch.Locations.REQUEST_BODY, m.start(), m.end()));
            }
        }
        if (ExtensionSearch.Type.All.equals(type) || ExtensionSearch.Type.Response.equals(type)) {
            m = p.matcher(resPanel.getTxtHeader().getText());
            while (m.find()) {
                matches.add(new SearchMatch(SearchMatch.Locations.RESPONSE_HEAD, m.start(), m.end()));
            }
            m = p.matcher(resPanel.getTxtBody().getText());
            while (m.find()) {
                matches.add(new SearchMatch(SearchMatch.Locations.RESPONSE_BODY, m.start(), m.end()));
            }
        }
    }
