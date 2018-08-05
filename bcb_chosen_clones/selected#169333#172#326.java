    protected Boolean lancerincident(long idbloc, String Etatbloc, java.util.GregorianCalendar datebloc, long idServeur, String niveau, String message) {
        String codeerr;
        Boolean retour = false;
        Boolean SauvegardeEtatAutocommit;
        int etat;
        acgtools_core.AcgIO.SortieLog(new Date() + " - Appel de la fonction Lancer incident");
        Statement statement = null;
        ResultSet resultat = null;
        String RequeteSQL = "";
        acgtools_core.AcgIO.SortieLog(new Date() + " - nouvel incident pour le bloc : " + acgtools_core.AcgIO.RetourneDate(datebloc));
        try {
            this.con = db.OpenConnection();
            SauvegardeEtatAutocommit = this.con.getAutoCommit();
            this.con.setAutoCommit(false);
            if (idbloc == 0) {
                idbloc = this.CreationBloc(idServeur);
                if (idbloc == 0) {
                    retour = false;
                    acgtools_core.AcgIO.SortieLog(new Date() + " - Problème lors de la création du bloc");
                    this.con.rollback();
                    this.con.close();
                    return false;
                }
            }
            acgtools_core.AcgIO.SortieLog(new Date() + " - bloc : " + idbloc);
            etat = this.ChargerEtatServeur(idbloc, datebloc);
            if (etat != 2) {
                statement = con.createStatement();
                acgtools_core.AcgIO.SortieLog(new Date() + " - Etat chargé");
                RequeteSQL = "SELECT incref_err_numer FROM tbl_incident_ref " + "WHERE incref_cde_job ='" + idbloc + "' " + "AND incref_err_numer NOT IN " + "(SELECT incref_err_numer FROM tbl_incident_ref " + "WHERE incref_err_etat='c') " + "AND incref_err_numer NOT IN " + "(SELECT incenc_err_numer FROM tbl_incident_encours " + "WHERE incenc_err_etat='c') ;";
                acgtools_core.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                resultat = statement.executeQuery(RequeteSQL);
                if (!resultat.next()) {
                    resultat.close();
                    RequeteSQL = "INSERT INTO tbl_incident_ref " + "(incref_cde_job,incref_err_date,incref_err_etat,incref_niv_crimd,incref_err_msg,incref_err_srvnm)" + "VALUES ('" + idbloc + "','" + acgtools_core.AcgIO.RetourneDate(datebloc) + "','" + Etatbloc + "','" + niveau + "','" + message + "','" + idServeur + "');";
                    acgtools_core.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                    statement.executeUpdate(RequeteSQL);
                    RequeteSQL = "SELECT incref_err_numer FROM tbl_incident_ref " + "WHERE incref_cde_job = '" + idbloc + "' " + "AND incref_err_srvnm = '" + idServeur + "' " + "AND incref_err_date = '" + acgtools_core.AcgIO.RetourneDate(datebloc) + "';";
                    acgtools_core.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                    resultat = statement.executeQuery(RequeteSQL);
                    if (resultat.next()) {
                        codeerr = resultat.getString("incref_err_numer");
                        resultat.close();
                        RequeteSQL = "INSERT INTO tbl_incident_encours" + "(incenc_err_numer, incenc_err_etat, incenc_esc_etap, " + "incenc_err_date, incenc_typ_user,incenc_cde_user,incenc_err_msg,incenc_niv_crimd) " + "VALUES ('" + codeerr + "','" + Etatbloc + "',0, " + "'" + acgtools_core.AcgIO.RetourneDate(datebloc) + "','n',0,'" + message + "','" + niveau + "');";
                        acgtools_core.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                        statement.executeUpdate(RequeteSQL);
                        acgtools_core.AcgIO.SortieLog(new Date() + " - Incident inséré dans la base de données");
                        acgtools_core.AcgIO.SortieLog(new Date() + " - Traitement de l'envois des emails si nécessaire");
                        this.usermail(codeerr, etat, acgtools_core.AcgIO.RetourneDate(datebloc), message);
                        acgtools_core.AcgIO.SortieLog(new Date() + " - Création de l'historique");
                        this.CreerHistorique(codeerr);
                        acgtools_core.AcgIO.SortieLog(new Date() + " - Créer maj");
                        this.CreerMaj(true);
                        retour = true;
                    } else {
                        acgtools_core.AcgIO.SortieLog(new Date() + " - Problème d'insertion du nouvel incident dans la base");
                        retour = false;
                    }
                } else {
                    codeerr = resultat.getString("incref_err_numer");
                    acgtools_core.AcgIO.SortieLog(new Date() + " - Numéro de l'erreur trouvé. Numéro =" + codeerr);
                    RequeteSQL = "SELECT incenc_err_etat FROM tbl_incident_encours " + "WHERE incenc_err_numer='" + codeerr + "';";
                    acgtools_core.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                    resultat = statement.executeQuery(RequeteSQL);
                    if (!resultat.next()) {
                        resultat.close();
                        acgtools_core.AcgIO.SortieLog(new Date() + " - Problème lors de la lecture de l'état de l'incident.");
                        String RequeteSQLInsert = "INSERT INTO tbl_incident_encours" + "(incenc_err_numer, incenc_err_etat, incenc_esc_etap, " + "incenc_err_date, incenc_typ_user,incenc_cde_user,incenc_err_msg,incenc_niv_crimd) " + "VALUES ('" + codeerr + "','" + Etatbloc + "',0, " + "'" + acgtools_core.AcgIO.RetourneDate(datebloc) + "','n',0,'" + "Incident non clotur&eacute; - " + message + "','" + niveau + "');";
                        acgtools_core.AcgIO.SortieLog(new Date() + " - " + RequeteSQLInsert);
                        statement.execute(RequeteSQLInsert);
                        resultat = statement.executeQuery(RequeteSQL);
                    } else {
                        resultat = statement.executeQuery(RequeteSQL);
                        acgtools_core.AcgIO.SortieLog(new Date() + " - Incident correctement positionné dans encours");
                    }
                    if (resultat.next()) {
                        switch(Etatbloc.charAt(0)) {
                            case 'c':
                                {
                                    acgtools_core.AcgIO.SortieLog(new Date() + " - Cloture de l'incident.");
                                    RequeteSQL = "UPDATE tbl_incident_ref SET incref_err_etat='c'" + "WHERE incref_err_numer='" + codeerr + "';";
                                    acgtools_core.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                                    statement.executeUpdate(RequeteSQL);
                                    this.UpdateEnCours(codeerr, "c", niveau, acgtools_core.AcgIO.RetourneDate(datebloc), message, "auto");
                                    acgtools_core.AcgIO.SortieLog(new Date() + " - Traitement de l'envois des emails si nécessaire");
                                    this.usermail(codeerr, etat, message, acgtools_core.AcgIO.RetourneDate(datebloc));
                                    acgtools_core.AcgIO.SortieLog(new Date() + " - Créer maj");
                                    this.CreerMaj(false);
                                    retour = true;
                                    break;
                                }
                            case 'm':
                                {
                                    this.UpdateEnCours(codeerr, "m", niveau, acgtools_core.AcgIO.RetourneDate(datebloc), message, "auto");
                                    acgtools_core.AcgIO.SortieLog(new Date() + " - Traitement de l'envois des emails si nécessaire");
                                    this.usermail(codeerr, etat, message, acgtools_core.AcgIO.RetourneDate(datebloc));
                                    acgtools_core.AcgIO.SortieLog(new Date() + " - Créer maj");
                                    this.CreerMaj(false);
                                    retour = true;
                                    break;
                                }
                            default:
                                {
                                    this.UpdateEnCours(codeerr, "m", niveau, acgtools_core.AcgIO.RetourneDate(datebloc), message, "");
                                    acgtools_core.AcgIO.SortieLog(new Date() + " - Traitement de l'envois des emails si nécessaire");
                                    this.usermail(codeerr, etat, message, acgtools_core.AcgIO.RetourneDate(datebloc));
                                    acgtools_core.AcgIO.SortieLog(new Date() + " - Créer maj");
                                    this.CreerMaj(false);
                                    retour = true;
                                    break;
                                }
                        }
                    } else {
                        acgtools_core.AcgIO.SortieLog(new Date() + " - Problème lors de la lecture de l'état de l'incident.");
                        retour = false;
                    }
                }
            } else {
                acgtools_core.AcgIO.SortieLog(new Date() + " - Systeme en maintenance, pas de remontée d'incidents.");
                retour = false;
            }
        } catch (ClassNotFoundException ex) {
            acgtools_core.AcgIO.SortieLog(new Date() + "Annulation des modifications.");
            con.rollback();
            acgtools_core.AcgIO.SortieLog(new Date() + "Probléme lors de l'éxécution de la connexion.");
            acgtools_core.AcgIO.SortieLog(ex.getMessage());
            retour = false;
        } catch (SQLException ex) {
            acgtools_core.AcgIO.SortieLog(new Date() + "Annulation des modifications.");
            con.rollback();
            acgtools_core.AcgIO.SortieLog(ex.getMessage());
            acgtools_core.AcgIO.SortieLog(new Date() + "Probléme lors de l'éxécution de la requète SQL :");
            acgtools_core.AcgIO.SortieLog(RequeteSQL);
            retour = false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (retour) {
                    con.commit();
                    acgtools_core.AcgIO.SortieLog(new Date() + " - Création de l'incident : succès");
                } else {
                    con.rollback();
                    acgtools_core.AcgIO.SortieLog(new Date() + " - Création de l'incident : echec");
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                acgtools_core.AcgIO.SortieLog(new Date() + "Problème lors de la fermeture de la connection à la base de données");
            }
            return retour;
        }
    }
