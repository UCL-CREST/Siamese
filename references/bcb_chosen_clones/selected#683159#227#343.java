    public boolean visar() throws SQLException, ClassNotFoundException, Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        Date fechaSystem = new Date();
        DateFormat aaaammdd = new SimpleDateFormat("yyyyMMdd");
        DateFormat hhmmss = new SimpleDateFormat("HHmmss");
        DateFormat sss = new SimpleDateFormat("S");
        String ss = sss.format(fechaSystem);
        if (ss.length() > 2) {
            ss = ss.substring(0, 2);
        }
        boolean visado = false;
        try {
            conn = ToolsBD.getConn();
            conn.setAutoCommit(false);
            String sentenciaSql = "UPDATE BZMODIF SET FZJCUSVI=?, FZJFVISA=?, FZJHVISA=?" + ((hayVisadoExtracto) ? ", FZJIEXTR=?" : "") + ((hayVisadoRemitente) ? ", FZJIREMI=?" : "") + " WHERE FZJCENSA='E' AND FZJCAGCO=? AND FZJNUMEN=? AND FZJANOEN=? AND FZJFMODI=? AND FZJHMODI=?";
            ps = conn.prepareStatement(sentenciaSql);
            ps.setString(1, usuarioVisado);
            ps.setInt(2, Integer.parseInt(aaaammdd.format(fechaSystem)));
            ps.setInt(3, Integer.parseInt(hhmmss.format(fechaSystem) + ss));
            int contador = 4;
            if (hayVisadoExtracto) {
                ps.setString(contador++, "X");
            }
            if (hayVisadoRemitente) {
                ps.setString(contador++, "X");
            }
            ps.setInt(contador++, oficina);
            ps.setInt(contador++, numeroRegistro);
            ps.setInt(contador++, anoEntrada);
            ps.setString(contador++, fechaModificacion);
            ps.setString(contador++, horaModificacion);
            int registrosAfectados = ps.executeUpdate();
            if (registrosAfectados > 0 && !hayVisadoExtracto && !hayVisadoRemitente) {
                visado = true;
            }
            if (registrosAfectados > 0 && (hayVisadoExtracto || hayVisadoRemitente)) {
                boolean generado = generarBZVISAD(conn, Integer.parseInt(aaaammdd.format(fechaSystem)), Integer.parseInt(hhmmss.format(fechaSystem) + ss));
                if (generado) {
                    visado = actualizarBZENTRA(conn);
                }
                String rem = "";
                String com = "";
                if (hayVisadoRemitente) {
                    if (!remitente.trim().equals("")) {
                        rem = remitente;
                    } else {
                        javax.naming.InitialContext contexto = new javax.naming.InitialContext();
                        Object ref = contexto.lookup("es.caib.regweb.ValoresHome");
                        ValoresHome home = (ValoresHome) javax.rmi.PortableRemoteObject.narrow(ref, ValoresHome.class);
                        Valores valor = home.create();
                        rem = valor.recuperaRemitenteCastellano(entidad1, entidad2 + "");
                        valor.remove();
                    }
                } else {
                    if (!altres.trim().equals("")) {
                        rem = remitente;
                    } else {
                        javax.naming.InitialContext contexto = new javax.naming.InitialContext();
                        Object ref = contexto.lookup("es.caib.regweb.ValoresHome");
                        ValoresHome home = (ValoresHome) javax.rmi.PortableRemoteObject.narrow(ref, ValoresHome.class);
                        Valores valor = home.create();
                        rem = valor.recuperaRemitenteCastellano(entidad1Old, entidad2Old + "");
                        valor.remove();
                    }
                }
                if (hayVisadoExtracto) {
                    com = extracto;
                } else {
                    com = comentario;
                }
                try {
                    Class t = Class.forName("es.caib.regweb.module.PluginHook");
                    Class[] partypes = { String.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class, String.class, String.class, String.class };
                    Object[] params = { "V", new Integer(anoEntrada), new Integer(numeroRegistro), new Integer(oficina), new Integer(fechaDocumento), rem, com, tipoDocumento, new Integer(fechaRegistro), new Integer(fzacagge), fora, new Integer(destinatario), idioma, null, null, null, null, null, null, null };
                    java.lang.reflect.Method metodo = t.getMethod("entrada", partypes);
                    metodo.invoke(null, params);
                } catch (IllegalAccessException iae) {
                } catch (IllegalArgumentException iae) {
                } catch (InvocationTargetException ite) {
                } catch (NullPointerException npe) {
                } catch (ExceptionInInitializerError eiie) {
                } catch (NoSuchMethodException nsme) {
                } catch (SecurityException se) {
                } catch (LinkageError le) {
                } catch (ClassNotFoundException le) {
                }
            }
            conn.commit();
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
            int fzafsis = Integer.parseInt(aaaammdd.format(fechaSystem));
            logLopdBZMODIF("UPDATE", sessioEjb.getCallerPrincipal().getName().toUpperCase(), fzafsis, horamili, 'E', numeroRegistro, anoEntrada, oficina, Integer.parseInt(fechaModificacion), Integer.parseInt(horaModificacion));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            visado = false;
            try {
                if (conn != null) conn.rollback(); else System.out.println("ERROR: No es pot fer rollback sense connexió!");
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        } finally {
            ToolsBD.closeConn(conn, ps, null);
        }
        return visado;
    }
