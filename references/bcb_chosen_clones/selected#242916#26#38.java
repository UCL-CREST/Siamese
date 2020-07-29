    public static String getDates(String startDate, int i, String inputFormat, String outputFormat) throws ParseException {
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
        Date inputDate = inputFormatter.parse(startDate);
        if (i == 1) {
            return outputFormatter.format(inputDate);
        } else {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(inputDate);
            calendar.add(Calendar.DATE, i - 1);
            return outputFormatter.format(calendar.getTime());
        }
    }
