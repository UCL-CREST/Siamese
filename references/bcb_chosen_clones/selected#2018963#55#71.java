    private String wikify(String content, RenderEngine renderEngine, InitialRenderContext renderContext) {
        if (content == null || content.length() == 0) {
            return "";
        }
        Pattern p = Pattern.compile(WIKI_START_TAG + ".+?" + WIKI_END_TAG, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(content);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String textToWikify = content.substring(start, end);
            textToWikify = textToWikify.substring(WIKI_START_TAG.length(), textToWikify.length() - WIKI_END_TAG.length());
            textToWikify = renderEngine.render(textToWikify, renderContext);
            content = content.substring(0, start) + textToWikify + content.substring(end, content.length());
            m = p.matcher(content);
        }
        return content;
    }
