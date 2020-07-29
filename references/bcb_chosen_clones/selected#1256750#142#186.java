    private String parseLineOrientedString(String code) {
        Queue<String> tags = new LinkedList<String>();
        Queue<String> delimiters = new LinkedList<String>();
        Pattern pattern = Pattern.compile(LINE_ORIENTED_STRING_TAG);
        String[] parts = code.split("<br/>\n", 2);
        String firstLine = parts[0];
        String body = parts[1];
        Matcher matcher = pattern.matcher(firstLine);
        while (matcher.find()) {
            String currentDelimiter = matcher.group(1);
            tags.add(matcher.group(2));
            delimiters.add(currentDelimiter);
            if (currentDelimiter.equals("`")) {
                this.output += "<span class=\"rubyexecution\">";
            } else {
                this.output += "<span class=\"rubystring\">";
            }
            this.output += matcher.group() + "</span>";
        }
        this.output += "<br/>\n";
        while (!tags.equals("")) {
            String currentTag = tags.poll();
            String currentDelimiter = delimiters.poll();
            Pattern endTagPattern = Pattern.compile("(?m)^" + currentTag + "((<br/>\n)|(\\Z))");
            Matcher endTagMatcher = endTagPattern.matcher(body);
            if (!endTagMatcher.find()) {
                break;
            }
            int endOfTag = endTagMatcher.start();
            String stringForCurrentTag = body.substring(0, endOfTag);
            if (currentDelimiter.equals("`")) {
                this.output += "<span class=\"rubyexecution\">";
            } else {
                this.output += "<span class=\"rubystring\">";
            }
            if (currentDelimiter.equals("'")) {
                this.output += stringForCurrentTag;
            } else {
                parseStringWithInterpolations(stringForCurrentTag);
            }
            this.output += currentTag + "</span><br/>\n";
            body = body.substring(endTagMatcher.end());
        }
        return body;
    }
