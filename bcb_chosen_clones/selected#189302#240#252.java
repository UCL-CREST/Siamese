        private String convertDate(String date) {
            ParsePosition p1 = new ParsePosition(0);
            SimpleDateFormat dateFormat = null;
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date d1 = null;
            try {
                d1 = df.parse(date);
            } catch (Exception e) {
            }
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String date1 = dateFormat.format(d1);
            return date1;
        }
