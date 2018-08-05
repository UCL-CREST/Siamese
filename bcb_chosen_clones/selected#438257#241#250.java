        public String toString() {
            try {
                DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dfIn.parse(dateKey);
                DateFormat dfOut = new SimpleDateFormat("d MMMM yyyy", new Locale("NL", "nl"));
                return dfOut.format(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
