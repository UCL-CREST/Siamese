    private boolean generarBZVISAD(ParametrosRegistroModificado param, Session session, int fecha, int hora) throws HibernateException, ClassNotFoundException, Exception {
        boolean generado = false;
        SQLQuery q = null;
        String insertBZVISAD = "INSERT INTO BZVISAD (FZKANOEN, FZKCAGCO, FZKCENSA, FZKCENTF, FZKCENTI, FZKNENTF, FZKNENTI, " + "FZKREMIF, FZKREMII, FZKCONEF, FZKCONEI, FZKCUSVI, FZKFENTF, FZKFENTI, FZKFVISA, FZKHVISA,  FZKNUMEN, " + "FZKTEXTO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
            ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();
            registro.fijaUsuario(param.getUsuarioVisado());
            registro.setoficina(param.getOficina() + "");
            registro.setNumeroEntrada(param.getNumeroRegistro() + "");
            registro.setAnoEntrada(param.getAnoEntrada() + "");
            registro = regent.leer(registro);
            if (registro == null) {
                throw new Exception("S'ha produ\357t un error i no s'han pogut crear el objecte RegistroEntrada");
            }
            if (registro.getData().equals("0")) {
                param.setFechaDocumento(0);
            } else {
                java.util.Date fechaTest = null;
                DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
                DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
                fechaTest = ddmmyyyy.parse(registro.getData());
                param.setFechaDocumento(Integer.parseInt(yyyymmdd.format(fechaTest)));
            }
            if (registro.getDataEntrada().equals("0")) {
                param.setFechaRegistro(0);
            } else {
                java.util.Date fechaTest = null;
                DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
                DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
                fechaTest = ddmmyyyy.parse(registro.getDataEntrada());
                param.setFechaRegistro(Integer.parseInt(yyyymmdd.format(fechaTest)));
            }
            param.setTipoDocumento(registro.getTipo());
            param.setFzacagge(Integer.parseInt(registro.getBalears()));
            param.setIdiomaExtracto(registro.getIdioex());
            param.setFora(registro.getFora());
            param.setDestinatario(Integer.parseInt(registro.getDestinatari()));
            param.setComentario(registro.getComentario());
            param.setIdioma(registro.getIdioma());
            param.setAltres(registro.getAltres());
            param.setEntidad1Old(registro.getEntidad1());
            param.setEntidad2Old(Integer.parseInt(registro.getEntidad2()));
            q = session.createSQLQuery(insertBZVISAD);
            q.setInteger(0, param.getAnoEntrada());
            q.setInteger(1, param.getOficina());
            q.setString(2, TIPO_REGISTRO);
            q.setString(3, (param.isHayVisadoRemitente()) ? param.getEntidad1() : " ");
            q.setString(4, (param.isHayVisadoRemitente()) ? registro.getEntidad1Grabada() : " ");
            q.setInteger(5, (param.isHayVisadoRemitente()) ? param.getEntidad2() : 0);
            q.setInteger(6, (param.isHayVisadoRemitente()) ? Integer.parseInt(registro.getEntidad2()) : 0);
            q.setString(7, (param.isHayVisadoRemitente()) ? param.getRemitente() : " ");
            q.setString(8, (param.isHayVisadoRemitente()) ? registro.getAltres() : " ");
            q.setString(9, (param.isHayVisadoExtracto()) ? param.getExtracto() : " ");
            q.setString(10, (param.isHayVisadoExtracto()) ? registro.getComentario() : " ");
            q.setString(11, param.getUsuarioVisado());
            q.setInteger(12, 0);
            q.setInteger(13, 0);
            q.setInteger(14, fecha);
            q.setInteger(15, hora);
            q.setInteger(16, param.getNumeroRegistro());
            q.setString(17, param.getMotivo());
            q.executeUpdate();
            generado = true;
            log.debug("generarBZVISAD finalizado correctamente.");
        } catch (Exception e) {
            generado = false;
            log.error("Error: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("S'ha produ\357t un error insert BZVISAD");
        }
        return generado;
    }
