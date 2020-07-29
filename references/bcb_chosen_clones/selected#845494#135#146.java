    private String getDateFrom(String dateString) {
        String returnVal = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = dateFormat.parse(dateString);
            DateFormat second = SimpleDateFormat.getDateInstance(DateFormat.SHORT, LanguageTraslator.getLocale());
            returnVal = second.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnVal;
    }
