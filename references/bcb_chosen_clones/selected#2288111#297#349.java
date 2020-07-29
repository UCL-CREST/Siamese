    protected synchronized AbstractBaseObject insert(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        DmsRelationalWord tmpDmsRelationalWord = (DmsRelationalWord) ((DmsRelationalWord) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   DMS_RELATIONAL_WORD(ID, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE, WORD, PARENT_ID, TYPE) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpDmsRelationalWord.getRecordStatus());
                setPrepareStatement(preStat, 3, new Integer(0));
                setPrepareStatement(preStat, 4, tmpDmsRelationalWord.getCreatorID());
                setPrepareStatement(preStat, 5, currTime);
                setPrepareStatement(preStat, 6, tmpDmsRelationalWord.getUpdaterID());
                setPrepareStatement(preStat, 7, currTime);
                if (tmpDmsRelationalWord.getWord() == null || "".equals(tmpDmsRelationalWord.getWord().trim())) {
                    return null;
                }
                setPrepareStatement(preStat, 8, tmpDmsRelationalWord.getWord());
                setPrepareStatement(preStat, 9, tmpDmsRelationalWord.getParentID());
                setPrepareStatement(preStat, 10, tmpDmsRelationalWord.getType());
                preStat.executeUpdate();
                tmpDmsRelationalWord.setID(nextID);
                tmpDmsRelationalWord.setCreatorID(tmpDmsRelationalWord.getCreatorID());
                tmpDmsRelationalWord.setCreateDate(currTime);
                tmpDmsRelationalWord.setUpdaterID(tmpDmsRelationalWord.getUpdaterID());
                tmpDmsRelationalWord.setUpdateDate(currTime);
                tmpDmsRelationalWord.setUpdateCount(new Integer(0));
                tmpDmsRelationalWord.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsRelationalWord.getCreatorID()));
                tmpDmsRelationalWord.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsRelationalWord.getUpdaterID()));
                dbConn.commit();
                return (tmpDmsRelationalWord);
            } catch (Exception e) {
                try {
                    dbConn.rollback();
                } catch (Exception ee) {
                }
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_INSERT_ERROR, e);
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
