    private String markup(String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        Pattern p = Pattern.compile(PHOTOS_START_TAG + ".+?" + PHOTOS_END_TAG, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(content);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String textToMarkup = content.substring(start, end);
            textToMarkup = textToMarkup.substring(PHOTOS_START_TAG.length(), textToMarkup.length() - PHOTOS_END_TAG.length());
            StringBuffer buf = new StringBuffer();
            buf.append("<div class=\"photos\">\n");
            try {
                BufferedReader reader = new BufferedReader(new StringReader(textToMarkup));
                String line = reader.readLine();
                buf.append("<div>\n");
                boolean foundPhotos = false;
                while (line != null) {
                    if (line.trim().equals("")) {
                        if (foundPhotos) {
                            buf.append("</div>\n");
                            buf.append("<div>\n");
                        }
                    } else {
                        String[] tokens = line.split("\\|");
                        buf.append("<img src=\"");
                        buf.append(tokens[0]);
                        buf.append("\" class=\"photo\" alt=\"");
                        if (tokens.length == 2) {
                            buf.append(tokens[1]);
                        }
                        buf.append("\" />\n");
                        foundPhotos = true;
                    }
                    line = reader.readLine();
                }
                buf.append("</div>\n");
            } catch (IOException ioe) {
                log.warn(ioe);
            }
            buf.append("</div>");
            content = content.substring(0, start) + buf.toString() + content.substring(end, content.length());
            m = p.matcher(content);
        }
        return content;
    }
