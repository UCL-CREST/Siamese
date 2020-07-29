    private void processOrder() {
        double neg = 0d;
        if (intMode == MODE_CHECKOUT) {
            if (round2Places(mBuf.getBufferTotal()) >= round2Places(order.getOrderTotal())) {
                double cash, credit, allowedCredit = 0d;
                allowedCredit = getStudentCredit();
                if (settings.get(DBSettings.MAIN_ALLOWNEGBALANCES).compareTo("1") == 0) {
                    try {
                        neg = Double.parseDouble(settings.get(DBSettings.MAIN_MAXNEGBALANCE));
                    } catch (NumberFormatException ex) {
                        System.err.println("NumberFormatException::Potential problem with setting MAIN_MAXNEGBALANCE");
                        System.err.println("     * Note: If you enable negative balances, please don't leave this");
                        System.err.println("             blank.  At least set it to 0.  For right now we are setting ");
                        System.err.println("             the max negative balance to $0.00");
                        System.err.println("");
                        System.err.println("Exception Message:" + ex.getMessage());
                    }
                    if (neg < 0) neg *= -1;
                    allowedCredit += neg;
                }
                if (round2Places(mBuf.getCredit()) <= round2Places(allowedCredit)) {
                    if (round2Places(mBuf.getCredit()) > round2Places(getStudentCredit()) && !student.isStudentSet()) {
                        gui.setStatus("Can't allow negative balance on an anonymous student!", true);
                    } else {
                        if (round2Places(mBuf.getCredit()) > round2Places(order.getOrderTotal())) {
                            credit = round2Places(order.getOrderTotal());
                        } else {
                            credit = round2Places(mBuf.getCredit());
                        }
                        if ((mBuf.getCash() + credit) >= order.getOrderTotal()) {
                            cash = round2Places(order.getOrderTotal() - credit);
                            double change = round2Places(mBuf.getCash() - cash);
                            if (round2Places(cash + credit) == round2Places(order.getOrderTotal())) {
                                Connection conn = null;
                                Statement stmt = null;
                                ResultSet rs = null;
                                try {
                                    conn = dbMan.getPOSConnection();
                                    conn.setAutoCommit(false);
                                    stmt = conn.createStatement();
                                    String host = getHostName();
                                    String stuId = student.getStudentNumber();
                                    String building = settings.get(DBSettings.MAIN_BUILDING);
                                    String cashier = dbMan.getPOSUser();
                                    String strSql = "insert into " + strPOSPrefix + "trans_master ( tm_studentid, tm_total, tm_cashtotal, tm_credittotal, tm_building, tm_register, tm_cashier, tm_datetime, tm_change ) values( '" + stuId + "', '" + round2Places(order.getOrderTotal()) + "', '" + round2Places(cash) + "', '" + round2Places(credit) + "', '" + building + "', '" + host + "', '" + cashier + "', NOW(), '" + round2Places(change) + "')";
                                    int intSqlReturnVal = -1;
                                    int masterID = -1;
                                    try {
                                        intSqlReturnVal = stmt.executeUpdate(strSql, Statement.RETURN_GENERATED_KEYS);
                                        ResultSet keys = stmt.getGeneratedKeys();
                                        keys.next();
                                        masterID = keys.getInt(1);
                                        keys.close();
                                        stmt.close();
                                    } catch (Exception exRetKeys) {
                                        System.err.println(exRetKeys.getMessage() + " (but pscafepos is attempting a work around)");
                                        intSqlReturnVal = stmt.executeUpdate(strSql);
                                        masterID = dbMan.getLastInsertIDWorkAround(stmt, strPOSPrefix + "trans_master_tm_id_seq");
                                        if (masterID == -1) System.err.println("It looks like the work around failed, please submit a bug report!"); else System.err.println("work around was successful!");
                                    }
                                    if (intSqlReturnVal == 1) {
                                        if (masterID >= 0) {
                                            OrderItem[] itms = order.getOrderItems();
                                            if (itms != null && itms.length > 0) {
                                                for (int i = 0; i < itms.length; i++) {
                                                    if (itms[i] != null) {
                                                        stmt = conn.createStatement();
                                                        int itemid = itms[i].getDBID();
                                                        double itemprice = round2Places(itms[i].getEffectivePrice());
                                                        int f, r, a;
                                                        String strItemName, strItemBuilding, strItemCat;
                                                        f = 0;
                                                        r = 0;
                                                        a = 0;
                                                        if (itms[i].isSoldAsFree()) {
                                                            f = 1;
                                                        }
                                                        if (itms[i].isSoldAsReduced()) {
                                                            r = 1;
                                                        }
                                                        if (itms[i].isTypeA()) {
                                                            a = 1;
                                                        }
                                                        strItemName = itms[i].getName();
                                                        strItemBuilding = (String) itms[i].getBuilding();
                                                        strItemCat = itms[i].getCategory();
                                                        if (stmt.executeUpdate("insert into " + strPOSPrefix + "trans_item ( ti_itemid, ti_tmid, ti_pricesold, ti_registerid, ti_cashier, ti_studentid, ti_isfree, ti_isreduced, ti_datetime, ti_istypea, ti_itemname, ti_itembuilding, ti_itemcat  ) values('" + itemid + "', '" + masterID + "', '" + round2Places(itemprice) + "', '" + host + "', '" + cashier + "', '" + stuId + "', '" + f + "', '" + r + "', NOW(), '" + a + "', '" + strItemName + "', '" + strItemBuilding + "', '" + strItemCat + "')") != 1) {
                                                            gui.setCriticalMessage("Item insert failed");
                                                            conn.rollback();
                                                        }
                                                        stmt.close();
                                                        stmt = conn.createStatement();
                                                        String sqlInv = "SELECT inv_id from " + strPOSPrefix + "inventory where inv_menuid = " + itemid + "";
                                                        if (stmt.execute(sqlInv)) {
                                                            ResultSet rsInv = stmt.getResultSet();
                                                            int delId = -1;
                                                            if (rsInv.next()) {
                                                                delId = rsInv.getInt("inv_id");
                                                            }
                                                            if (delId != -1) {
                                                                stmt.executeUpdate("delete from " + strPOSPrefix + "inventory where inv_id = " + delId);
                                                            }
                                                            stmt.close();
                                                        }
                                                    } else {
                                                        gui.setCriticalMessage("Null Item");
                                                        conn.rollback();
                                                    }
                                                }
                                                boolean blOk = true;
                                                if (round2Places(credit) > 0d) {
                                                    if (round2Places(allowedCredit) >= round2Places(credit)) {
                                                        if (hasStudentCredit()) {
                                                            stmt = conn.createStatement();
                                                            if (stmt.executeUpdate("update " + strPOSPrefix + "studentcredit set credit_amount = credit_amount - " + round2Places(credit) + " where credit_active = '1' and credit_studentid = '" + stuId + "'") == 1) {
                                                                stmt.close();
                                                                stmt = conn.createStatement();
                                                                if (stmt.executeUpdate("update " + strPOSPrefix + "studentcredit set credit_lastused = NOW() where credit_active = '1' and credit_studentid = '" + stuId + "'") == 1) {
                                                                    stmt.close();
                                                                    stmt = conn.createStatement();
                                                                    if (stmt.executeUpdate("insert into " + strPOSPrefix + "studentcredit_log ( scl_studentid, scl_action, scl_transid, scl_datetime ) values( '" + stuId + "', '" + round2Places((-1) * credit) + "', '" + masterID + "', NOW() )") == 1) {
                                                                        stmt.close();
                                                                        blOk = true;
                                                                    } else {
                                                                        gui.setCriticalMessage("Unable to update student credit log.");
                                                                        blOk = false;
                                                                    }
                                                                } else {
                                                                    gui.setCriticalMessage("Unable to update student credit account.");
                                                                    blOk = false;
                                                                }
                                                            } else {
                                                                gui.setCriticalMessage("Unable to update student credit account.");
                                                                blOk = false;
                                                            }
                                                        } else {
                                                            stmt = conn.createStatement();
                                                            if (stmt.executeUpdate("insert into " + strPOSPrefix + "studentcredit (credit_amount,credit_active,credit_studentid,credit_lastused) values('" + round2Places((-1) * credit) + "','1','" + stuId + "', NOW())") == 1) {
                                                                stmt.close();
                                                                stmt = conn.createStatement();
                                                                if (stmt.executeUpdate("insert into " + strPOSPrefix + "studentcredit_log ( scl_studentid, scl_action, scl_transid, scl_datetime ) values( '" + stuId + "', '" + round2Places((-1) * credit) + "', '" + masterID + "', NOW() )") == 1) {
                                                                    stmt.close();
                                                                    blOk = true;
                                                                } else {
                                                                    gui.setCriticalMessage("Unable to update student credit log.");
                                                                    blOk = false;
                                                                }
                                                            } else {
                                                                gui.setCriticalMessage("Unable to create new student credit account.");
                                                                blOk = false;
                                                            }
                                                        }
                                                    } else {
                                                        gui.setCriticalMessage("Student doesn't have enought credit.");
                                                        blOk = false;
                                                    }
                                                }
                                                if (blOk) {
                                                    if (blDepositCredit && change > 0d) {
                                                        try {
                                                            if (doStudentCreditUpdate(change, stuId)) {
                                                                change = 0d;
                                                            } else blOk = false;
                                                        } catch (Exception cExp) {
                                                            blOk = false;
                                                        }
                                                    }
                                                }
                                                if (blOk) {
                                                    boolean blHBOK = true;
                                                    if (itms != null && itms.length > 0) {
                                                        for (int i = 0; i < itms.length; i++) {
                                                            stmt = conn.createStatement();
                                                            if (stmt.execute("select count(*) from " + strPOSPrefix + "hotbar where hb_itemid = '" + itms[i].getDBID() + "' and hb_building = '" + building + "' and hb_register = '" + host + "' and hb_cashier = '" + cashier + "'")) {
                                                                rs = stmt.getResultSet();
                                                                rs.next();
                                                                int num = rs.getInt(1);
                                                                stmt.close();
                                                                if (num == 1) {
                                                                    stmt = conn.createStatement();
                                                                    if (stmt.executeUpdate("update " + strPOSPrefix + "hotbar set hb_count = hb_count + 1 where hb_itemid = '" + itms[i].getDBID() + "' and hb_building = '" + building + "' and hb_register = '" + host + "' and hb_cashier = '" + cashier + "'") != 1) blHBOK = false;
                                                                } else {
                                                                    stmt = conn.createStatement();
                                                                    if (stmt.executeUpdate("insert into " + strPOSPrefix + "hotbar ( hb_itemid, hb_building, hb_register, hb_cashier, hb_count ) values( '" + itms[i].getDBID() + "', '" + building + "', '" + host + "', '" + cashier + "', '1' )") != 1) blHBOK = false;
                                                                }
                                                                stmt.close();
                                                            }
                                                        }
                                                    } else blHBOK = false;
                                                    if (blHBOK) {
                                                        conn.commit();
                                                        gui.setStatus("Order Complete.");
                                                        gui.disableUI();
                                                        summary = new PSOrderSummary(gui);
                                                        if (cashDrawer != null) cashDrawer.openDrawer(); else summary.setPOSEventListener(this);
                                                        summary.display(money.format(order.getOrderTotal()), money.format(mBuf.getCash()), money.format(credit), money.format(change), money.format(getStudentCredit()));
                                                    } else {
                                                        conn.rollback();
                                                        gui.setStatus("Failure during Hotbar update.  Transaction has been rolled back.", true);
                                                    }
                                                } else {
                                                    conn.rollback();
                                                }
                                            } else {
                                                gui.setCriticalMessage("Unable to fetch items.");
                                                conn.rollback();
                                            }
                                        } else {
                                            gui.setCriticalMessage("Unable to retrieve autoid");
                                            conn.rollback();
                                        }
                                    } else {
                                        gui.setCriticalMessage("Error During Writting of Transaction Master Record.");
                                        conn.rollback();
                                    }
                                } catch (SQLException sqlEx) {
                                    System.err.println("SQLException: " + sqlEx.getMessage());
                                    System.err.println("SQLState: " + sqlEx.getSQLState());
                                    System.err.println("VendorError: " + sqlEx.getErrorCode());
                                    try {
                                        conn.rollback();
                                    } catch (SQLException sqlEx2) {
                                        System.err.println("Rollback failed: " + sqlEx2.getMessage());
                                    }
                                } catch (Exception e) {
                                    System.err.println("Exception: " + e.getMessage());
                                    System.err.println(e);
                                    try {
                                        conn.rollback();
                                    } catch (SQLException sqlEx2) {
                                        System.err.println("Rollback failed: " + sqlEx2.getMessage());
                                    }
                                } finally {
                                    if (rs != null) {
                                        try {
                                            rs.close();
                                        } catch (SQLException sqlEx) {
                                            rs = null;
                                        }
                                        if (stmt != null) {
                                            try {
                                                stmt.close();
                                            } catch (SQLException sqlEx) {
                                                stmt = null;
                                            } catch (Exception e) {
                                                System.err.println("Exception: " + e.getMessage());
                                                System.err.println(e);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            gui.setStatus("Credit total + Cash total is less then the order total! ", true);
                        }
                    }
                } else {
                    if (settings.get(DBSettings.MAIN_ALLOWNEGBALANCES).compareTo("1") == 0) {
                        gui.setStatus("Sorry, maximum negative balance is " + money.format(neg) + "!", true);
                    } else gui.setStatus("Student does not have enough credit to process this order.", true);
                }
            } else {
                gui.setStatus("Buffer total is less then the order total.", true);
            }
        }
    }
