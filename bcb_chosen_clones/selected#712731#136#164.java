    public static String datetimeToPath(String iso_date) {
        if (iso_date == null) return null;
        cat.debug("Path from " + iso_date);
        Date d = null;
        SimpleDateFormat target = null;
        try {
            List ds = new LinkedList();
            ds.add("yyyyMMdd'T'HHmmssZ");
            ds.add("yyyyMMdd'T'HHmmss z");
            ds.add("yyyyMMdd'T'HHmmss");
            Iterator i = ds.iterator();
            SimpleDateFormat inp;
            while ((d == null) && i.hasNext()) {
                try {
                    inp = new SimpleDateFormat((String) i.next());
                    d = inp.parse(iso_date);
                    cat.debug("Using date format " + inp);
                } catch (ParseException e) {
                }
            }
            assert (d != null) : "invalid date: " + iso_date;
            cat.debug("SportsML date-time: " + iso_date);
            StringBuilder fmt = new StringBuilder("yyyy").append(File.separator).append("MM").append(File.separator).append("dd");
            target = new SimpleDateFormat(fmt.toString());
        } catch (Exception e) {
            cat.error("Failed to parse " + iso_date + ": " + e.getClass().getName() + ": " + e.getMessage());
        }
        return (target != null) ? target.format(d) : null;
    }
