    private String processBrTags(String message, String contentType) {
        StringBuffer processedMessage;
        if (contentType != null && contentType.equals(HTML_CONTENT_TYPE)) {
            processedMessage = new StringBuffer();
            Pattern p = Pattern.compile("<\\s*[bB][rR](.*?)(/\\s*>)");
            Matcher m = p.matcher(message);
            int slash_index;
            int start = 0;
            while (m.find()) {
                processedMessage.append(message.substring(start, m.start()));
                slash_index = m.group().lastIndexOf("/");
                processedMessage.append(m.group().substring(0, slash_index));
                processedMessage.append(m.group().substring(slash_index + 1));
                start = m.end();
            }
            processedMessage.append(message.substring(start));
        } else {
            processedMessage = new StringBuffer(message);
        }
        return processedMessage.toString();
    }
