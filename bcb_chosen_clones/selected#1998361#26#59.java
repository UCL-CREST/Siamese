    public static void insert(Connection c, MLPApprox net, int azioneId, String descrizione, int[] indiciID, int output, Date from, Date to) throws SQLException {
        try {
            PreparedStatement ps = c.prepareStatement(insertNet, PreparedStatement.RETURN_GENERATED_KEYS);
            ArrayList<Integer> indexes = new ArrayList<Integer>(indiciID.length);
            for (int i = 0; i < indiciID.length; i++) indexes.add(indiciID[i]);
            ps.setObject(1, net);
            ps.setInt(2, azioneId);
            ps.setObject(3, indexes);
            ps.setInt(4, output);
            ps.setDate(5, from);
            ps.setDate(6, to);
            ps.setString(7, descrizione);
            ps.executeUpdate();
            ResultSet key = ps.getGeneratedKeys();
            if (key.next()) {
                int id = key.getInt(1);
                for (int i = 0; i < indiciID.length; i++) {
                    PreparedStatement psIndex = c.prepareStatement(insertNetIndex);
                    psIndex.setInt(1, indiciID[i]);
                    psIndex.setInt(2, id);
                    psIndex.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw e1;
            }
            throw e;
        }
    }
