    void testMatchComplete(Matcher matcher, String string) {
        try {
            harness.check(matcher.matches());
            harness.check(matcher.lookingAt());
            harness.check(!matcher.find());
            matcher.reset();
            harness.check(matcher.find());
            Pattern pattern = Pattern.compile("(" + matcher.pattern().pattern() + ")");
            matcher = pattern.matcher(string);
            harness.check(matcher.matches());
            harness.check(matcher.lookingAt());
            harness.check(!matcher.find());
            matcher.reset();
            harness.check(matcher.find());
            harness.check(matcher.start() == 0);
            harness.check(matcher.end() == string.length());
            harness.check(string, matcher.group());
            int groups = matcher.groupCount();
            harness.check(groups >= 1);
            harness.check(matcher.start(0) == 0);
            harness.check(matcher.start(1) == 0);
            harness.check(matcher.end(0) == string.length());
            harness.check(matcher.end(1) == string.length());
            harness.check(string, matcher.group(0));
            harness.check(string, matcher.group(1));
            harness.check("cat", matcher.replaceAll("cat"));
            harness.check("dog", matcher.replaceFirst("dog"));
            harness.check("cat" + string + "dog", matcher.replaceAll("cat$0dog"));
            harness.check("dog" + string + "cat", matcher.replaceFirst("dog$1cat"));
            matcher.reset();
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) matcher.appendReplacement(sb, "blue");
            matcher.appendTail(sb);
            harness.check("blue", sb.toString());
        } catch (PatternSyntaxException pse) {
            harness.debug(pse);
            harness.check(false);
        } catch (IllegalStateException ise) {
            harness.debug(ise);
            harness.check(false);
        }
    }
