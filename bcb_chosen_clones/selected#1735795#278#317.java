    private String determineGuardedHtml() {
        StringBuffer buf = new StringBuffer();
        if (m_guardedButtonPresent) {
            buf.append("\n<span id='" + getHtmlIdPrefix() + PUSH_PAGE_SUFFIX + "' style='display:none'>\n");
            String location = m_guardedHtmlLocation != null ? m_guardedHtmlLocation : (String) Config.getProperty(Config.PROP_PRESENTATION_DEFAULT_GUARDED_HTML_LOCATION);
            String html = (String) c_guardedHtmlCache.get(location);
            if (html == null) {
                if (log.isDebugEnabled()) log.debug(this.NAME + ".determineGuardedHtml: Reading the Guarded Html Fragment: " + location);
                URL url = getUrl(location);
                if (url != null) {
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                        StringBuffer buf1 = new StringBuffer();
                        String line = null;
                        while ((line = in.readLine()) != null) {
                            buf1.append(line);
                            buf1.append('\n');
                        }
                        html = buf1.toString();
                    } catch (IOException e) {
                        log.warn(this.NAME + ".determineGuardedHtml: Failed to read the Guarded Html Fragment: " + location, e);
                    } finally {
                        try {
                            if (in != null) in.close();
                        } catch (IOException ex) {
                            log.warn(this.NAME + ".determineGuardedHtml: Failed to close the Guarded Html Fragment: " + location, ex);
                        }
                    }
                } else {
                    log.warn("Failed to read the Guarded Html Fragment: " + location);
                }
                if (html == null) html = "Transaction in Progress";
                c_guardedHtmlCache.put(location, html);
            }
            buf.append(html);
            buf.append("\n</span>\n");
        }
        return buf.toString();
    }
