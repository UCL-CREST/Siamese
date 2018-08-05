    public void parse(byte[] segmentData) {
        String asciiText = new String(segmentData, 0, segmentData.length);
        Pattern pattern = Pattern.compile("\r\n([\\w#]+)=([\\w-#,\\. ]+)");
        Matcher matcher = pattern.matcher(asciiText);
        while (matcher.find()) {
            App12Tag tag = App12Tag.getTagByIdentifier(matcher.group(1));
            if (tag != null) {
                values.put(tag, Arrays.copyOfRange(segmentData, matcher.start(2), matcher.end(2)));
            } else {
                log.log(Level.WARNING, "Found unknown tag: " + matcher.group(1));
            }
        }
    }
