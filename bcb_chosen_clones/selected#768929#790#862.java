    public static String formatDate(String dateTime, String pattern) {
        final String yearSymbols = "Gy";
        final String monthSymbols = "M";
        final String daySymbols = "dDEFwW";
        TimeZone timeZone;
        String zone;
        if (dateTime.endsWith("Z") || dateTime.endsWith("z")) {
            timeZone = TimeZone.getTimeZone("GMT");
            dateTime = dateTime.substring(0, dateTime.length() - 1) + "GMT";
            zone = "z";
        } else if ((dateTime.length() >= 6) && (dateTime.charAt(dateTime.length() - 3) == ':') && ((dateTime.charAt(dateTime.length() - 6) == '+') || (dateTime.charAt(dateTime.length() - 6) == '-'))) {
            String offset = dateTime.substring(dateTime.length() - 6);
            if ("+00:00".equals(offset) || "-00:00".equals(offset)) {
                timeZone = TimeZone.getTimeZone("GMT");
            } else {
                timeZone = TimeZone.getTimeZone("GMT" + offset);
            }
            zone = "z";
            dateTime = dateTime.substring(0, dateTime.length() - 6) + "GMT" + offset;
        } else {
            timeZone = TimeZone.getDefault();
            zone = "";
        }
        String[] formats = { dt + zone, d, gym, gy };
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat(t + zone);
            inFormat.setLenient(false);
            Date d = inFormat.parse(dateTime);
            SimpleDateFormat outFormat = new SimpleDateFormat(strip(yearSymbols + monthSymbols + daySymbols, pattern));
            outFormat.setTimeZone(timeZone);
            return outFormat.format(d);
        } catch (ParseException pe) {
        }
        for (int i = 0; i < formats.length; i++) {
            try {
                SimpleDateFormat inFormat = new SimpleDateFormat(formats[i]);
                inFormat.setLenient(false);
                Date d = inFormat.parse(dateTime);
                SimpleDateFormat outFormat = new SimpleDateFormat(pattern);
                outFormat.setTimeZone(timeZone);
                return outFormat.format(d);
            } catch (ParseException pe) {
            }
        }
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat(gmd);
            inFormat.setLenient(false);
            Date d = inFormat.parse(dateTime);
            SimpleDateFormat outFormat = new SimpleDateFormat(strip(yearSymbols, pattern));
            outFormat.setTimeZone(timeZone);
            return outFormat.format(d);
        } catch (ParseException pe) {
        }
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat(gm);
            inFormat.setLenient(false);
            Date d = inFormat.parse(dateTime);
            SimpleDateFormat outFormat = new SimpleDateFormat(strip(yearSymbols, pattern));
            outFormat.setTimeZone(timeZone);
            return outFormat.format(d);
        } catch (ParseException pe) {
        }
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat(gd);
            inFormat.setLenient(false);
            Date d = inFormat.parse(dateTime);
            SimpleDateFormat outFormat = new SimpleDateFormat(strip(yearSymbols + monthSymbols, pattern));
            outFormat.setTimeZone(timeZone);
            return outFormat.format(d);
        } catch (ParseException pe) {
        }
        return EMPTY_STR;
    }
