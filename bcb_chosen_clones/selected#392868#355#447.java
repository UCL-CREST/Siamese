    protected synchronized Long putModel(String table, String linkTable, String type, TupleBinding binding, LocatableModel model) {
        try {
            if (model.getId() != null && !"".equals(model.getId())) {
                ps7.setInt(1, Integer.parseInt(model.getId()));
                ps7.execute();
                ps6.setInt(1, Integer.parseInt(model.getId()));
                ps6.execute();
            }
            if (persistenceMethod == PostgreSQLStore.BYTEA) {
                ps1.setString(1, model.getContig());
                ps1.setInt(2, model.getStartPosition());
                ps1.setInt(3, model.getStopPosition());
                ps1.setString(4, type);
                DatabaseEntry objData = new DatabaseEntry();
                binding.objectToEntry(model, objData);
                ps1.setBytes(5, objData.getData());
                ps1.executeUpdate();
            } else if (persistenceMethod == PostgreSQLStore.OID || persistenceMethod == PostgreSQLStore.FIELDS) {
                ps1b.setString(1, model.getContig());
                ps1b.setInt(2, model.getStartPosition());
                ps1b.setInt(3, model.getStopPosition());
                ps1b.setString(4, type);
                DatabaseEntry objData = new DatabaseEntry();
                binding.objectToEntry(model, objData);
                int oid = lobj.create(LargeObjectManager.READ | LargeObjectManager.WRITE);
                LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
                obj.write(objData.getData());
                obj.close();
                ps1b.setInt(5, oid);
                ps1b.executeUpdate();
            }
            ResultSet rs = null;
            PreparedStatement ps = conn.prepareStatement("select currval('" + table + "_" + table + "_id_seq')");
            rs = ps.executeQuery();
            int modelId = -1;
            if (rs != null) {
                if (rs.next()) {
                    modelId = rs.getInt(1);
                }
            }
            rs.close();
            ps.close();
            for (String key : model.getTags().keySet()) {
                int tagId = -1;
                if (tags.get(key) != null) {
                    tagId = tags.get(key);
                } else {
                    ps2.setString(1, key);
                    rs = ps2.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            tagId = rs.getInt(1);
                        }
                    }
                    rs.close();
                }
                if (tagId < 0) {
                    ps3.setString(1, key);
                    ps3.setString(2, model.getTags().get(key));
                    ps3.executeUpdate();
                    rs = ps4.executeQuery();
                    if (rs != null) {
                        if (rs.next()) {
                            tagId = rs.getInt(1);
                            tags.put(key, tagId);
                        }
                    }
                    rs.close();
                }
                ps5.setInt(1, tagId);
                ps5.executeUpdate();
            }
            conn.commit();
            return (new Long(modelId));
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return (null);
    }
