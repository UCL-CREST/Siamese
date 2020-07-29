    public static String convertTime(String informat, String outformat, Locale locale, String date) {
        SimpleDateFormat in_formatter = new SimpleDateFormat(informat, Locale.US);
        SimpleDateFormat out_formatter = new SimpleDateFormat(outformat, locale);
        GregorianCalendar now = new GregorianCalendar();
        Date time = null;
        String ret = null;
        try {
            time = in_formatter.parse(date);
            ret = out_formatter.format(time);
        } catch (ParseException ex) {
            ex.printStackTrace();
            ret = out_formatter.format(now.getTime());
        }
        return ret;
    }
