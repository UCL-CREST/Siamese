    public Vector recuperar(String usuario, int sizePagina, int pagina) {
        Connection conn = null;
        ResultSet rs = null;
        ResultSet rsHist = null;
        PreparedStatement ps = null;
        PreparedStatement psHist = null;
        usuario = usuario.toUpperCase();
        Vector registrosSalidaVector = new Vector();
        DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
        DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fechaDocumento = null;
        java.util.Date fechaEESS = null;
        String fechaInicio = fechaDesde.substring(6, 10) + fechaDesde.substring(3, 5) + fechaDesde.substring(0, 2);
        String fechaFinal = fechaHasta.substring(6, 10) + fechaHasta.substring(3, 5) + fechaHasta.substring(0, 2);
        try {
            conn = ToolsBD.getConn();
            String sentenciaSql = "SELECT * FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " + "LEFT JOIN BZSALOFF ON FOSCAGCO=FZSCAGCO AND FOSANOEN=FZSANOEN AND FOSNUMEN=FZSNUMEN " + "LEFT JOIN BZOFIFIS ON FZOCAGCO=FOSCAGCO AND OFF_CODI=OFS_CODI " + "LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI " + "LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA " + "LEFT JOIN BHORGAN ON FHXCORGA=FZSCORGA " + "LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE " + "LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI " + "LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE " + "LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZSCAGCO AND FZHCAUT=? " + "WHERE " + "FZSCAGCO>=? AND FZSCAGCO<=? AND " + "FZSFENTR>=? AND FZSFENTR<=? " + (oficinaFisica == null || oficinaFisica.equals("") ? "" : " AND OFF_CODI = ? ") + (!extracto.trim().equals("") ? " AND (UPPER(FZSCONEN) LIKE ? OR UPPER(FZSCONE2) LIKE ?) " : "") + (!tipo.trim().equals("") ? " AND FZSCTIPE=? " : "") + (!destinatario.trim().equals("") ? " AND (UPPER(FZSREMIT) LIKE ? OR UPPER(FZGDENT2) LIKE ? ) " : "") + (!destino.trim().equals("") ? " AND (UPPER(FZSPROCE) LIKE ? OR UPPER(FABDAGGE) LIKE ? ) " : "") + (!remitente.trim().equals("") ? " AND UPPER(FHXDORGT) LIKE ? " : "") + " AND ( (FHXFALTA<=FZSFENTR AND ( (FHXFBAJA>= FZSFENTR AND FHXFBAJA !=0) OR FHXFBAJA = 0) ) ) " + (!CodiRemitent.trim().equals("") ? " AND FZSCORGA = ? " : "") + "ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN";
            ps = conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int contador = 1;
            ps.setString(contador++, usuario);
            ps.setString(contador++, "CS");
            ps.setInt(contador++, Integer.parseInt(oficinaDesde));
            ps.setInt(contador++, Integer.parseInt(oficinaHasta));
            ps.setInt(contador++, Integer.parseInt(fechaInicio));
            ps.setInt(contador++, Integer.parseInt(fechaFinal));
            if (oficinaFisica != null && !oficinaFisica.equals("")) {
                ps.setInt(contador++, Integer.parseInt(oficinaFisica));
            }
            if (!extracto.trim().equals("")) {
                ps.setString(contador++, "%" + extracto.toUpperCase() + "%");
                ps.setString(contador++, "%" + extracto.toUpperCase() + "%");
            }
            if (!tipo.trim().equals("")) {
                ps.setString(contador++, tipo);
            }
            if (!destinatario.trim().equals("")) {
                ps.setString(contador++, "%" + destinatario.toUpperCase() + "%");
                ps.setString(contador++, "%" + destinatario.toUpperCase() + "%");
            }
            if (!destino.trim().equals("")) {
                ps.setString(contador++, "%" + destino.toUpperCase() + "%");
                ps.setString(contador++, "%" + destino.toUpperCase() + "%");
            }
            if (!remitente.trim().equals("")) {
                ps.setString(contador++, "%" + remitente.toUpperCase() + "%");
            }
            if (!CodiRemitent.trim().equals("")) {
                ps.setString(contador++, CodiRemitent);
            }
            if (pagina <= 1 && isCalcularTotalRegistres()) {
                rs = ps.executeQuery();
                System.out.println("Nombre total de registres =" + rs.getFetchSize());
                rs.last();
                int rowcount = rs.getRow();
                rs.beforeFirst();
                System.out.println("Total rows for the query using Scrollable ResultSet: " + rowcount);
                this.totalFiles = String.valueOf(rowcount);
            }
            ps.setMaxRows((sizePagina * pagina) + 1);
            rs = ps.executeQuery();
            if (pagina > 1) {
                rs.next();
                rs.relative((sizePagina * (pagina - 1)) - 1);
            }
            while (rs.next()) {
                RegistroSeleccionado registroSalida = new RegistroSeleccionado();
                registroSalida.setAnoEntrada(String.valueOf(rs.getInt("FZSANOEN")));
                registroSalida.setNumeroEntrada(String.valueOf(rs.getInt("FZSNUMEN")));
                registroSalida.setOficina(String.valueOf(rs.getInt("FZSCAGCO")));
                String fechaES = String.valueOf(rs.getInt("FZSFENTR"));
                try {
                    fechaEESS = yyyymmdd.parse(fechaES);
                    registroSalida.setFechaES(ddmmyyyy.format(fechaEESS));
                } catch (Exception e) {
                    registroSalida.setFechaES(fechaES);
                }
                registroSalida.setOficinaFisica(rs.getString("OFF_CODI"));
                registroSalida.setDescripcionOficinaFisica(rs.getString("OFF_NOM"));
                String textoOficina = null;
                String sentenciaSqlHistOfi = "SELECT * FROM BHAGECO01 WHERE FHACAGCO=? AND FHAFALTA<=? " + "AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
                psHist = conn.prepareStatement(sentenciaSqlHistOfi);
                psHist.setString(1, String.valueOf(rs.getInt("FZSCAGCO")));
                psHist.setString(2, fechaES);
                psHist.setString(3, fechaES);
                rsHist = psHist.executeQuery();
                if (rsHist.next()) {
                    textoOficina = rsHist.getString("FHADAGCO");
                } else {
                    textoOficina = rs.getString("FAADAGCO");
                    if (textoOficina == null) {
                        textoOficina = " ";
                    }
                }
                if (rsHist != null) rsHist.close();
                if (psHist != null) psHist.close();
                registroSalida.setDescripcionOficina(textoOficina);
                String fechaDocu = String.valueOf(rs.getInt("FZSFDOCU"));
                try {
                    fechaDocumento = yyyymmdd.parse(fechaDocu);
                    registroSalida.setData(ddmmyyyy.format(fechaDocumento));
                } catch (Exception e) {
                    registroSalida.setData(fechaDocu);
                }
                if (rs.getString("FZGCENTI") == null) {
                    registroSalida.setDescripcionRemitente(rs.getString("FZSREMIT"));
                } else {
                    registroSalida.setDescripcionRemitente(rs.getString("FZGDENT2"));
                }
                if (rs.getString("FABDAGGE") == null) {
                    registroSalida.setDescripcionGeografico(rs.getString("FZSPROCE"));
                } else {
                    registroSalida.setDescripcionGeografico(rs.getString("FABDAGGE"));
                }
                String sentenciaSqlHistOrga = "SELECT * FROM BHORGAN01 WHERE FHXCORGA=? AND FHXFALTA<=? " + "AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
                psHist = conn.prepareStatement(sentenciaSqlHistOrga);
                psHist.setString(1, String.valueOf(rs.getInt("FZSCORGA")));
                psHist.setString(2, fechaES);
                psHist.setString(3, fechaES);
                rsHist = psHist.executeQuery();
                if (rsHist.next()) {
                    registroSalida.setDescripcionOrganismoDestinatario(rsHist.getString("FHXDORGT"));
                } else {
                    registroSalida.setDescripcionOrganismoDestinatario(rs.getString("FAXDORGT"));
                    if (registroSalida.getDescripcionOrganismoDestinatario() == null) {
                        registroSalida.setDescripcionOrganismoDestinatario(" ");
                    }
                }
                if (rsHist != null) rsHist.close();
                if (psHist != null) psHist.close();
                if (rs.getString("FZIDTIPE") == null) registroSalida.setDescripcionDocumento("&nbsp;"); else registroSalida.setDescripcionDocumento(rs.getString("FZIDTIPE"));
                registroSalida.setDescripcionIdiomaDocumento(rs.getString("FZMDIDI"));
                registroSalida.setRegistroAnulado(rs.getString("FZSENULA"));
                if (rs.getString("FZSCIDIO").equals("1")) {
                    registroSalida.setExtracto(rs.getString("FZSCONEN"));
                } else {
                    registroSalida.setExtracto(rs.getString("FZSCONE2"));
                }
                Date fechaSystem = new Date();
                DateFormat hhmmss = new SimpleDateFormat("HHmmss");
                DateFormat sss = new SimpleDateFormat("S");
                String ss = sss.format(fechaSystem);
                DateFormat aaaammdd = new SimpleDateFormat("yyyyMMdd");
                int fzafsis = Integer.parseInt(aaaammdd.format(fechaSystem));
                if (ss.length() > 2) {
                    ss = ss.substring(0, 2);
                }
                int fzahsis = Integer.parseInt(hhmmss.format(fechaSystem) + ss);
                String Stringsss = sss.format(fechaSystem);
                switch(Stringsss.length()) {
                    case (1):
                        Stringsss = "00" + Stringsss;
                        break;
                    case (2):
                        Stringsss = "0" + Stringsss;
                        break;
                }
                int horamili = Integer.parseInt(hhmmss.format(fechaSystem) + Stringsss);
                logLopdBZSALIDA("SELECT", usuario, fzafsis, horamili, rs.getInt("FZSNUMEN"), rs.getInt("FZSANOEN"), rs.getInt("FZSCAGCO"));
                registrosSalidaVector.addElement(registroSalida);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ToolsBD.closeConn(conn, ps, rs);
        }
        return registrosSalidaVector;
    }
