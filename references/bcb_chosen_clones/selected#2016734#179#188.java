    public String miliSegundosEmMinSeq(long mili) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss");
        Date date = null;
        try {
            date = sdf.parse(String.valueOf(mili / 1000000));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new java.text.SimpleDateFormat("HH:mm:ss").format(date);
    }
