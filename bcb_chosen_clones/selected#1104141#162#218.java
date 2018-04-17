    @Override
    public void saveStructure(long userId, TreeStructureInfo info, List<TreeStructureNode> structure) throws DatabaseException {
        if (info == null) throw new NullPointerException("info");
        if (structure == null) throw new NullPointerException("structure");
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.warn("Unable to set autocommit off", e);
        }
        PreparedStatement insertInfoSt = null, insSt = null;
        try {
            insertInfoSt = getConnection().prepareStatement(INSERT_INFO);
            insertInfoSt.setLong(1, userId);
            insertInfoSt.setString(2, info.getDescription() != null ? info.getDescription() : "");
            insertInfoSt.setString(3, info.getBarcode());
            insertInfoSt.setString(4, info.getName());
            insertInfoSt.setString(5, info.getInputPath());
            insertInfoSt.setString(6, info.getModel());
            insertInfoSt.executeUpdate();
            PreparedStatement seqSt = getConnection().prepareStatement(INFO_VALUE);
            ResultSet rs = seqSt.executeQuery();
            int key = -1;
            while (rs.next()) {
                key = rs.getInt(1);
            }
            if (key == -1) {
                getConnection().rollback();
                throw new DatabaseException("Unable to obtain new id from DB when executing query: " + insertInfoSt);
            }
            int total = 0;
            for (TreeStructureNode node : structure) {
                insSt = getConnection().prepareStatement(INSERT_NODE);
                insSt.setLong(1, key);
                insSt.setString(2, node.getPropId());
                insSt.setString(3, node.getPropParent());
                insSt.setString(4, node.getPropName());
                insSt.setString(5, node.getPropPicture());
                insSt.setString(6, node.getPropType());
                insSt.setString(7, node.getPropTypeId());
                insSt.setString(8, node.getPropPageType());
                insSt.setString(9, node.getPropDateIssued());
                insSt.setString(10, node.getPropAltoPath());
                insSt.setString(11, node.getPropOcrPath());
                insSt.setBoolean(12, node.getPropExist());
                total += insSt.executeUpdate();
            }
            if (total != structure.size()) {
                getConnection().rollback();
                throw new DatabaseException("Unable to insert _ALL_ nodes: " + total + " nodes were inserted of " + structure.size());
            }
            getConnection().commit();
        } catch (SQLException e) {
            LOGGER.error("Queries: \"" + insertInfoSt + "\" and \"" + insSt + "\"", e);
        } finally {
            closeConnection();
        }
    }
