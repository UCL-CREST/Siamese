    public static void dateFormatTest() {
        String formatLong = "yyyy-MM-dd";
        String formatCn = "yyyy年MM月dd日";
        SimpleDateFormat sdfLong = new SimpleDateFormat(formatLong);
        SimpleDateFormat sdfCn = new SimpleDateFormat(formatCn);
        try {
            String dateStr = "2011-02-14";
            Date date = sdfLong.parse(dateStr);
            System.out.println(sdfCn.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
