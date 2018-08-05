    public synchronized AbstractBaseObject insert(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        MailSetting tmpMailSetting = (MailSetting) ((MailSetting) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   MAIL_SETTING(ID, USER_RECORD_ID, PROFILE_NAME, MAIL_SERVER_TYPE, DISPLAY_NAME, EMAIL_ADDRESS, REMEMBER_PWD_FLAG, SPA_LOGIN_FLAG, INCOMING_SERVER_HOST, INCOMING_SERVER_PORT, INCOMING_SERVER_LOGIN_NAME, INCOMING_SERVER_LOGIN_PWD, OUTGOING_SERVER_HOST, OUTGOING_SERVER_PORT, OUTGOING_SERVER_LOGIN_NAME, OUTGOING_SERVER_LOGIN_PWD, PARAMETER_1, PARAMETER_2, PARAMETER_3, PARAMETER_4, PARAMETER_5, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpMailSetting.getUserRecordID());
                setPrepareStatement(preStat, 3, tmpMailSetting.getProfileName());
                setPrepareStatement(preStat, 4, tmpMailSetting.getMailServerType());
                setPrepareStatement(preStat, 5, tmpMailSetting.getDisplayName());
                setPrepareStatement(preStat, 6, tmpMailSetting.getEmailAddress());
                setPrepareStatement(preStat, 7, tmpMailSetting.getRememberPwdFlag());
                setPrepareStatement(preStat, 8, tmpMailSetting.getSpaLoginFlag());
                setPrepareStatement(preStat, 9, tmpMailSetting.getIncomingServerHost());
                setPrepareStatement(preStat, 10, tmpMailSetting.getIncomingServerPort());
                setPrepareStatement(preStat, 11, tmpMailSetting.getIncomingServerLoginName());
                setPrepareStatement(preStat, 12, tmpMailSetting.getIncomingServerLoginPwd());
                setPrepareStatement(preStat, 13, tmpMailSetting.getOutgoingServerHost());
                setPrepareStatement(preStat, 14, tmpMailSetting.getOutgoingServerPort());
                setPrepareStatement(preStat, 15, tmpMailSetting.getOutgoingServerLoginName());
                setPrepareStatement(preStat, 16, tmpMailSetting.getOutgoingServerLoginPwd());
                setPrepareStatement(preStat, 17, tmpMailSetting.getParameter1());
                setPrepareStatement(preStat, 18, tmpMailSetting.getParameter2());
                setPrepareStatement(preStat, 19, tmpMailSetting.getParameter3());
                setPrepareStatement(preStat, 20, tmpMailSetting.getParameter4());
                setPrepareStatement(preStat, 21, tmpMailSetting.getParameter5());
                setPrepareStatement(preStat, 22, GlobalConstant.RECORD_STATUS_ACTIVE);
                setPrepareStatement(preStat, 23, new Integer(0));
                setPrepareStatement(preStat, 24, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 25, currTime);
                setPrepareStatement(preStat, 26, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 27, currTime);
                preStat.executeUpdate();
                tmpMailSetting.setID(nextID);
                tmpMailSetting.setCreatorID(sessionContainer.getUserRecordID());
                tmpMailSetting.setCreateDate(currTime);
                tmpMailSetting.setUpdaterID(sessionContainer.getUserRecordID());
                tmpMailSetting.setUpdateDate(currTime);
                tmpMailSetting.setUpdateCount(new Integer(0));
                tmpMailSetting.setCreatorName(UserInfoFactory.getUserFullName(tmpMailSetting.getCreatorID()));
                tmpMailSetting.setUpdaterName(UserInfoFactory.getUserFullName(tmpMailSetting.getUpdaterID()));
                dbConn.commit();
                return (tmpMailSetting);
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
            } catch (Exception e) {
                try {
                    dbConn.rollback();
                } catch (Exception ex) {
                }
                log.error(e, e);
            } finally {
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
            return null;
        }
    }
