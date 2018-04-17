    public static String parseUTCDate(String utcDateString, String format) {
        if (utcDateString.indexOf(":") <= -1) {
            utcDateString += " 00:00:00";
        }
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'UTC+0800' yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = (Date) df.parse(utcDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(format).format(date);
    }
