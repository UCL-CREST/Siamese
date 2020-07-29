    public String clearExcess(String document) {
        StringBuilder sb = new StringBuilder(document);
        final Pattern pattern = Pattern.compile("[!?]{2,}");
        final Matcher matcher = pattern.matcher(sb);
        int start = 0;
        while (matcher.find(start)) {
            sb.replace(matcher.start(), matcher.end(), matcher.group().contains("?") ? "?" : "!");
            start = matcher.start() + 1;
        }
        return sb.toString();
    }
