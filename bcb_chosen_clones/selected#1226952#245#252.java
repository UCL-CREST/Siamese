    public static int convierteStringFechaAIntFecha(String strFecha) throws java.text.ParseException {
        DateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat date1 = new SimpleDateFormat("yyyyMMdd");
        Date fechaAux = null;
        log.debug("Fecha a convertir: " + strFecha);
        fechaAux = dateF.parse(strFecha);
        return Integer.parseInt(date1.format(fechaAux));
    }
