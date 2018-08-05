    public static PointList parsePointString(String text, int boardSize) {
        String regex = "\\b([Pp][Aa][Ss][Ss]|[A-Ta-t](1\\d|[1-9]))\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        PointList list = new PointList(32);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            GoPoint point;
            try {
                point = parsePoint(text.substring(start, end), boardSize);
            } catch (GtpResponseFormatError e) {
                assert false;
                continue;
            }
            list.add(point);
        }
        return list;
    }
