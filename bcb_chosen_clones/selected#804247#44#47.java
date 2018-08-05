    public static String formatTime(String iso8601) throws ParseException {
        final Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(iso8601);
        return DateFormat.getDateTimeInstance().format(date);
    }
