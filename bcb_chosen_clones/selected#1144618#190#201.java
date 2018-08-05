    public String convertTime(String datum) {
        try {
            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date filmDate = sdfIn.parse(datum);
            SimpleDateFormat sdfOut;
            sdfOut = new SimpleDateFormat("HH:mm:ss");
            datum = sdfOut.format(filmDate);
        } catch (Exception ex) {
            daten.fehler.fehlerMeldung(ex, "MediatheBr.convertDatum");
        }
        return datum;
    }
