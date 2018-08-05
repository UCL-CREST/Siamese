    public Object convert(String id) throws ConverterException {
        Set dates = new TreeSet(new StringComparator(false, true));
        try {
            DateFormat dateFormatYMD = new SimpleDateFormat("yyyyMMdd");
            DateFormat dateFormatYM = new SimpleDateFormat("yyyyMM");
            Calendar cal = CalendarFactoryUtil.getCalendar();
            cal.setTime(dateFormatYMD.parse(_date));
            cal.set(Calendar.DATE, 1);
            Calendar now = CalendarFactoryUtil.getCalendar();
            String url = "http://www.reverendfun.com/artchives/?search=";
            while (cal.before(now)) {
                String text = Http.URLtoString(url + dateFormatYM.format(cal.getTime()));
                int x = text.indexOf("date=");
                int y = text.indexOf("\"", x);
                while (x != -1 && y != -1) {
                    String fromDateString = text.substring(x + 5, y);
                    dates.add(fromDateString);
                    x = text.indexOf("date=", y);
                    y = text.indexOf("\"", x);
                }
                cal.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            throw new ConverterException(_date + " " + e.toString());
        }
        return dates;
    }
