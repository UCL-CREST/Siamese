    @Override
    public String execute() {
        String result = "";
        String sourceFormat = (String) oInfo.getParameters()[0];
        String targetFormat = (String) oInfo.getParameters()[1];
        String value = (String) oInfo.getParameters()[2];
        SimpleDateFormat sourceFormatter = new SimpleDateFormat(sourceFormat);
        SimpleDateFormat targetFormatter = new SimpleDateFormat(targetFormat);
        Date date = null;
        try {
            if (sourceFormat.equals("yyyy-MM-dd'T'HH:mm:ssz")) {
                Calendar calendar = javax.xml.bind.DatatypeConverter.parseDateTime(value);
                date = calendar.getTime();
            } else {
                date = sourceFormatter.parse(value);
            }
            result = targetFormatter.format(date);
        } catch (ParseException e) {
            result = value;
        }
        return result;
    }
