    public synchronized AbstractBaseObject update(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        MailSetting tmpMailSetting = (MailSetting) ((MailSetting) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("UPDATE MAIL_SETTING ");
                sqlStat.append("SET  USER_RECORD_ID=?, PROFILE_NAME=?, MAIL_SERVER_TYPE=?, DISPLAY_NAME=?, EMAIL_ADDRESS=?, REMEMBER_PWD_FLAG=?, SPA_LOGIN_FLAG=?, INCOMING_SERVER_HOST=?, INCOMING_SERVER_PORT=?, INCOMING_SERVER_LOGIN_NAME=?, INCOMING_SERVER_LOGIN_PWD=?, OUTGOING_SERVER_HOST=?, OUTGOING_SERVER_PORT=?, OUTGOING_SERVER_LOGIN_NAME=?, OUTGOING_SERVER_LOGIN_PWD=?, PARAMETER_1=?, PARAMETER_2=?, PARAMETER_3=?, PARAMETER_4=?, PARAMETER_5=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=? ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpMailSetting.getUserRecordID());
                setPrepareStatement(preStat, 2, tmpMailSetting.getProfileName());
                setPrepareStatement(preStat, 3, tmpMailSetting.getMailServerType());
                setPrepareStatement(preStat, 4, tmpMailSetting.getDisplayName());
                setPrepareStatement(preStat, 5, tmpMailSetting.getEmailAddress());
                setPrepareStatement(preStat, 6, tmpMailSetting.getRememberPwdFlag());
                setPrepareStatement(preStat, 7, tmpMailSetting.getSpaLoginFlag());
                setPrepareStatement(preStat, 8, tmpMailSetting.getIncomingServerHost());
                setPrepareStatement(preStat, 9, tmpMailSetting.getIncomingServerPort());
                setPrepareStatement(preStat, 10, tmpMailSetting.getIncomingServerLoginName());
                setPrepareStatement(preStat, 11, tmpMailSetting.getIncomingServerLoginPwd());
                setPrepareStatement(preStat, 12, tmpMailSetting.getOutgoingServerHost());
                setPrepareStatement(preStat, 13, tmpMailSetting.getOutgoingServerPort());
                setPrepareStatement(preStat, 14, tmpMailSetting.getOutgoingServerLoginName());
                setPrepareStatement(preStat, 15, tmpMailSetting.getOutgoingServerLoginPwd());
                setPrepareStatement(preStat, 16, tmpMailSetting.getParameter1());
                setPrepareStatement(preStat, 17, tmpMailSetting.getParameter2());
                setPrepareStatement(preStat, 18, tmpMailSetting.getParameter3());
                setPrepareStatement(preStat, 19, tmpMailSetting.getParameter4());
                setPrepareStatement(preStat, 20, tmpMailSetting.getParameter5());
                setPrepareStatement(preStat, 21, new Integer(tmpMailSetting.getUpdateCount().intValue() + 1));
                setPrepareStatement(preStat, 22, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 23, currTime);
                setPrepareStatement(preStat, 24, tmpMailSetting.getID());
                setPrepareStatement(preStat, 25, tmpMailSetting.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                dbConn.commit();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    tmpMailSetting.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpMailSetting.setUpdateDate(currTime);
                    tmpMailSetting.setUpdateCount(new Integer(tmpMailSetting.getUpdateCount().intValue() + 1));
                    tmpMailSetting.setCreatorName(UserInfoFactory.getUserFullName(tmpMailSetting.getCreatorID()));
                    tmpMailSetting.setUpdaterName(UserInfoFactory.getUserFullName(tmpMailSetting.getUpdaterID()));
                    return (tmpMailSetting);
                }
            } catch (Exception e) {
                try {
                    dbConn.rollback();
                } catch (Exception ex) {
                }
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_UPDATE_ERROR, e);
            } finally {
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }
