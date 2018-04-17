    private String localToUTC(String sDate, TimeZone timezone) throws Exception {
        if (sDate == null || sDate.equals("")) {
            return sDate;
        }
        String utcPattern = "yyyyMMdd'T'HHmmss'Z'";
        DateFormat utcFormatter = new SimpleDateFormat(utcPattern);
        utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String noUtcPattern = "yyyyMMdd'T'HHmmss";
        DateFormat noUtcFormatter = new SimpleDateFormat(noUtcPattern);
        if (sDate.indexOf('Z') != -1) {
            return sDate;
        }
        Date date;
        if (timezone != null) {
            noUtcFormatter.setTimeZone(timezone);
        }
        date = noUtcFormatter.parse(sDate);
        return utcFormatter.format(date);
    }
