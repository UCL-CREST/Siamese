    public static String convertDate(String date, String informat, String outformat) {
        if (Utils.isValueEmpty(date)) return null;
        try {
            SimpleDateFormat inf = new SimpleDateFormat(informat);
            SimpleDateFormat outf = new SimpleDateFormat(outformat);
            Date dateobj = inf.parse(date);
            return outf.format(dateobj);
        } catch (ParseException e) {
            throw new DB2TMInputException("Couldn't parse date: " + date, e);
        }
    }
