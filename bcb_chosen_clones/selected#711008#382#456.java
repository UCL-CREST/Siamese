    public List recuperarRegistros(String oficina, String usuario) {
        List registros = new ArrayList();
        Session session = getSession();
        ScrollableResults rs = null;
        SQLQuery q = null;
        String sentenciaHql = "";
        String fecha = "";
        java.util.Date fechaDocumento = null;
        DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
        DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
        if (oficina.equals("00")) {
            sentenciaHql = "SELECT FZJCAGCO, FZJNUMEN, FZJANOEN, FZJCENTI, FZJREMIT, FZJIREMI, " + " FZJCONEN, FZJIEXTR, FZJFMODI, FZJHMODI, FZJTEXTO FROM BZMODIF " + " WHERE (FZJIEXTR=' ' OR FZJIREMI=' ' OR FZJIEXTR='' OR FZJIREMI='') AND FZJFVISA=0 AND FZJCAGCO " + " IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=?) AND FZJCENSA='E' ORDER BY " + " FZJCAGCO, FZJANOEN, FZJNUMEN, FZJFMODI, FZJHMODI";
        } else {
            sentenciaHql = "SELECT FZJCAGCO, FZJNUMEN, FZJANOEN, FZJCENTI, FZJREMIT, FZJIREMI, " + " FZJCONEN, FZJIEXTR, FZJFMODI, FZJHMODI, FZJTEXTO FROM BZMODIF " + " WHERE FZJCAGCO=? AND FZJFVISA=0 AND (FZJIEXTR=' ' OR FZJIREMI=' ' OR FZJIEXTR='' OR FZJIREMI='') AND FZJCENSA='E' ORDER BY " + " FZJCAGCO, FZJANOEN, FZJNUMEN, FZJFMODI, FZJHMODI";
        }
        try {
            q = session.createSQLQuery(sentenciaHql);
            q.addScalar("FZJCAGCO", Hibernate.INTEGER);
            q.addScalar("FZJNUMEN", Hibernate.INTEGER);
            q.addScalar("FZJANOEN", Hibernate.INTEGER);
            q.addScalar("FZJCENTI", Hibernate.STRING);
            q.addScalar("FZJREMIT", Hibernate.STRING);
            q.addScalar("FZJIREMI", Hibernate.STRING);
            q.addScalar("FZJCONEN", Hibernate.STRING);
            q.addScalar("FZJIEXTR", Hibernate.STRING);
            ;
            q.addScalar("FZJFMODI", Hibernate.INTEGER);
            q.addScalar("FZJHMODI", Hibernate.INTEGER);
            q.addScalar("FZJTEXTO", Hibernate.STRING);
            if (oficina.equals("00")) {
                q.setString(0, usuario);
                q.setString(1, "VE");
            } else {
                q.setInteger(0, Integer.parseInt(oficina));
            }
            rs = q.scroll();
            while (rs.next()) {
                RegistroModificadoSeleccionado reg = new RegistroModificadoSeleccionado();
                reg.setNumeroOficina(rs.getInteger(0));
                reg.setNumeroRegistro(rs.getInteger(1));
                reg.setAnoRegistro(rs.getInteger(2));
                if ((!rs.getString(3).trim().equals("") || !rs.getString(4).trim().equals("")) && (rs.getString(5).equals(" ") || rs.getString(5).equals(""))) {
                    reg.setVisadoR("*");
                } else {
                    reg.setVisadoR("");
                }
                if (!rs.getString(6).trim().equals("") && (rs.getString(7).equals(" ") || rs.getString(7).equals(""))) {
                    reg.setVisadoC("*");
                } else {
                    reg.setVisadoC("");
                }
                fecha = String.valueOf(rs.getInteger(8));
                reg.setFechaModif(rs.getInteger(8));
                reg.setHoraModif(rs.getInteger(9));
                try {
                    fechaDocumento = yyyymmdd.parse(fecha);
                    reg.setFechaModificacion(ddmmyyyy.format(fechaDocumento));
                } catch (Exception e) {
                    reg.setFechaModificacion(fecha);
                }
                reg.setMotivoCambio(rs.getString(10));
                if (((rs.getString(5).equals(" ") || rs.getString(5).equals("")) && (!rs.getString(3).trim().equals("") || !rs.getString(4).trim().equals(""))) || ((rs.getString(7).equals(" ") || rs.getString(7).equals("")) && !rs.getString(6).trim().equals(""))) {
                    registros.add(reg);
                }
            }
            log.debug("recuperarRegistros ejecutado correctamente.");
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            close(session);
        }
        return registros;
    }
