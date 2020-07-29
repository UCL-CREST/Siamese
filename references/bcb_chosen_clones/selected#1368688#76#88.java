    public static String convertToString(Date date) {
        if (date == null) return null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        java.util.Date newDate = new java.util.Date();
        try {
            newDate = df.parse(date.toString());
        } catch (ParseException e) {
            System.out.println(e);
        }
        df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(newDate);
    }
