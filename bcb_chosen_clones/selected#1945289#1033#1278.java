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
            fechaTest = dateF.parse(dataentrada);
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaTest);
            DateFormat date1 = new SimpleDateFormat("yyyyMMdd");
            fzaanoe = Integer.parseInt(anoEntrada);
            int fzafent = Integer.parseInt(date1.format(fechaTest));
            conn = ToolsBD.getConn();
            conn.setAutoCommit(false);
            int fzanume = Integer.parseInt(numeroEntrada);
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
            int fzacorg = Integer.parseInt(destinatari);
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
            if (salida1.equals("")) {
                salida1 = "0";
            }
            if (salida2.equals("")) {
                salida2 = "0";
            }
            int fzanloc = Integer.parseInt(salida1);
            int fzaaloc = Integer.parseInt(salida2);
            if (disquet.equals("")) {
                disquet = "0";
            }
            int fzandis = Integer.parseInt(disquet);
            if (fzandis > 0) {
                ToolsBD.actualizaDisqueteEntrada(conn, fzandis, oficina, anoEntrada, errores);
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
            String deleteOfifis = "DELETE FROM BZENTOFF WHERE FOEANOEN=? AND FOENUMEN=? AND FOECAGCO=?";
            ms = conn.prepareStatement(deleteOfifis);
            ms.setInt(1, fzaanoe);
            ms.setInt(2, fzanume);
            ms.setInt(3, fzacagc);
            ms.execute();
            ms.close();
            String insertOfifis = "INSERT INTO BZENTOFF (FOEANOEN, FOENUMEN, FOECAGCO, OFE_CODI)" + "VALUES (?,?,?,?)";
            ms = conn.prepareStatement(insertOfifis);
            ms.setInt(1, fzaanoe);
            ms.setInt(2, fzanume);
            ms.setInt(3, fzacagc);
            ms.setInt(4, off_codi);
            ms.execute();
            ms.close();
            ms = conn.prepareStatement("UPDATE BZENTRA SET FZAFDOCU=?, FZAREMIT=?, FZACONEN=?, FZACTIPE=?, " + "FZACEDIE=?, FZAENULA=?, FZAPROCE=?, FZAFENTR=?, FZACTAGG=?, FZACAGGE=?, FZACORGA=?, " + "FZACENTI=?, FZANENTI=?, FZAHORA=?, FZACIDIO=?, FZACONE2=?, FZANLOC=?, FZAALOC=?, FZANDIS=?, " + "FZACUSU=?, FZACIDI=? WHERE FZAANOEN=? AND FZANUMEN=? AND FZACAGCO=?");
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
                Object ref = contexto.lookup("es.caib.regweb.RegistroModificadoEntradaHome");
                RegistroModificadoEntradaHome home = (RegistroModificadoEntradaHome) javax.rmi.PortableRemoteObject.narrow(ref, RegistroModificadoEntradaHome.class);
                RegistroModificadoEntrada registroModificado = home.create();
                registroModificado.setAnoEntrada(fzaanoe);
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
                    Class[] partypes = { String.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class, String.class, String.class, String.class };
                    Object[] params = { "M", new Integer(fzaanoe), new Integer(fzanume), new Integer(fzacagc), new Integer(fzafdoc), remitente, comentario, tipo, new Integer(fzafent), new Integer(fzacagge), fzaproce, new Integer(fzacorg), idioma, BOIBdata, new Integer(BOIBnumeroBOCAIB), new Integer(BOIBpagina), new Integer(BOIBlineas), BOIBtexto, BOIBobservaciones, correo };
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
                conn.commit();
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
                logLopdBZENTRA("UPDATE", (usuario.toUpperCase().length() > 10) ? usuario.toUpperCase().substring(0, 10) : usuario.toUpperCase(), fzafsis, horamili, fzanume, fzaanoe, fzacagc);
            } else {
                registroActualizado = false;
                errores.put("", "Error inesperat, no s'ha modificat el registre");
                throw new RemoteException("Error inesperat, no s'ha modifcat el registre");
            }
            System.out.println("Municipi codi: " + municipi060);
            if (municipi060.equals("000")) eliminar060(); else if (!municipi060.equals("")) actualizar060();
        } catch (Exception ex) {
            System.out.println("Error inesperat, no s'ha desat el registre: " + ex.getMessage());
            ex.printStackTrace();
            registroActualizado = false;
            errores.put("", "Error inesperat, no s'ha desat el registre" + ": " + ex.getClass() + "->" + ex.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException sqle) {
                throw new RemoteException("S'ha produït un error i no s'han pogut tornar enrere els canvis efectuats", sqle);
            }
            throw new RemoteException("Error inesperat, no s'ha modifcat el registre", ex);
        } finally {
            ToolsBD.closeConn(conn, ms, null);
        }
    }
