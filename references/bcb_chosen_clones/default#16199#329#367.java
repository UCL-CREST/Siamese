    public static String DateTimePerGivenFormat(String DateAndOrTime, String outputFormat) {
        if ((DateAndOrTime == null) || !(DateAndOrTime.length() > 0) || DateAndOrTime.startsWith("%")) return "";
        String desiredDate = null;
        String inputFormat = null;
        java.sql.Timestamp today = new java.sql.Timestamp(System.currentTimeMillis());
        if (DateAndOrTime.equals("Date")) {
            desiredDate = today.toString().substring(0, 10);
            inputFormat = "yyyy'-'MM'-'dd";
        } else if (DateAndOrTime.equals("Time")) {
            desiredDate = today.toString().substring(11, 19);
            inputFormat = "H:mm:ss";
        } else if (DateAndOrTime.equals("Timestamp")) {
            desiredDate = today.toString();
            inputFormat = "yyyy'-'MM'-'dd' 'H:mm:ss.SSS";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = null;
        try {
            currentTime_2 = formatter.parse(desiredDate, pos);
        } catch (Exception ex) {
            lf.warning(desiredDate + " format does not match " + inputFormat, "XAFunctoids", "DateTimePerGivenFormat");
            return (desiredDate + " format does not match " + inputFormat);
        }
        SimpleDateFormat formatter2 = null;
        try {
            formatter2 = new SimpleDateFormat(outputFormat);
        } catch (Exception ex1) {
            lf.warning(outputFormat + " does not confirm to date specifications ", "XAFunctoids", "DateTimePerGivenFormat");
            return (outputFormat + " does not confirm to date specifications ");
        }
        String dateString = null;
        try {
            dateString = formatter2.format(currentTime_2);
        } catch (Exception ex2) {
            lf.warning(ex2.getMessage() + " XAFunctoids" + " DateTimePerGivenFormat");
        }
        return dateString;
    }
