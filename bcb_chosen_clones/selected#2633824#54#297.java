    protected String doIt() throws java.lang.Exception {
        StringBuffer sql = null;
        int no = 0;
        String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;
        String[] strFields = new String[] { "Value", "Name", "Description", "DocumentNote", "Help", "UPC", "SKU", "Classification", "ProductType", "Discontinued", "DiscontinuedBy", "ImageURL", "DescriptionURL" };
        for (int i = 0; i < strFields.length; i++) {
            sql = new StringBuffer("UPDATE I_PRODUCT i " + "SET ").append(strFields[i]).append(" = (SELECT ").append(strFields[i]).append(" FROM M_Product p" + " WHERE i.M_Product_ID=p.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID) " + "WHERE M_Product_ID IS NOT NULL" + " AND EXISTS (SELECT * FROM M_Product p WHERE " + strFields[i] + " IS NOT NULL AND p.M_Product_ID = i.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID)" + " AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + strFields[i] + " - default from existing Product=" + no);
            }
        }
        String[] numFields = new String[] { "C_UOM_ID", "M_Product_Category_ID", "Volume", "Weight", "ShelfWidth", "ShelfHeight", "ShelfDepth", "UnitsPerPallet", "M_Product_Family_ID" };
        for (int i = 0; i < numFields.length; i++) {
            sql = new StringBuffer("UPDATE I_PRODUCT i " + "SET ").append(numFields[i]).append(" = (SELECT ").append(numFields[i]).append(" FROM M_Product p" + " WHERE i.M_Product_ID=p.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID) " + "WHERE M_Product_ID IS NOT NULL" + " AND EXISTS (SELECT * FROM M_Product p WHERE " + numFields[i] + " IS NOT NULL AND p.M_Product_ID = i.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID)" + " AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + numFields[i] + " default from existing Product=" + no);
            }
        }
        String[] strFieldsPO = new String[] { "UPC", "PriceEffective", "VendorProductNo", "VendorCategory", "Manufacturer", "Discontinued", "DiscontinuedBy" };
        for (int i = 0; i < strFieldsPO.length; i++) {
            sql = new StringBuffer("UPDATE I_PRODUCT i " + "SET ").append(strFieldsPO[i]).append(" = (SELECT ").append(strFieldsPO[i]).append(" FROM M_Product_PO p" + " WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) " + "WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL" + " AND EXISTS (SELECT * FROM M_Product_PO p WHERE " + strFieldsPO[i] + " IS NOT NULL AND p.M_Product_ID = i.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID)" + " AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + strFieldsPO[i] + " default from existing Product PO=" + no);
            }
        }
        String[] numFieldsPO = new String[] { "C_UOM_ID", "C_Currency_ID", "RoyaltyAmt", "Order_Min", "Order_Pack", "CostPerOrder", "DeliveryTime_Promised" };
        for (int i = 0; i < numFieldsPO.length; i++) {
            sql = new StringBuffer("UPDATE I_PRODUCT i " + "SET ").append(numFieldsPO[i]).append(" = (SELECT ").append(numFieldsPO[i]).append(" FROM M_Product_PO p" + " WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) " + "WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL" + " AND EXISTS (SELECT * FROM M_Product_PO p WHERE " + numFieldsPO[i] + " IS NOT NULL AND p.M_Product_ID = i.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID)" + " AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + numFieldsPO[i] + " default from existing Product PO=" + no);
            }
        }
        numFieldsPO = new String[] { "PriceList", "PricePO" };
        for (int i = 0; i < numFieldsPO.length; i++) {
            sql = new StringBuffer("UPDATE I_PRODUCT i " + "SET ").append(numFieldsPO[i]).append(" = (SELECT ").append(numFieldsPO[i]).append(" FROM M_Product_PO p" + " WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) " + "WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL" + " AND (").append(numFieldsPO[i]).append(" IS NULL OR ").append(numFieldsPO[i]).append("=0)" + " AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + numFieldsPO[i] + " default from existing Product PO=" + no);
            }
        }
        sql = new StringBuffer("UPDATE I_Product i " + "SET X12DE355 = " + "(SELECT X12DE355 FROM C_UOM u WHERE u.IsDefault='Y' AND u.AD_Client_ID IN (0,i.AD_Client_ID) AND ROWNUM=1) " + "WHERE X12DE355 IS NULL AND C_UOM_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("doIt - Set UOM Default=" + no);
        sql = new StringBuffer("UPDATE I_Product i " + "SET C_UOM_ID = (SELECT C_UOM_ID FROM C_UOM u WHERE u.X12DE355=i.X12DE355 AND u.AD_Client_ID IN (0,i.AD_Client_ID)) " + "WHERE C_UOM_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.info("doIt - Set UOM=" + no);
        sql = new StringBuffer("UPDATE I_Product " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid UOM, ' " + "WHERE C_UOM_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.fine("doIt - Invalid UOM=" + no);
        }
        sql = new StringBuffer("UPDATE I_Product " + "SET ProductCategory_Value=(SELECT Value FROM M_Product_Category" + " WHERE IsDefault='Y' AND AD_Client_ID=").append(m_AD_Client_ID).append(" AND ROWNUM=1) " + "WHERE ProductCategory_Value IS NULL AND M_Product_Category_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("doIt - Set Category Default=" + no);
        sql = new StringBuffer("UPDATE I_Product i " + "SET M_Product_Category_ID=(SELECT M_Product_Category_ID FROM M_Product_Category c" + " WHERE i.ProductCategory_Value=c.Value AND i.AD_Client_ID=c.AD_Client_ID) " + "WHERE M_Product_Category_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.info("doIt - Set Category=" + no);
        sql = new StringBuffer("UPDATE I_Product " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid ProdCategorty,' " + "WHERE M_Product_Category_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.fine("doIt - Invalid Category=" + no);
        }
        sql = new StringBuffer("UPDATE I_Product i " + "SET ISO_Code=(SELECT ISO_Code FROM C_Currency c" + " INNER JOIN C_AcctSchema a ON (a.C_Currency_ID=c.C_Currency_ID)" + " INNER JOIN AD_ClientInfo fo ON (a.C_AcctSchema_ID=fo.C_AcctSchema1_ID)" + " WHERE fo.AD_Client_ID=i.AD_Client_ID) " + "WHERE C_Currency_ID IS NULL AND ISO_Code IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("doIt - Set Currency Default=" + no);
        sql = new StringBuffer("UPDATE I_Product i " + "SET C_Currency_ID=(SELECT C_Currency_ID FROM C_Currency c" + " WHERE i.ISO_Code=c.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID)) " + "WHERE C_Currency_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.info("doIt- Set Currency=" + no);
        sql = new StringBuffer("UPDATE I_Product " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Currency,' " + "WHERE C_Currency_ID IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.fine("doIt - Invalid Currency=" + no);
        }
        sql = new StringBuffer("UPDATE I_Product " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid ProductType,' " + "WHERE ProductType NOT IN ('I','S')" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.fine("doIt - Invalid ProductType=" + no);
        }
        sql = new StringBuffer("UPDATE I_Product i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Value not unique,' " + "WHERE I_IsImported<>'Y'" + " AND Value IN (SELECT Value FROM I_Product pr WHERE i.AD_Client_ID=pr.AD_Client_ID GROUP BY Value HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.warning("doIt - Not Unique Value=" + no);
        }
        sql = new StringBuffer("UPDATE I_Product i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=UPC not unique,' " + "WHERE I_IsImported<>'Y'" + " AND UPC IN (SELECT UPC FROM I_Product pr WHERE i.AD_Client_ID=pr.AD_Client_ID GROUP BY UPC HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.warning("doIt - Not Unique UPC=" + no);
        }
        sql = new StringBuffer("UPDATE I_Product i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Mandatory Value,' " + "WHERE Value IS NULL" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.warning("doIt - No Mandatory Value=" + no);
        }
        sql = new StringBuffer("UPDATE I_Product " + "SET VendorProductNo=Value " + "WHERE C_BPartner_ID IS NOT NULL AND VendorProductNo IS NULL" + " AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.info("doIt - VendorProductNo Set to Value=" + no);
        sql = new StringBuffer("UPDATE I_Product i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=VendorProductNo not unique,' " + "WHERE I_IsImported<>'Y'" + " AND C_BPartner_ID IS NOT NULL" + " AND (C_BPartner_ID, VendorProductNo) IN " + " (SELECT C_BPartner_ID, VendorProductNo FROM I_Product pr WHERE i.AD_Client_ID=pr.AD_Client_ID GROUP BY C_BPartner_ID, VendorProductNo HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.warning("doIt - Not Unique VendorProductNo=" + no);
        }
        int C_TaxCategory_ID = 0;
        try {
            PreparedStatement pstmt = DB.prepareStatement("SELECT C_TaxCategory_ID FROM C_TaxCategory WHERE IsDefault='Y'" + clientCheck);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                C_TaxCategory_ID = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new Exception("doIt - TaxCategory", e);
        }
        log.fine("doIt - C_TaxCategory_ID=" + C_TaxCategory_ID);
        int noInsert = 0;
        int noUpdate = 0;
        int noInsertPO = 0;
        int noUpdatePO = 0;
        log.fine("doIt - start inserting/updating ...");
        sql = new StringBuffer("SELECT I_Product_ID, M_Product_ID, C_BPartner_ID " + "FROM I_Product WHERE I_IsImported='N'").append(clientCheck);
        Connection conn = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
        try {
            PreparedStatement pstmt_insertProduct = conn.prepareStatement("INSERT INTO M_Product (M_Product_ID," + "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy," + "Value,Name,Description,DocumentNote,Help," + "UPC,SKU,C_UOM_ID,IsSummary,M_Product_Category_ID,C_TaxCategory_ID," + "ProductType,ImageURL,DescriptionURL,M_Product_Family_ID) " + "SELECT ?," + "AD_Client_ID,AD_Org_ID,'Y',CURRENT_TIMESTAMP,CreatedBy,CURRENT_TIMESTAMP,UpdatedBy," + "Value,Name,Description,DocumentNote,Help," + "UPC,SKU,C_UOM_ID,'N',M_Product_Category_ID," + C_TaxCategory_ID + "," + "ProductType,ImageURL,DescriptionURL,M_Product_Category_ID " + "FROM I_Product " + "WHERE I_Product_ID=?");
            PreparedStatement pstmt_updateProduct = conn.prepareStatement("UPDATE M_PRODUCT " + "SET Value=aux.value" + ",Name=aux.Name" + ",Description=aux.Description" + ",DocumentNote=aux.DocumentNote" + ",Help=aux.Help" + ",UPC=aux.UPC" + ",SKU=aux.SKU" + ",C_UOM_ID=aux.C_UOM_ID" + ",M_Product_Category_ID=aux.M_Product_Category_ID" + ",Classification=aux.Classification" + ",ProductType=aux.ProductType" + ",Volume=aux.Volume" + ",Weight=aux.Weight" + ",ShelfWidth=aux.ShelfWidth" + ",ShelfHeight=aux.ShelfHeight" + ",ShelfDepth=aux.ShelfDepth" + ",UnitsPerPallet=aux.UnitsPerPallet" + ",Discontinued=aux.Discontinued" + ",DiscontinuedBy=aux.DiscontinuedBy" + ",Updated=current_timestamp" + ",UpdatedBy=aux.UpdatedBy" + " from (SELECT Value,Name,Description,DocumentNote,Help,UPC,SKU,C_UOM_ID,M_Product_Category_ID,Classification,ProductType,Volume,Weight,ShelfWidth,ShelfHeight,ShelfDepth,UnitsPerPallet,Discontinued,DiscontinuedBy,UpdatedBy FROM I_Product WHERE I_Product_ID=?) as aux" + " WHERE M_Product_ID=?");
            PreparedStatement pstmt_updateProductPO = conn.prepareStatement("UPDATE M_Product_PO " + "SET IsCurrentVendor='Y'" + ",C_UOM_ID=aux1.C_UOM_ID" + ",C_Currency_ID=aux1.C_Currency_ID" + ",UPC=aux1.UPC" + ",PriceList=aux1.PriceList" + ",PricePO=aux1.PricePO" + ",RoyaltyAmt=aux1.RoyaltyAmt" + ",PriceEffective=aux1.PriceEffective" + ",VendorProductNo=aux1.VendorProductNo" + ",VendorCategory=aux1.VendorCategory" + ",Manufacturer=aux1.Manufacturer" + ",Discontinued=aux1.Discontinued" + ",DiscontinuedBy=aux1.DiscontinuedBy" + ",Order_Min=aux1.Order_Min" + ",Order_Pack=aux1.Order_Pack" + ",CostPerOrder=aux1.CostPerOrder" + ",DeliveryTime_Promised=aux1.DeliveryTime_Promised" + ",Updated=current_timestamp" + ",UpdatedBy=aux1.UpdatedBy" + " from (SELECT 'Y',C_UOM_ID,C_Currency_ID,UPC,PriceList,PricePO,RoyaltyAmt,PriceEffective,VendorProductNo,VendorCategory,Manufacturer,Discontinued,DiscontinuedBy,Order_Min,Order_Pack,CostPerOrder,DeliveryTime_Promised,UpdatedBy FROM I_Product WHERE I_Product_ID=?) as aux1" + " WHERE M_Product_ID=? AND C_BPartner_ID=?");
            PreparedStatement pstmt_insertProductPO = conn.prepareStatement("INSERT INTO M_Product_PO (M_Product_ID,C_BPartner_ID, " + "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy," + "IsCurrentVendor,C_UOM_ID,C_Currency_ID,UPC," + "PriceList,PricePO,RoyaltyAmt,PriceEffective," + "VendorProductNo,VendorCategory,Manufacturer," + "Discontinued,DiscontinuedBy,Order_Min,Order_Pack," + "CostPerOrder,DeliveryTime_Promised) " + "SELECT ?,?, " + "AD_Client_ID,AD_Org_ID,'Y',d,CreatedBy,CURRENT_TIMESTAMP,UpdatedBy," + "'Y',C_UOM_ID,C_Currency_ID,UPC," + "PriceList,PricePO,RoyaltyAmt,PriceEffective," + "VendorProductNo,VendorCategory,Manufacturer," + "Discontinued,DiscontinuedBy,Order_Min,Order_Pack," + "CostPerOrder,DeliveryTime_Promised " + "FROM I_Product " + "WHERE I_Product_ID=?");
            PreparedStatement pstmt_setImported = conn.prepareStatement("UPDATE I_Product SET I_IsImported='Y', M_Product_ID=?, " + "Updated=CURRENT_TIMESTAMP, Processed='Y' WHERE I_Product_ID=?");
            PreparedStatement pstmt = DB.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int I_Product_ID = rs.getInt(1);
                int M_Product_ID = rs.getInt(2);
                int C_BPartner_ID = rs.getInt(3);
                boolean newProduct = M_Product_ID == 0;
                log.fine("I_Product_ID=" + I_Product_ID + ", M_Product_ID=" + M_Product_ID + ", C_BPartner_ID=" + C_BPartner_ID);
                if (newProduct) {
                    M_Product_ID = DB.getNextID(m_AD_Client_ID, "M_Product", null);
                    pstmt_insertProduct.setInt(1, M_Product_ID);
                    pstmt_insertProduct.setInt(2, I_Product_ID);
                    try {
                        no = pstmt_insertProduct.executeUpdate();
                        log.finer("Insert Product = " + no);
                        noInsert++;
                    } catch (SQLException ex) {
                        log.warning("Insert Product - " + ex.toString());
                        sql = new StringBuffer("UPDATE I_Product i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert Product: " + ex.toString())).append("WHERE I_Product_ID=").append(I_Product_ID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                } else {
                    pstmt_updateProduct.setInt(1, I_Product_ID);
                    pstmt_updateProduct.setInt(2, M_Product_ID);
                    try {
                        no = pstmt_updateProduct.executeUpdate();
                        log.finer("Update Product = " + no);
                        noUpdate++;
                    } catch (SQLException ex) {
                        log.warning("Update Product - " + ex.toString());
                        sql = new StringBuffer("UPDATE I_Product i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Update Product: " + ex.toString())).append("WHERE I_Product_ID=").append(I_Product_ID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                }
                if (C_BPartner_ID != 0) {
                    no = 0;
                    if (!newProduct) {
                        pstmt_updateProductPO.setInt(1, I_Product_ID);
                        pstmt_updateProductPO.setInt(2, M_Product_ID);
                        pstmt_updateProductPO.setInt(3, C_BPartner_ID);
                        try {
                            no = pstmt_updateProductPO.executeUpdate();
                            log.finer("Update Product_PO = " + no);
                            noUpdatePO++;
                        } catch (SQLException ex) {
                            log.warning("Update Product_PO - " + ex.toString());
                            noUpdate--;
                            conn.rollback();
                            sql = new StringBuffer("UPDATE I_Product i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Update Product_PO: " + ex.toString())).append("WHERE I_Product_ID=").append(I_Product_ID);
                            DB.executeUpdate(sql.toString());
                            continue;
                        }
                    }
                    if (no == 0) {
                        pstmt_insertProductPO.setInt(1, M_Product_ID);
                        pstmt_insertProductPO.setInt(2, C_BPartner_ID);
                        pstmt_insertProductPO.setInt(3, I_Product_ID);
                        try {
                            no = pstmt_insertProductPO.executeUpdate();
                            log.finer("Insert Product_PO = " + no);
                            noInsertPO++;
                        } catch (SQLException ex) {
                            log.warning("Insert Product_PO - " + ex.toString());
                            noInsert--;
                            conn.rollback();
                            sql = new StringBuffer("UPDATE I_Product i " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert Product_PO: " + ex.toString())).append("WHERE I_Product_ID=").append(I_Product_ID);
                            DB.executeUpdate(sql.toString());
                            continue;
                        }
                    }
                }
                pstmt_setImported.setInt(1, M_Product_ID);
                pstmt_setImported.setInt(2, I_Product_ID);
                no = pstmt_setImported.executeUpdate();
                conn.commit();
            }
            rs.close();
            pstmt.close();
            pstmt_insertProduct.close();
            pstmt_updateProduct.close();
            pstmt_insertProductPO.close();
            pstmt_updateProductPO.close();
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
            log.log(Level.SEVERE, "doIt", e);
            throw new Exception("doIt", e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            conn = null;
        }
        sql = new StringBuffer("UPDATE I_Product " + "SET I_IsImported='N', Updated=CURRENT_TIMESTAMP " + "WHERE I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        StringBuffer infoReturn = new StringBuffer("");
        infoReturn.append("<tr><td>@Errors@</td><td>").append(no).append("</td></tr>");
        infoReturn.append("<tr><td>@M_Product_ID@: @Inserted@</td><td>").append(noInsert).append("</td></tr>");
        infoReturn.append("<tr><td>@M_Product_ID@: @Updated@</td><td>").append(noUpdate).append("</td></tr>");
        infoReturn.append("<tr><td>@M_Product_ID@ @Purchase@: @Inserted@</td><td>").append(noInsertPO).append("</td></tr>");
        infoReturn.append("<tr><td>@M_Product_ID@ @Purchase@: @Updated@</td><td>").append(noUpdatePO).append("</td></tr>");
        return infoReturn.toString();
    }
