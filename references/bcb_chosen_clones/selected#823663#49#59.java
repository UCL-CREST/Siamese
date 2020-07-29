    public String getDayOfWeek(String dateStr) {
        SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatD = new SimpleDateFormat("E");
        Date d = null;
        try {
            d = formatYMD.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatD.format(d);
    }
