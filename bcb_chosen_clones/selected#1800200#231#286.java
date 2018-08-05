    public MsgRecvInfo[] recvMsg(MsgRecvReq msgRecvReq) throws SQLException {
        String updateSQL = " update dyhikemomessages set receive_id = ?, receive_Time = ?  where mo_to =? and receive_id =0  limit 20";
        String selectSQL = " select MOMSG_ID,mo_from,mo_to,create_time,mo_content from dyhikemomessages where receive_id =?  ";
        String insertSQL = " insert into t_receive_history select * from dyhikemomessages  where receive_id =?  ";
        String deleteSQL = " delete from dyhikemomessages where receive_id =? ";
        Logger logger = Logger.getLogger(this.getClass());
        ArrayList msgInfoList = new ArrayList();
        String mo_to = msgRecvReq.getAuthInfo().getUserName();
        MsgRecvInfo[] msgInfoArray = new ototype.MsgRecvInfo[0];
        String receiveTime = Const.DF.format(new Date());
        logger.debug("recvMsgNew1");
        Connection conn = null;
        try {
            int receiveID = this.getSegquence("receiveID");
            conn = this.getJdbcTemplate().getDataSource().getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setInt(1, receiveID);
            pstmt.setString(2, receiveTime);
            pstmt.setString(3, mo_to);
            int recordCount = pstmt.executeUpdate();
            logger.info(recordCount + " record(s) got");
            if (recordCount > 0) {
                pstmt = conn.prepareStatement(selectSQL);
                pstmt.setInt(1, receiveID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    MsgRecvInfo msg = new MsgRecvInfo();
                    msg.setDestMobile(rs.getString("mo_to"));
                    msg.setRecvAddi(rs.getString("mo_to"));
                    msg.setSendAddi(rs.getString("MO_FROM"));
                    msg.setContent(rs.getString("mo_content"));
                    msg.setRecvDate(rs.getString("create_time"));
                    msgInfoList.add(msg);
                }
                msgInfoArray = (MsgRecvInfo[]) msgInfoList.toArray(new MsgRecvInfo[msgInfoList.size()]);
                pstmt = conn.prepareStatement(insertSQL);
                pstmt.setInt(1, receiveID);
                pstmt.execute();
                pstmt = conn.prepareStatement(deleteSQL);
                pstmt.setInt(1, receiveID);
                pstmt.execute();
                conn.commit();
            }
            logger.debug("recvMsgNew2");
            return msgInfoArray;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
