        private String processValue(String pre) {
            StringBuilder post = new StringBuilder();
            Pattern pat = Pattern.compile("(" + PATTERN_KEY + "|" + TOKEN_KEY + ")");
            Matcher matcher = pat.matcher(pre);
            int from = 0;
            while (matcher.find()) {
                post.append(pre.substring(from, matcher.start()));
                String s = matcher.group(1);
                if (s.matches(PATTERN_KEY)) {
                    post.append("\"" + lookupPattern(s) + "\"");
                } else {
                    post.append("\"" + lookupToken(s) + "\"");
                }
                from = matcher.end();
            }
            post.append(pre.substring(from));
            return catAndRemoveQuotes(post.toString());
        }
