    protected String doIt() throws java.lang.Exception {
        StringBuffer sql = null;
        int no = 0;
        String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;
        if (m_deleteOldImported) {
            sql = new StringBuffer("DELETE I_BPartner " + "WHERE I_IsImported='Y'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            log.fine("Delete Old Impored =" + no);
        }
        sql = new StringBuffer("UPDATE I_BPartner " + "SET AD_Client_ID = COALESCE(AD_Client_ID, ").append(m_AD_Client_ID).append(")," + " AD_Org_ID = COALESCE(AD_Org_ID, 0)," + " IsActive = COALESCE(IsActive, 'Y')," + " Created = COALESCE(Created, current_timestamp)," + " CreatedBy = COALESCE(CreatedBy, 0)," + " Updated = COALESCE(Updated, current_timestamp)," + " UpdatedBy = COALESCE(UpdatedBy, 0)," + " I_ErrorMsg = ''," + " I_IsImported = 'N' " + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
        no = DB.executeUpdate(sql.toString());
        log.fine("Reset=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET GroupValue=(SELECT Value FROM C_BP_Group g WHERE g.IsDefault='Y'" + " AND g.AD_Client_ID=i.AD_Client_ID AND ROWNUM=1) " + "WHERE GroupValue IS NULL AND C_BP_Group_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Group Default=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET C_BP_Group_ID=(SELECT C_BP_Group_ID FROM C_BP_Group g" + " WHERE i.GroupValue=g.Value AND g.AD_Client_ID=i.AD_Client_ID ORDER BY g.IsDefault DESC LIMIT 1) " + "WHERE C_BP_Group_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Group=" + no);
        sql = new StringBuffer("UPDATE I_BPartner " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'" + getMsg("ImportBPInvalidGroup") + ". ' " + "WHERE C_BP_Group_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.config("Invalid Group=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET CountryCode=(SELECT CountryCode FROM C_Country c WHERE c.isactive='Y'" + " AND c.AD_Client_ID IN (0, i.AD_Client_ID) AND ROWNUM=1) " + "WHERE CountryCode IS NULL AND C_Country_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Country Default=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c" + " WHERE i.CountryCode=c.CountryCode AND c.AD_Client_ID IN (0, i.AD_Client_ID)) " + "WHERE C_Country_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Country=" + no);
        sql = new StringBuffer("UPDATE I_BPartner " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'" + getMsg("ImportBPInvalidCountry") + ". ' " + "WHERE C_Country_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.config("Invalid Country=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "Set RegionName=(SELECT Name FROM C_Region r" + " WHERE r.IsDefault='Y' AND r.C_Country_ID=i.C_Country_ID" + " AND r.AD_Client_ID IN (0, i.AD_Client_ID) AND ROWNUM=1) " + "WHERE RegionName IS NULL AND C_Region_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Region Default=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r" + " WHERE r.Name=i.RegionName AND r.C_Country_ID=i.C_Country_ID" + " AND r.AD_Client_ID IN (0, i.AD_Client_ID)) " + "WHERE C_Region_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Region=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'" + getMsg("ImportBPInvalidRegion") + ". ' " + " WHERE C_Region_ID IS NULL " + " AND EXISTS (SELECT * FROM C_Country c" + " WHERE c.C_Country_ID=i.C_Country_ID AND c.HasRegion='Y')" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.config("Invalid Region=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET BPContactGreeting=NULL WHERE C_Greeting_ID IS NULL AND char_length(trim(BPContactGreeting)) = 0 AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Reset Greeting=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET C_Greeting_ID=(SELECT C_Greeting_ID FROM C_Greeting g" + " WHERE i.BPContactGreeting=g.Name AND g.AD_Client_ID IN (0, i.AD_Client_ID)) " + "WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Greeting=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'" + getMsg("ImportBPInvalidGreeting") + ". ' " + "WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.config("Invalid Greeting=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner p" + " WHERE i.Value=p.Value AND p.AD_Client_ID=i.AD_Client_ID) " + "WHERE C_BPartner_ID IS NULL AND Value IS NOT NULL" + " AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Found BPartner=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET AD_User_ID=(SELECT AD_User_ID FROM AD_User c" + " WHERE i.ContactName=c.Name AND i.C_BPartner_ID=c.C_BPartner_ID AND c.AD_Client_ID=i.AD_Client_ID) " + "WHERE C_BPartner_ID IS NOT NULL AND AD_User_ID IS NULL AND ContactName IS NOT NULL" + " AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Found Contact=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET C_BPartner_Location_ID=(SELECT C_BPartner_Location_ID" + " FROM C_BPartner_Location bpl INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID)" + " WHERE i.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=i.AD_Client_ID" + " AND DUMP(i.Address1)=DUMP(l.Address1) AND DUMP(i.Address2)=DUMP(l.Address2)" + " AND DUMP(i.City)=DUMP(l.City) AND DUMP(i.Postal)=DUMP(l.Postal) AND DUMP(i.Postal_Add)=DUMP(l.Postal_Add)" + " AND DUMP(i.C_Region_ID)=DUMP(l.C_Region_ID) AND DUMP(i.C_Country_ID)=DUMP(l.C_Country_ID)) " + "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL" + " AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Found Location=" + no);
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET C_Categoria_IVA_Codigo=" + MCategoriaIva.CONSUMIDOR_FINAL + " WHERE (C_Categoria_IVA_Codigo IS NULL OR C_Categoria_IVA_Codigo = 0) " + "  AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET C_Categoria_IVA_ID= " + " (SELECT C_Categoria_IVA_ID " + "  FROM C_Categoria_IVA c " + "  WHERE i.C_Categoria_IVA_Codigo=c.Codigo AND c.AD_Client_ID=i.AD_Client_ID " + " ) " + "WHERE C_Categoria_IVA_ID IS NULL " + "  AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        sql = new StringBuffer("UPDATE I_BPartner i " + "SET SalesRep_ID=(SELECT AD_User_ID " + "FROM AD_User u " + "WHERE u.Name = i.SalesRep_Name AND u.AD_Client_ID IN (0, i.AD_Client_ID)) " + "WHERE SalesRep_ID IS NULL " + "AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Country=" + no);
        int noInsert = 0;
        int noUpdate = 0;
        sql = new StringBuffer("SELECT I_BPartner_ID, C_BPartner_ID," + "C_BPartner_Location_ID,COALESCE(Address1,Address2,City,RegionName,CountryCode)," + "AD_User_ID,ContactName " + "FROM I_BPartner " + "WHERE I_IsImported='N'").append(clientCheck);
        Connection conn = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
        try {
            log.info("En importBPartbner antes de hacer el update en c_BPartner");
            PreparedStatement pstmt_updateBPartner = conn.prepareStatement("UPDATE C_BPartner " + "SET Value=aux.Value" + ",Name=aux.Name" + ",Name2=aux.Name2" + ",Description=aux.Description" + ",DUNS=aux.DUNS" + ",TaxID=aux.TaxID" + ",NAICS=aux.NAICS" + ",C_BP_Group_ID=aux.C_BP_Group_ID" + ",Updated=current_timestamp" + ",UpdatedBy=aux.UpdatedBy" + ",IIBB=aux.IIBB" + " from (SELECT Value,Name,Name2,Description,DUNS,TaxID,NAICS,C_BP_Group_ID,UpdatedBy,IIBB FROM I_BPartner WHERE I_BPartner_ID=?) as aux" + " WHERE C_BPartner_ID=?");
            log.info("En importBPartbner despues de hacer el update en c_BPartner");
            PreparedStatement pstmt_insertLocation = conn.prepareStatement("INSERT INTO C_Location (C_Location_ID," + "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy," + "Address1,Address2,City,Postal,Postal_Add,C_Country_ID,C_Region_ID) " + "SELECT ?," + "AD_Client_ID,AD_Org_ID,'Y',current_timestamp,CreatedBy,current_timestamp,UpdatedBy," + "Address1,Address2,City,Postal,Postal_Add,C_Country_ID,C_Region_ID " + "FROM I_BPartner " + "WHERE I_BPartner_ID=?");
            PreparedStatement pstmt_insertBPLocation = conn.prepareStatement("INSERT INTO C_BPartner_Location ( " + "	C_BPartner_Location_ID," + "	AD_Client_ID," + "	AD_Org_ID," + "	IsActive," + "	Created," + "	CreatedBy," + "	Updated," + "	UpdatedBy," + "	Name," + "	IsBillTo," + "	IsShipTo," + "	IsPayFrom," + "	IsRemitTo," + "	Phone," + "	Phone2," + "	Fax," + "	C_BPartner_ID," + "	C_Location_ID) " + "SELECT ?,AD_Client_ID,AD_Org_ID,'Y',current_timestamp,CreatedBy,current_timestamp,UpdatedBy," + "CASE WHEN char_length(trim(coalesce(address1,''))) > 0 THEN address1 " + "     WHEN char_length(trim(coalesce(city,''))) > 0 THEN city " + "     WHEN char_length(trim(coalesce(regionname,''))) > 0 THEN regionname " + "     ELSE name " + "END," + "'Y','Y','Y','Y'," + "Phone,Phone2,Fax, ?,? " + "FROM I_BPartner " + "WHERE I_BPartner_ID=?");
            PreparedStatement pstmt_insertBPContact = conn.prepareStatement("INSERT INTO AD_User (AD_User_ID," + "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy," + "C_BPartner_ID,C_BPartner_Location_ID,C_Greeting_ID," + "Name,Title,Description,Comments,Phone,Phone2,Fax,EMail,Birthday) " + "SELECT ?," + "AD_Client_ID,AD_Org_ID,'Y',current_timestamp,CreatedBy,current_timestamp,UpdatedBy," + "?,?,C_Greeting_ID," + "ContactName,Title,ContactDescription,Comments,Phone,Phone2,Fax,EMail,Birthday " + "FROM I_BPartner " + " WHERE I_BPartner_ID=?");
            PreparedStatement pstmt_updateBPContact = conn.prepareStatement("UPDATE AD_User " + "SET C_Greeting_ID=aux1.C_Greeting_ID" + ",Name=aux1.Name" + ",Title=aux1.Title" + ",Description=aux1.Description" + ",Comments=aux1.Commets" + ",Phone=aux1.Phone" + ",Phone2=aux1.Phone2" + ",Fax=aux1.Fax" + ",EMail=aux1.EMail" + ",Birthday=aux1.Birthaday" + ",Updated=current_timestamp" + ",UpdatedBy=aux1.UpdatedBy" + " from (SELECT C_Greeting_ID,ContactName,Title,ContactDescription,Comments,Phone,Phone2,Fax,EMail,Birthday,UpdatedBy FROM I_BPartner WHERE I_BPartner_ID=?) as aux1" + " WHERE AD_User_ID=?");
            PreparedStatement pstmt_setImported = conn.prepareStatement("UPDATE I_BPartner SET I_IsImported='Y'," + " C_BPartner_ID=?, C_BPartner_Location_ID=?, AD_User_ID=?, " + " Updated=current_timestamp, Processed='Y' WHERE I_BPartner_ID=?");
            PreparedStatement pstmt = DB.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int I_BPartner_ID = rs.getInt(1);
                int C_BPartner_ID = rs.getInt(2);
                boolean newBPartner = C_BPartner_ID == 0;
                int C_BPartner_Location_ID = rs.getInt(3);
                String newLocali = rs.getString(4);
                boolean newLocation = rs.getString(4) != null;
                int AD_User_ID = rs.getInt(5);
                boolean newContact = rs.getString(6) != null;
                log.fine("I_BPartner_ID=" + I_BPartner_ID + ", C_BPartner_ID=" + C_BPartner_ID + ", C_BPartner_Location_ID=" + C_BPartner_Location_ID + " create=" + newLocation + ", AD_User_ID=" + AD_User_ID + " create=" + newContact);
                if (newBPartner) {
                    X_I_BPartner iBP = new X_I_BPartner(getCtx(), I_BPartner_ID, null);
                    MBPartner bp = new MBPartner(iBP);
                    if (bp.save()) {
                        C_BPartner_ID = bp.getC_BPartner_ID();
                        log.finest("Insert BPartner");
                        noInsert++;
                    } else {
                        sql = new StringBuffer("UPDATE I_BPartner i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert BPartner failed: " + CLogger.retrieveErrorAsString())).append(" WHERE I_BPartner_ID=").append(I_BPartner_ID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                } else {
                    pstmt_updateBPartner.setInt(1, I_BPartner_ID);
                    pstmt_updateBPartner.setInt(2, C_BPartner_ID);
                    try {
                        no = pstmt_updateBPartner.executeUpdate();
                        log.finest("Update BPartner = " + no);
                        noUpdate++;
                    } catch (SQLException ex) {
                        log.finest("Update BPartner -- " + ex.toString());
                        sql = new StringBuffer("UPDATE I_BPartner i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Update BPartner: " + ex.toString())).append(" WHERE I_BPartner_ID=").append(I_BPartner_ID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                }
                if (C_BPartner_Location_ID != 0) {
                } else if (newLocation) {
                    int C_Location_ID = 0;
                    try {
                        C_Location_ID = DB.getNextID(m_AD_Client_ID, "C_Location", null);
                        if (C_Location_ID <= 0) {
                            throw new DBException("No NextID (" + C_Location_ID + ")");
                        }
                        pstmt_insertLocation.setInt(1, C_Location_ID);
                        pstmt_insertLocation.setInt(2, I_BPartner_ID);
                        no = pstmt_insertLocation.executeUpdate();
                        log.finest("Insert Location = " + no);
                    } catch (SQLException ex) {
                        log.finest("Insert Location - " + ex.toString());
                        conn.rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_BPartner i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert Location: " + ex.toString())).append(" WHERE I_BPartner_ID=").append(I_BPartner_ID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                    try {
                        C_BPartner_Location_ID = (DB.getNextID(m_AD_Client_ID, "C_BPartner_Location", null));
                        log.finest("C_BPartner_Location_ID es : " + C_BPartner_Location_ID);
                        if (C_BPartner_Location_ID <= 0) {
                            throw new DBException("No NextID (" + C_BPartner_Location_ID + ")");
                        }
                        pstmt_insertBPLocation.setInt(1, C_BPartner_Location_ID);
                        pstmt_insertBPLocation.setInt(2, C_BPartner_ID);
                        pstmt_insertBPLocation.setInt(3, C_Location_ID);
                        pstmt_insertBPLocation.setInt(4, I_BPartner_ID);
                        no = pstmt_insertBPLocation.executeUpdate();
                        log.finest("Insert BP Location = " + no);
                    } catch (Exception ex) {
                        log.finest("Insert BPLocation - " + ex.toString());
                        conn.rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_BPartner i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert BPLocation: " + ex.toString())).append(" WHERE I_BPartner_ID=").append(I_BPartner_ID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                }
                if (AD_User_ID != 0) {
                    pstmt_updateBPContact.setInt(1, I_BPartner_ID);
                    pstmt_updateBPContact.setInt(2, AD_User_ID);
                    try {
                        no = pstmt_updateBPContact.executeUpdate();
                        log.finest("Update BP Contact = " + no);
                    } catch (SQLException ex) {
                        log.finest("Update BP Contact - " + ex.toString());
                        conn.rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_BPartner i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Update BP Contact: " + ex.toString())).append(" WHERE I_BPartner_ID=").append(I_BPartner_ID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                } else if (newContact) {
                    try {
                        AD_User_ID = DB.getNextID(m_AD_Client_ID, "AD_User", null);
                        if (AD_User_ID <= 0) {
                            throw new DBException("No NextID (" + AD_User_ID + ")");
                        }
                        pstmt_insertBPContact.setInt(1, AD_User_ID);
                        pstmt_insertBPContact.setInt(2, C_BPartner_ID);
                        if (C_BPartner_Location_ID == 0) {
                            pstmt_insertBPContact.setNull(3, Types.NUMERIC);
                        } else {
                            pstmt_insertBPContact.setInt(3, C_BPartner_Location_ID);
                        }
                        pstmt_insertBPContact.setInt(4, I_BPartner_ID);
                        no = pstmt_insertBPContact.executeUpdate();
                        log.finest("Insert BP Contact = " + no);
                    } catch (Exception ex) {
                        log.finest("Insert BPContact - " + ex.toString());
                        conn.rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_BPartner i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert BPContact: " + ex.toString())).append(" WHERE I_BPartner_ID=").append(I_BPartner_ID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                }
                pstmt_setImported.setInt(1, C_BPartner_ID);
                if (C_BPartner_Location_ID == 0) {
                    pstmt_setImported.setNull(2, Types.NUMERIC);
                } else {
                    pstmt_setImported.setInt(2, C_BPartner_Location_ID);
                }
                if (AD_User_ID == 0) {
                    pstmt_setImported.setNull(3, Types.NUMERIC);
                } else {
                    pstmt_setImported.setInt(3, AD_User_ID);
                }
                pstmt_setImported.setInt(4, I_BPartner_ID);
                no = pstmt_setImported.executeUpdate();
                conn.commit();
            }
            rs.close();
            pstmt.close();
            pstmt_updateBPartner.close();
            pstmt_insertLocation.close();
            pstmt_insertBPLocation.close();
            pstmt_insertBPContact.close();
            pstmt_updateBPContact.close();
            pstmt_setImported.close();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.close();
                }
                conn = null;
            } catch (SQLException ex) {
            }
            throw new Exception("ImportBPartner.doIt", e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            conn = null;
        }
        sql = new StringBuffer("UPDATE I_BPartner " + "SET I_IsImported='N', Updated=current_timestamp " + "WHERE I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        addLog(0, null, new BigDecimal(no), "@Errors@");
        addLog(0, null, new BigDecimal(noInsert), "@C_BPartner_ID@: @Inserted@");
        addLog(0, null, new BigDecimal(noUpdate), "@C_BPartner_ID@: @Updated@");
        return "";
    }
