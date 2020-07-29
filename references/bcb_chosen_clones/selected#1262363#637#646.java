    private String convertDate(String textDate, String inputFormat, String outputFormat) {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat(inputFormat);
            SimpleDateFormat sdfOutput = new SimpleDateFormat(outputFormat);
            Date date = sdfInput.parse(textDate);
            return sdfOutput.format(date);
        } catch (ParseException pe) {
            return null;
        }
    }
