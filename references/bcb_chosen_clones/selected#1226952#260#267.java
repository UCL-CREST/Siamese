    public static String convierteyyyymmddFechaAddmmyyyyFecha(String strFecha) throws java.text.ParseException {
        DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
        DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fechaDocumento = null;
        log.debug("Fecha a convertir: " + strFecha);
        fechaDocumento = yyyymmdd.parse(strFecha);
        return ddmmyyyy.format(fechaDocumento);
    }
