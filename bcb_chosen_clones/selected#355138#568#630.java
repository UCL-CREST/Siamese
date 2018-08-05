    public void leer() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        leidos = false;
        DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
        DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fechaDocumento = null;
        try {
            conn = ToolsBD.getConn();
            String sentenciaSql = "SELECT * FROM BZOFREM " + "WHERE REM_OFANY=? AND REM_OFOFI=? AND REM_OFNUM=?";
            ps = conn.prepareStatement(sentenciaSql);
            ps.setInt(1, Integer.parseInt(anoOficio));
            ps.setInt(2, Integer.parseInt(oficinaOficio));
            ps.setInt(3, Integer.parseInt(numeroOficio));
            rs = ps.executeQuery();
            if (rs.next()) {
                leidos = true;
                anoOficio = String.valueOf(rs.getInt("REM_OFANY"));
                numeroOficio = String.valueOf(rs.getInt("REM_OFNUM"));
                oficinaOficio = String.valueOf(rs.getInt("REM_OFOFI"));
                String fechaO = String.valueOf(rs.getInt("REM_OFFEC"));
                try {
                    fechaDocumento = yyyymmdd.parse(fechaO);
                    fechaOficio = (ddmmyyyy.format(fechaDocumento));
                } catch (Exception e) {
                    fechaOficio = fechaO;
                }
                descripcion = rs.getString("REM_CONT");
                anoSalida = String.valueOf(rs.getInt("REM_SALANY"));
                numeroSalida = String.valueOf(rs.getInt("REM_SALNUM"));
                oficinaSalida = String.valueOf(rs.getInt("REM_SALOFI"));
                nulo = rs.getString("REM_NULA");
                motivosNulo = rs.getString("REM_NULMTD");
                usuarioNulo = rs.getString("REM_NULUSU");
                String fechaN = String.valueOf(rs.getInt("REM_NULFEC"));
                try {
                    fechaDocumento = yyyymmdd.parse(fechaN);
                    fechaNulo = (ddmmyyyy.format(fechaDocumento));
                } catch (Exception e) {
                    fechaNulo = fechaN;
                }
                anoEntrada = String.valueOf(rs.getInt("REM_ENTANY"));
                numeroEntrada = String.valueOf(rs.getInt("REM_ENTNUM"));
                oficinaEntrada = String.valueOf(rs.getInt("REM_ENTOFI"));
                descartadoEntrada = rs.getString("REM_ENTDES");
                motivosDescarteEntrada = rs.getString("REM_ENTMTD");
                usuarioEntrada = rs.getString("REM_ENTUSU");
                String fechaE = String.valueOf(rs.getInt("REM_ENTFEC"));
                try {
                    fechaDocumento = yyyymmdd.parse(fechaE);
                    fechaEntrada = (ddmmyyyy.format(fechaDocumento));
                } catch (Exception e) {
                    fechaEntrada = fechaE;
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: Leer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ToolsBD.closeConn(conn, ps, rs);
        }
    }
