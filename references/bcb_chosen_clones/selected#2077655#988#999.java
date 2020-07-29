    private static String convertDate(String dateIn, String fromDateFormat, String toDateFormat) {
        String dateOut;
        try {
            SimpleDateFormat formatIn = new SimpleDateFormat(fromDateFormat);
            SimpleDateFormat formatOut = new SimpleDateFormat(toDateFormat);
            Date data = formatIn.parse(dateIn, new ParsePosition(0));
            dateOut = formatOut.format(data);
        } catch (Exception e) {
            dateOut = null;
        }
        return dateOut;
    }
