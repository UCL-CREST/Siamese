    public static String formatGivenDate(String inputFormat, String inputDate, String outputFormat) {
        if ((inputDate == null) || !(inputDate.length() > 0) || inputDate.startsWith("%")) return "";
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = null;
        try {
            currentTime_2 = formatter.parse(inputDate, pos);
        } catch (Exception ex) {
            lf.warning(inputDate + " format does not match " + inputFormat, "XAFunctoids", "formatGivenDate");
            return (inputDate + " format does not match " + inputFormat);
        }
        SimpleDateFormat formatter2 = null;
        try {
            formatter2 = new SimpleDateFormat(outputFormat);
        } catch (Exception ex1) {
            lf.warning(outputFormat + " does not confirm to date specifications ", "XAFunctoids", "formatGivenDate");
            return (outputFormat + " does not confirm to date specifications ");
        }
        String dateString = null;
        try {
            dateString = formatter2.format(currentTime_2);
        } catch (Exception ex2) {
            lf.severe(ex2.getMessage() + " XAFunctoids" + " formatGivenDate");
        }
        return dateString;
    }
