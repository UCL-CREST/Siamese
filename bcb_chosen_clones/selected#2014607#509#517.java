    private String addMonth(String yyyymm) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Timestamp ts = new Timestamp(sdf.parse(yyyymm).getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(ts);
        c.add(Calendar.MONTH, 1);
        SimpleDateFormat sdfStr = new SimpleDateFormat("yyyy-MM");
        return sdfStr.format(new Timestamp(c.getTime().getTime()));
    }
