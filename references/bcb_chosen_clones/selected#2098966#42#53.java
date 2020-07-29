    public String getEffective() {
        SimpleDateFormat sdfIn = new SimpleDateFormat(this.sdfIn);
        SimpleDateFormat sdfOut = new SimpleDateFormat(this.sdfOut);
        GregorianCalendar loan = new GregorianCalendar();
        try {
            loan.setTimeInMillis(sdfIn.parse(value).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.effective = sdfOut.format(loan.getTime());
        return this.effective;
    }
