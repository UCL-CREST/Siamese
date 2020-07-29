    private void analyzeString(MemTreeBuilder builder, String input, String pattern, String flags) throws XPathException {
        final Pattern ptn;
        if (flags != null) {
            int iFlags = parseStringFlags(flags);
            ptn = Pattern.compile(pattern, iFlags);
        } else {
            ptn = Pattern.compile(pattern);
        }
        Matcher matcher = ptn.matcher(input);
        int offset = 0;
        while (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            if (matchResult.start() != offset) {
                nonMatch(builder, input.substring(offset, matchResult.start()));
                offset = matchResult.start();
            }
            builder.startElement(new QName("match", Function.BUILTIN_FUNCTION_NS), null);
            for (int i = 1; i <= matchResult.groupCount(); i++) {
                if (matchResult.start(i) != offset) {
                    String chars = input.substring(offset, matchResult.start(i));
                    if (matchResult.start(i) >= matchResult.start() && matchResult.start(i) <= matchResult.end()) {
                        builder.characters(chars);
                    } else {
                        builder.endElement();
                        nonMatch(builder, chars);
                        builder.startElement(new QName("match", Function.BUILTIN_FUNCTION_NS), null);
                    }
                    offset = matchResult.start(i);
                }
                builder.startElement(new QName("group", Function.BUILTIN_FUNCTION_NS), null);
                builder.addAttribute(new QName("nr"), Integer.toString(i));
                builder.characters(input.substring(matchResult.start(i), matchResult.end(i)));
                builder.endElement();
                offset = matchResult.end(i);
            }
            if (offset != matchResult.end()) {
                String matchedInput = input.substring(offset, matchResult.end());
                builder.characters(matchedInput);
            }
            builder.endElement();
            offset = matchResult.end();
        }
        if (offset != input.length()) {
            nonMatch(builder, input.substring(offset));
        }
    }
