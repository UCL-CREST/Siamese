    public SukuData updatePerson(String usertext, SukuData req) {
        String insPers;
        String userid = Utils.toUsAscii(usertext);
        if (userid != null && userid.length() > 16) {
            userid = userid.substring(0, 16);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("insert into unit (pid,tag,privacy,groupid,sex,sourcetext,privatetext,userrefn");
        if (userid != null) {
            sb.append(",createdby");
        }
        sb.append(") values (?,?,?,?,?,?,?,? ");
        if (userid != null) {
            sb.append(",'" + userid + "'");
        }
        sb.append(")");
        insPers = sb.toString();
        String updPers;
        sb = new StringBuilder();
        sb.append("update unit set privacy=?,groupid=?,sex=?,sourcetext=?," + "privatetext=?,userrefn=?,Modified=now()");
        if (userid != null) {
            sb.append(",modifiedby = '" + userid + "' where pid = ?");
        } else {
            sb.append(" where pid = ?");
        }
        updPers = sb.toString();
        sb = new StringBuilder();
        String updSql;
        sb.append("update unitnotice set ");
        sb.append("surety=?,Privacy=?,NoticeType=?,Description=?,");
        sb.append("DatePrefix=?,FromDate=?,ToDate=?,Place=?,");
        sb.append("Village=?,Farm=?,Croft=?,Address=?,");
        sb.append("PostalCode=?,PostOffice=?,State=?,Country=?,Email=?,");
        sb.append("NoteText=?,MediaFilename=?,MediaTitle=?,Prefix=?,");
        sb.append("Surname=?,Givenname=?,Patronym=?,PostFix=?,");
        sb.append("SourceText=?,PrivateText=?,RefNames=?,RefPlaces=?,Modified=now()");
        if (userid != null) {
            sb.append(",modifiedby = '" + userid + "'");
        }
        sb.append(" where pnid = ?");
        updSql = sb.toString();
        sb = new StringBuilder();
        String insSql;
        sb.append("insert into unitnotice  (");
        sb.append("surety,Privacy,NoticeType,Description,");
        sb.append("DatePrefix,FromDate,ToDate,Place,");
        sb.append("Village,Farm,Croft,Address,");
        sb.append("PostalCode,PostOffice,State,Country,Email,");
        sb.append("NoteText,MediaFilename,MediaTitle,Prefix,");
        sb.append("Surname,Givenname,Patronym,PostFix,");
        sb.append("SourceText,PrivateText,RefNames,Refplaces,pnid,pid,tag");
        if (userid != null) {
            sb.append(",createdby");
        }
        sb.append(") values (");
        sb.append("?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,");
        sb.append("?,?,?,?,?,?,?,?");
        if (userid != null) {
            sb.append(",'" + userid + "'");
        }
        sb.append(")");
        insSql = sb.toString();
        sb = new StringBuilder();
        String updLangSql;
        sb.append("update unitlanguage set ");
        sb.append("NoticeType=?,Description=?," + "Place=?,");
        sb.append("NoteText=?,MediaTitle=?,Modified=now() ");
        if (userid != null) {
            sb.append(",modifiedby = '" + userid + "'");
        }
        sb.append("where pnid=? and langCode = ?");
        updLangSql = sb.toString();
        sb = new StringBuilder();
        String insLangSql;
        sb.append("insert into unitlanguage (pnid,pid,tag,langcode,");
        sb.append("NoticeType,Description,Place,");
        sb.append("NoteText,MediaTitle");
        if (userid != null) {
            sb.append(",createdby");
        }
        sb.append(") values (?,?,?,?,?,?,?,?,?");
        if (userid != null) {
            sb.append(",'" + userid + "'");
        }
        sb.append(")");
        insLangSql = sb.toString();
        String delOneLangSql = "delete from unitlanguage where pnid = ? and langcode = ? ";
        String updRowSql = "update unitnotice set noticerow = ? where pnid = ? ";
        String delSql = "delete from unitnotice where pnid = ? ";
        String delAllLangSql = "delete from Unitlanguage where pnid = ? ";
        SukuData res = new SukuData();
        UnitNotice[] nn = req.persLong.getNotices();
        if (nn != null) {
            String prevName = "";
            String prevOccu = "";
            for (int i = 0; i < nn.length; i++) {
                if (nn[i].getTag().equals("NAME")) {
                    String thisName = Utils.nv(nn[i].getGivenname()) + "/" + Utils.nv(nn[i].getPatronym()) + "/" + Utils.nv(nn[i].getPrefix()) + "/" + Utils.nv(nn[i].getSurname()) + "/" + Utils.nv(nn[i].getPostfix());
                    if (thisName.equals(prevName) && !prevName.equals("")) {
                        if (nn[i].isToBeDeleted() == false) {
                            String e = Resurses.getString("IDENTICAL_NAMES_ERROR") + " [" + req.persLong.getPid() + "] idx [" + i + "] = " + thisName;
                            logger.warning(e);
                            if (req.persLong.getPid() > 0) {
                                res.resu = e;
                                return res;
                            }
                        }
                    }
                    prevName = thisName;
                } else if (nn[i].getTag().equals("OCCU")) {
                    String thisOccu = Utils.nv(nn[i].getDescription());
                    if (thisOccu.equals(prevOccu) && !prevOccu.equals("")) {
                        if (nn[i].isToBeDeleted() == false) {
                            String e = Resurses.getString("IDENTICAL_OCCU_ERROR") + " [" + req.persLong.getPid() + "] idx [" + i + "] = '" + thisOccu + "'";
                            logger.warning(e);
                            if (req.persLong.getPid() > 0) {
                                res.resu = e;
                                return res;
                            }
                        }
                    }
                    prevOccu = thisOccu;
                }
            }
        }
        int pid = 0;
        try {
            con.setAutoCommit(false);
            Statement stm;
            PreparedStatement pst;
            if (req.persLong.getPid() > 0) {
                res.resultPid = req.persLong.getPid();
                pid = req.persLong.getPid();
                if (req.persLong.isMainModified()) {
                    if (req.persLong.getModified() == null) {
                        pst = con.prepareStatement(updPers + " and modified is null ");
                    } else {
                        pst = con.prepareStatement(updPers + " and modified = ?");
                    }
                    pst.setString(1, req.persLong.getPrivacy());
                    pst.setString(2, req.persLong.getGroupId());
                    pst.setString(3, req.persLong.getSex());
                    pst.setString(4, req.persLong.getSource());
                    pst.setString(5, req.persLong.getPrivateText());
                    pst.setString(6, req.persLong.getRefn());
                    pst.setInt(7, req.persLong.getPid());
                    if (req.persLong.getModified() != null) {
                        pst.setTimestamp(8, req.persLong.getModified());
                    }
                    int lukuri = pst.executeUpdate();
                    if (lukuri != 1) {
                        logger.warning("Person update for pid " + pid + " failed [" + lukuri + "] (Should be 1)");
                        throw new SQLException("TRANSACTION_ERROR_1");
                    }
                    String apara = null;
                    String bpara = null;
                    String cpara = null;
                    String dpara = null;
                    if (req.persLong.getSex().equals("M")) {
                        apara = "FATH";
                        bpara = "MOTH";
                        cpara = "HUSB";
                        dpara = "WIFE";
                    } else if (req.persLong.getSex().equals("F")) {
                        bpara = "FATH";
                        apara = "MOTH";
                        dpara = "HUSB";
                        cpara = "WIFE";
                    }
                    if (apara != null) {
                        String sqlParent = "update relation as b set tag=? " + "where b.rid in (select a.rid from relation as a " + "where a.pid = ? and a.pid <> b.rid and a.tag='CHIL')	" + "and tag=?";
                        PreparedStatement ppare = con.prepareStatement(sqlParent);
                        ppare.setString(1, apara);
                        ppare.setInt(2, req.persLong.getPid());
                        ppare.setString(3, bpara);
                        int resup = ppare.executeUpdate();
                        logger.fine("updated count for person parent= " + resup);
                        String sqlSpouse = "update relation as b set tag=? " + "where b.rid in (select a.rid " + "from relation as a where a.pid = ? and a.pid <> b.pid " + "and a.tag in ('HUSB','WIFE')) and tag=?";
                        ppare = con.prepareStatement(sqlSpouse);
                        ppare.setString(1, cpara);
                        ppare.setInt(2, req.persLong.getPid());
                        ppare.setString(3, dpara);
                        resup = ppare.executeUpdate();
                        logger.fine("updated count for person spouse= " + resup);
                    }
                }
            } else {
                stm = con.createStatement();
                ResultSet rs = stm.executeQuery("select nextval('unitseq')");
                if (rs.next()) {
                    pid = rs.getInt(1);
                    res.resultPid = pid;
                } else {
                    throw new SQLException("Sequence unitseq error");
                }
                rs.close();
                pst = con.prepareStatement(insPers);
                pst.setInt(1, pid);
                pst.setString(2, req.persLong.getTag());
                pst.setString(3, req.persLong.getPrivacy());
                pst.setString(4, req.persLong.getGroupId());
                pst.setString(5, req.persLong.getSex());
                pst.setString(6, req.persLong.getSource());
                pst.setString(7, req.persLong.getPrivateText());
                pst.setString(8, req.persLong.getRefn());
                int lukuri = pst.executeUpdate();
                if (lukuri != 1) {
                    logger.warning("Person created for pid " + pid + "  gave result " + lukuri);
                }
            }
            PreparedStatement pstDel = con.prepareStatement(delSql);
            PreparedStatement pstDelLang = con.prepareStatement(delAllLangSql);
            PreparedStatement pstUpdRow = con.prepareStatement(updRowSql);
            if (nn != null) {
                for (int i = 0; i < nn.length; i++) {
                    UnitNotice n = nn[i];
                    int pnid = 0;
                    if (n.isToBeDeleted()) {
                        pstDelLang.setInt(1, n.getPnid());
                        int landelcnt = pstDelLang.executeUpdate();
                        pstDel.setInt(1, n.getPnid());
                        int delcnt = pstDel.executeUpdate();
                        if (delcnt != 1) {
                            logger.warning("Person notice [" + n.getTag() + "]delete for pid " + pid + " failed [" + delcnt + "] (Should be 1)");
                            throw new SQLException("TRANSACTION_ERROR_2");
                        }
                        String text = "Poistettiin " + delcnt + " riviä [" + landelcnt + "] kieliversiota pid = " + n.getPid() + " tag=" + n.getTag();
                        logger.fine(text);
                    } else if (n.getPnid() == 0 || n.isToBeUpdated()) {
                        if (n.getPnid() == 0) {
                            stm = con.createStatement();
                            ResultSet rs = stm.executeQuery("select nextval('unitnoticeseq')");
                            if (rs.next()) {
                                pnid = rs.getInt(1);
                            } else {
                                throw new SQLException("Sequence unitnoticeseq error");
                            }
                            rs.close();
                            pst = con.prepareStatement(insSql);
                        } else {
                            if (n.getModified() == null) {
                                pst = con.prepareStatement(updSql + " and modified is null ");
                            } else {
                                pst = con.prepareStatement(updSql + " and modified = ?");
                            }
                            pnid = n.getPnid();
                        }
                        if (n.isToBeUpdated() || n.getPnid() == 0) {
                            pst.setInt(1, n.getSurety());
                            pst.setString(2, n.getPrivacy());
                            pst.setString(3, n.getNoticeType());
                            pst.setString(4, n.getDescription());
                            pst.setString(5, n.getDatePrefix());
                            pst.setString(6, n.getFromDate());
                            pst.setString(7, n.getToDate());
                            pst.setString(8, n.getPlace());
                            pst.setString(9, n.getVillage());
                            pst.setString(10, n.getFarm());
                            pst.setString(11, n.getCroft());
                            pst.setString(12, n.getAddress());
                            pst.setString(13, n.getPostalCode());
                            pst.setString(14, n.getPostOffice());
                            pst.setString(15, n.getState());
                            pst.setString(16, n.getCountry());
                            pst.setString(17, n.getEmail());
                            pst.setString(18, n.getNoteText());
                            pst.setString(19, n.getMediaFilename());
                            pst.setString(20, n.getMediaTitle());
                            pst.setString(21, n.getPrefix());
                            pst.setString(22, n.getSurname());
                            pst.setString(23, n.getGivenname());
                            pst.setString(24, n.getPatronym());
                            pst.setString(25, n.getPostfix());
                            pst.setString(26, n.getSource());
                            pst.setString(27, n.getPrivateText());
                            if (n.getRefNames() == null) {
                                pst.setNull(28, Types.ARRAY);
                            } else {
                                Array xx = con.createArrayOf("varchar", n.getRefNames());
                                pst.setArray(28, xx);
                            }
                            if (n.getRefPlaces() == null) {
                                pst.setNull(29, Types.ARRAY);
                            } else {
                                Array xx = con.createArrayOf("varchar", n.getRefPlaces());
                                pst.setArray(29, xx);
                            }
                        }
                        if (n.getPnid() > 0) {
                            pst.setInt(30, n.getPnid());
                            if (n.getModified() != null) {
                                pst.setTimestamp(31, n.getModified());
                            }
                            int luku = pst.executeUpdate();
                            if (luku != 1) {
                                logger.warning("Person notice [" + n.getTag() + "] update for pid " + pid + " failed [" + luku + "] (Should be 1)");
                                throw new SQLException("TRANSACTION_ERROR_3");
                            }
                            logger.fine("Päivitettiin " + luku + " tietuetta pnid=[" + n.getPnid() + "]");
                        } else {
                            pst.setInt(30, pnid);
                            pst.setInt(31, pid);
                            pst.setString(32, n.getTag());
                            int luku = pst.executeUpdate();
                            logger.fine("Luotiin " + luku + " tietue pnid=[" + pnid + "]");
                        }
                        if (n.getMediaData() == null) {
                            String sql = "update unitnotice set mediadata = null where pnid = ?";
                            pst = con.prepareStatement(sql);
                            pst.setInt(1, pnid);
                            int lukuri = pst.executeUpdate();
                            if (lukuri != 1) {
                                logger.warning("media deleted for pnid " + n.getPnid() + " gave result " + lukuri);
                            }
                        } else {
                            String UPDATE_IMAGE_DATA = "update UnitNotice set MediaData = ?," + "mediaWidth = ?,mediaheight = ? where PNID = ? ";
                            PreparedStatement ps = this.con.prepareStatement(UPDATE_IMAGE_DATA);
                            ps.setBytes(1, n.getMediaData());
                            Dimension d = n.getMediaSize();
                            ps.setInt(2, d.width);
                            ps.setInt(3, d.height);
                            ps.setInt(4, pnid);
                            ps.executeUpdate();
                        }
                    }
                    if (n.getLanguages() != null) {
                        for (int l = 0; l < n.getLanguages().length; l++) {
                            UnitLanguage ll = n.getLanguages()[l];
                            if (ll.isToBeDeleted()) {
                                if (ll.getPnid() > 0) {
                                    pst = con.prepareStatement(delOneLangSql);
                                    pst.setInt(1, ll.getPnid());
                                    pst.setString(2, ll.getLangCode());
                                    int lukuri = pst.executeUpdate();
                                    if (lukuri != 1) {
                                        logger.warning("language deleted for pnid " + n.getPnid() + " [" + ll.getLangCode() + "] gave result " + lukuri);
                                    }
                                }
                            }
                            if (ll.isToBeUpdated()) {
                                if (ll.getPnid() == 0) {
                                    pst = con.prepareStatement(insLangSql);
                                    pst.setInt(1, n.getPnid());
                                    pst.setInt(2, pid);
                                    pst.setString(3, n.getTag());
                                    pst.setString(4, ll.getLangCode());
                                    pst.setString(5, ll.getNoticeType());
                                    pst.setString(6, ll.getDescription());
                                    pst.setString(7, ll.getPlace());
                                    pst.setString(8, ll.getNoteText());
                                    pst.setString(9, ll.getMediaTitle());
                                    int lukuri = pst.executeUpdate();
                                    if (lukuri != 1) {
                                        logger.warning("language added for pnid " + n.getPnid() + " [" + ll.getLangCode() + "] gave result " + lukuri);
                                    }
                                } else {
                                    pst = con.prepareStatement(updLangSql);
                                    pst.setString(1, ll.getNoticeType());
                                    pst.setString(2, ll.getDescription());
                                    pst.setString(3, ll.getPlace());
                                    pst.setString(4, ll.getNoteText());
                                    pst.setString(5, ll.getMediaTitle());
                                    pst.setInt(6, ll.getPnid());
                                    pst.setString(7, ll.getLangCode());
                                    int lukuri = pst.executeUpdate();
                                    if (lukuri != 1) {
                                        logger.warning("language for pnid " + ll.getPnid() + " [" + ll.getLangCode() + "] gave result " + lukuri);
                                    }
                                    pst.close();
                                }
                            }
                        }
                    }
                    if (n.getPnid() > 0) {
                        pnid = n.getPnid();
                    }
                    pstUpdRow.setInt(1, i + 1);
                    pstUpdRow.setInt(2, pnid);
                    pstUpdRow.executeUpdate();
                }
            }
            if (req.relations != null) {
                if (req.persLong.getPid() == 0) {
                    req.persLong.setPid(pid);
                    for (int i = 0; i < req.relations.length; i++) {
                        Relation r = req.relations[i];
                        if (r.getPid() == 0) {
                            r.setPid(pid);
                        }
                    }
                }
                updateRelations(userid, req);
            }
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                logger.log(Level.WARNING, "Person update rollback failed", e1);
            }
            logger.log(Level.WARNING, "person update rolled back for [" + pid + "]", e);
            res.resu = e.getMessage();
            return res;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                logger.log(Level.WARNING, "set autocommit failed", e);
            }
        }
        return res;
    }
