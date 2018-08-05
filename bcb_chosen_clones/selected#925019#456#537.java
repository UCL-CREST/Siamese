    public Vector recuperarRegistrosOficina(String usuario, int maxRegistros, int oficina, String any, String accion, int numero) throws java.rmi.RemoteException, Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        usuario = usuario.toUpperCase();
        Vector registrosVector = new Vector();
        DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
        DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fechaDocumento = null;
        java.util.Date fechaEESS = null;
        try {
            conn = ToolsBD.getConn();
            String texto = "";
            if (numero > 0) {
                if (accion.equals("R")) {
                    texto = " AND FZSNUMEN<?";
                } else {
                    texto = " AND FZSNUMEN>?";
                }
            } else {
                texto = "";
            }
            String sentenciaSql = "SELECT * FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " + "LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI " + "LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA " + "LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE " + "LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI " + "LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE " + "LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZSCAGCO " + "WHERE FZHCAUT=? AND FZSCAGCO=?" + (!any.trim().equals("") ? "AND FZSANOEN>=?" : "") + texto + "ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN " + (accion.equals("R") ? "DESC" : "ASC");
            ps = conn.prepareStatement(sentenciaSql);
            ps.setMaxRows(maxRegistros);
            ps.setString(1, usuario);
            ps.setString(2, "AS");
            ps.setInt(3, oficina);
            int contadorPS = 4;
            if (!any.trim().equals("")) {
                ps.setInt(contadorPS++, Integer.parseInt(any));
            }
            if (numero > 0) {
                ps.setInt(contadorPS, numero);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                RegistroSeleccionado registro = new RegistroSeleccionado();
                registro.setAnoEntrada(String.valueOf(rs.getInt("FZSANOEN")));
                registro.setNumeroEntrada(String.valueOf(rs.getInt("FZSNUMEN")));
                registro.setOficina(String.valueOf(rs.getInt("FZSCAGCO")));
                String textoOficina = rs.getString("FAADAGCO");
                if (textoOficina == null) {
                    textoOficina = " ";
                }
                registro.setDescripcionOficina(textoOficina);
                String fechaDocu = String.valueOf(rs.getInt("FZSFDOCU"));
                try {
                    fechaDocumento = yyyymmdd.parse(fechaDocu);
                    registro.setData(ddmmyyyy.format(fechaDocumento));
                } catch (Exception e) {
                    registro.setData(fechaDocu);
                }
                String fechaES = String.valueOf(rs.getInt("FZSFENTR"));
                try {
                    fechaEESS = yyyymmdd.parse(fechaES);
                    registro.setFechaES(ddmmyyyy.format(fechaEESS));
                } catch (Exception e) {
                    registro.setFechaES(fechaES);
                }
                if (rs.getString("FZGCENTI") == null) {
                    registro.setDescripcionRemitente(rs.getString("FZSREMIT"));
                } else {
                    registro.setDescripcionRemitente(rs.getString("FZGDENT2"));
                }
                registro.setDescripcionOrganismoDestinatario(rs.getString("FAXDORGR"));
                registro.setDescripcionDocumento(rs.getString("FZIDTIPE"));
                registro.setDescripcionIdiomaDocumento(rs.getString("FZMDIDI"));
                registro.setRegistroAnulado(rs.getString("FZSENULA"));
                registrosVector.addElement(registro);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ToolsBD.closeConn(conn, ps, rs);
        }
        if (accion.equals("R")) {
            Collections.sort(registrosVector);
        }
        return registrosVector;
    }
