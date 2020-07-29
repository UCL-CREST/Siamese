    public int executeUpdate(String query, QueryParameter params) throws DAOException {
        PreparedStatement ps = null;
        Query queryObj = getModel().getQuery(query);
        if (conditionalQueries != null && conditionalQueries.containsKey(query)) {
            queryObj = (Query) conditionalQueries.get(query);
        }
        String sql = queryObj.getStatement(params.getVariables());
        logger.debug(sql);
        try {
            if (con == null || con.isClosed()) {
                con = DataSource.getInstance().getConnection(getModel().getDataSource());
            }
            ps = con.prepareStatement(sql);
            setParameters(ps, queryObj, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("DataBase Error :", e);
            if (transactionMode) rollback();
            transactionMode = false;
            throw new DAOException("Unexpected Error Query (" + query + ")", "error.DAO.database", e.getMessage());
        } catch (Exception ex) {
            logger.error("Error :", ex);
            if (transactionMode) rollback();
            transactionMode = false;
            throw new DAOException("Unexpected Error Query (" + query + ")", "error.DAO.database", ex.getMessage());
        } finally {
            try {
                if (!transactionMode) con.commit();
                if (ps != null) ps.close();
                if (!transactionMode && con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException("Unexpected Error Query (" + query + ")", "error.DAO.database", e.getMessage());
            }
        }
    }
