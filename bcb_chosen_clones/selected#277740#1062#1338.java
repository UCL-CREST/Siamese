    public void actualizar() throws SQLException, ClassNotFoundException, Exception {
        Connection conn = null;
        PreparedStatement ms = null;
        if (!validado) {
            validado = validar();
        }
        if (!validado) {
            throw new Exception("No s'ha realitzat la validació de les dades del registre ");
        }
        registroActualizado = false;
        try {
            int fzaanoe;
            String campo;
            fechaTest = dateF.parse(datasalida);
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaTest);
            DateFormat date1 = new SimpleDateFormat("yyyyMMdd");
            fzaanoe = Integer.parseInt(anoSalida);
            int fzafent = Integer.parseInt(date1.format(fechaTest));
            conn = ToolsBD.getConn();
            conn.setAutoCommit(false);
            int fzanume = Integer.parseInt(numeroSalida);
            int fzacagc = Integer.parseInt(oficina);
            int off_codi = 0;
            try {
                off_codi = Integer.parseInt(oficinafisica);
            } catch (Exception e) {
            }
            fechaTest = dateF.parse(data);
            cal.setTime(fechaTest);
            int fzafdoc = Integer.parseInt(date1.format(fechaTest));
            String fzacone, fzacone2;
            if (idioex.equals("1")) {
                fzacone = comentario;
                fzacone2 = "";
            } else {
                fzacone = "";
                fzacone2 = comentario;
            }
            String fzaproce;
            int fzactagg, fzacagge;
            if (fora.equals("")) {
                fzactagg = 90;
                fzacagge = Integer.parseInt(balears);
                fzaproce = "";
            } else {
                fzaproce = fora;
                fzactagg = 0;
                fzacagge = 0;
            }
            int ceros = 0;
            int fzacorg = Integer.parseInt(remitent);
            int fzanent;
            String fzacent;
            if (altres.equals("")) {
                altres = "";
                fzanent = Integer.parseInt(entidad2);
                fzacent = entidadCastellano;
            } else {
                fzanent = 0;
                fzacent = "";
            }
            int fzacidi = Integer.parseInt(idioex);
            horaTest = horaF.parse(hora);
            cal.setTime(horaTest);
            DateFormat hhmm = new SimpleDateFormat("HHmm");
            int fzahora = Integer.parseInt(hhmm.format(horaTest));
            if (entrada1.equals("")) {
                entrada1 = "0";
            }
            if (entrada2.equals("")) {
                entrada2 = "0";
            }
            int fzanloc = Integer.parseInt(entrada1);
            int fzaaloc = Integer.parseInt(entrada2);
            if (disquet.equals("")) {
                disquet = "0";
            }
            int fzandis = Integer.parseInt(disquet);
            if (fzandis > 0) {
                ToolsBD.actualizaDisqueteEntrada(conn, fzandis, oficina, anoSalida, errores);
            }
            Date fechaSystem = new Date();
            DateFormat aaaammdd = new SimpleDateFormat("yyyyMMdd");
            int fzafsis = Integer.parseInt(aaaammdd.format(fechaSystem));
            DateFormat hhmmss = new SimpleDateFormat("HHmmss");
            DateFormat sss = new SimpleDateFormat("S");
            String ss = sss.format(fechaSystem);
            if (ss.length() > 2) {
                ss = ss.substring(0, 2);
            }
            int fzahsis = Integer.parseInt(hhmmss.format(fechaSystem) + ss);
            if (correo != null) {
                String insertBZNCORR = "INSERT INTO BZNCORR (FZPCENSA, FZPCAGCO, FZPANOEN, FZPNUMEN, FZPNCORR)" + "VALUES (?,?,?,?,?)";
                String updateBZNCORR = "UPDATE BZNCORR SET FZPNCORR=? WHERE FZPCENSA=? AND FZPCAGCO=? AND FZPANOEN=? AND FZPNUMEN=?";
                String deleteBZNCORR = "DELETE FROM BZNCORR WHERE FZPCENSA=? AND FZPCAGCO=? AND FZPANOEN=? AND FZPNUMEN=?";
                int actualizados = 0;
                if (!correo.trim().equals("")) {
                    ms = conn.prepareStatement(updateBZNCORR);
                    ms.setString(1, correo);
                    ms.setString(2, "S");
                    ms.setInt(3, fzacagc);
                    ms.setInt(4, fzaanoe);
                    ms.setInt(5, fzanume);
                    actualizados = ms.executeUpdate();
                    ms.close();
                    if (actualizados == 0) {
                        ms = conn.prepareStatement(insertBZNCORR);
                        ms.setString(1, "S");
                        ms.setInt(2, fzacagc);
                        ms.setInt(3, fzaanoe);
                        ms.setInt(4, fzanume);
                        ms.setString(5, correo);
                        ms.execute();
                        ms.close();
                    }
                } else {
                    ms = conn.prepareStatement(deleteBZNCORR);
                    ms.setString(1, "S");
                    ms.setInt(2, fzacagc);
                    ms.setInt(3, fzaanoe);
                    ms.setInt(4, fzanume);
                    ms.execute();
                }
            }
            String deleteOfifis = "DELETE FROM BZSALOFF WHERE FOSANOEN=? AND FOSNUMEN=? AND FOSCAGCO=?";
            ms = conn.prepareStatement(deleteOfifis);
            ms.setInt(1, fzaanoe);
            ms.setInt(2, fzanume);
            ms.setInt(3, fzacagc);
            ms.execute();
            ms.close();
            String insertOfifis = "INSERT INTO BZSALOFF (FOSANOEN, FOSNUMEN, FOSCAGCO, OFS_CODI)" + "VALUES (?,?,?,?)";
            ms = conn.prepareStatement(insertOfifis);
            ms.setInt(1, fzaanoe);
            ms.setInt(2, fzanume);
            ms.setInt(3, fzacagc);
            ms.setInt(4, off_codi);
            ms.execute();
            ms.close();
            ms = conn.prepareStatement("UPDATE BZSALIDA SET FZSFDOCU=?, FZSREMIT=?, FZSCONEN=?, FZSCTIPE=?, " + "FZSCEDIE=?, FZSENULA=?, FZSPROCE=?, FZSFENTR=?, FZSCTAGG=?, FZSCAGGE=?, FZSCORGA=?, " + "FZSCENTI=?, FZSNENTI=?, FZSHORA=?, FZSCIDIO=?, FZSCONE2=?, FZSNLOC=?, FZSALOC=?, FZSNDIS=?, " + "FZSCUSU=?, FZSCIDI=? WHERE FZSANOEN=? AND FZSNUMEN=? AND FZSCAGCO=? ");
            ms.setInt(1, fzafdoc);
            ms.setString(2, (altres.length() > 30) ? altres.substring(0, 30) : altres);
            ms.setString(3, (fzacone.length() > 160) ? fzacone.substring(0, 160) : fzacone);
            ms.setString(4, (tipo.length() > 2) ? tipo.substring(0, 1) : tipo);
            ms.setString(5, "N");
            ms.setString(6, (registroAnulado.length() > 1) ? registroAnulado.substring(0, 1) : registroAnulado);
            ms.setString(7, (fzaproce.length() > 25) ? fzaproce.substring(0, 25) : fzaproce);
            ms.setInt(8, fzafent);
            ms.setInt(9, fzactagg);
            ms.setInt(10, fzacagge);
            ms.setInt(11, fzacorg);
            ms.setString(12, (fzacent.length() > 7) ? fzacent.substring(0, 8) : fzacent);
            ms.setInt(13, fzanent);
            ms.setInt(14, fzahora);
            ms.setInt(15, fzacidi);
            ms.setString(16, (fzacone2.length() > 160) ? fzacone2.substring(0, 160) : fzacone2);
            ms.setInt(17, fzanloc);
            ms.setInt(18, fzaaloc);
            ms.setInt(19, fzandis);
            ms.setString(20, (usuario.toUpperCase().length() > 10) ? usuario.toUpperCase().substring(0, 10) : usuario.toUpperCase());
            ms.setString(21, idioma);
            ms.setInt(22, fzaanoe);
            ms.setInt(23, fzanume);
            ms.setInt(24, fzacagc);
            boolean modificado = false;
            if (!motivo.equals("")) {
                javax.naming.InitialContext contexto = new javax.naming.InitialContext();
                Object ref = contexto.lookup("es.caib.regweb.RegistroModificadoSalidaHome");
                RegistroModificadoSalidaHome home = (RegistroModificadoSalidaHome) javax.rmi.PortableRemoteObject.narrow(ref, RegistroModificadoSalidaHome.class);
                RegistroModificadoSalida registroModificado = home.create();
                registroModificado.setAnoSalida(fzaanoe);
                registroModificado.setOficina(fzacagc);
                if (!entidad1Nuevo.trim().equals("")) {
                    if (entidad2Nuevo.equals("")) {
                        entidad2Nuevo = "0";
                    }
                }
                int fzanentNuevo;
                String fzacentNuevo;
                if (altresNuevo.trim().equals("")) {
                    altresNuevo = "";
                    fzanentNuevo = Integer.parseInt(entidad2Nuevo);
                    fzacentNuevo = convierteEntidadCastellano(entidad1Nuevo, conn);
                } else {
                    fzanentNuevo = 0;
                    fzacentNuevo = "";
                }
                if (!fzacentNuevo.equals(fzacent) || fzanentNuevo != fzanent) {
                    registroModificado.setEntidad2(fzanentNuevo);
                    registroModificado.setEntidad1(fzacentNuevo);
                } else {
                    registroModificado.setEntidad2(0);
                    registroModificado.setEntidad1("");
                }
                if (!comentarioNuevo.trim().equals(comentario.trim())) {
                    registroModificado.setExtracto(comentarioNuevo);
                } else {
                    registroModificado.setExtracto("");
                }
                registroModificado.setUsuarioModificacion(usuario.toUpperCase());
                registroModificado.setNumeroRegistro(fzanume);
                if (altresNuevo.equals(altres)) {
                    registroModificado.setRemitente("");
                } else {
                    registroModificado.setRemitente(altresNuevo);
                }
                registroModificado.setMotivo(motivo);
                modificado = registroModificado.generarModificacion(conn);
                registroModificado.remove();
            }
            if ((modificado && !motivo.equals("")) || motivo.equals("")) {
                int afectados = ms.executeUpdate();
                if (afectados > 0) {
                    registroActualizado = true;
                } else {
                    registroActualizado = false;
                }
                String remitente = "";
                if (!altres.trim().equals("")) {
                    remitente = altres;
                } else {
                    javax.naming.InitialContext contexto = new javax.naming.InitialContext();
                    Object ref = contexto.lookup("es.caib.regweb.ValoresHome");
                    ValoresHome home = (ValoresHome) javax.rmi.PortableRemoteObject.narrow(ref, ValoresHome.class);
                    Valores valor = home.create();
                    remitente = valor.recuperaRemitenteCastellano(fzacent, fzanent + "");
                    valor.remove();
                }
                try {
                    Class t = Class.forName("es.caib.regweb.module.PluginHook");
                    Class[] partypes = { String.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class };
                    Object[] params = { "M", new Integer(fzaanoe), new Integer(fzanume), new Integer(fzacagc), new Integer(fzafdoc), remitente, comentario, tipo, new Integer(fzafent), new Integer(fzacagge), fzaproce, new Integer(fzacorg), idioma };
                    java.lang.reflect.Method metodo = t.getMethod("salida", partypes);
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
                logLopdBZSALIDA("UPDATE", (usuario.toUpperCase().length() > 10) ? usuario.toUpperCase().substring(0, 10) : usuario.toUpperCase(), fzahsis, horamili, fzanume, fzaanoe, fzacagc);
                conn.commit();
            } else {
                registroActualizado = false;
                errores.put("", "Error inesperat, no s'ha modificat el registre");
                throw new RemoteException("Error inesperat, no s'ha modifcat el registre");
            }
        } catch (Exception ex) {
            System.out.println("Error inesperat " + ex.getMessage());
            ex.printStackTrace();
            registroActualizado = false;
            errores.put("", "Error inesperat, no s'ha modificat el registre" + ": " + ex.getClass() + "->" + ex.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException sqle) {
                throw new RemoteException("S'ha produ\357t un error i no s'han pogut tornar enrere els canvis efectuats", sqle);
            }
            throw new RemoteException("Error inesperat, no s'ha modifcat el registre", ex);
        } finally {
            ToolsBD.closeConn(conn, ms, null);
        }
    }
