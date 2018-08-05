    public static void main(String args[]) {
        DateFormat Dformat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = null;
        String str = "1988_08_08";
        try {
            date = Dformat.parse(str);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        System.out.println("The original format of Date is: " + date);
        System.out.println("The date parsed from str is: " + date);
        str = Dformat.format(date);
        System.out.println("The str Parsed from date is: " + str);
        Dformat = DateFormat.getDateInstance(DateFormat.SHORT);
        str = Dformat.format(date);
        System.out.println("Using SHORT PARSE, the str Parsed from date is: " + str);
        Dformat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        str = Dformat.format(date);
        System.out.println("Using MEDIUM PARSE, the str Parsed from date is: " + str);
        Dformat = DateFormat.getDateInstance(DateFormat.LONG);
        str = Dformat.format(date);
        System.out.println("Using LONG PARSE, the str Parsed from date is: " + str);
        Dformat = DateFormat.getDateInstance(DateFormat.FULL);
        str = Dformat.format(date);
        System.out.println("Using FULL PARSE, the str Parsed from date is: " + str);
        DateFormat format1 = new SimpleDateFormat("yyyy��MM��dd��HHʱmm��ss��");
        date = new Date();
        str = format1.format(date);
        System.out.println(str);
        DateFormat format2 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        System.out.println(format2.format(date));
    }
