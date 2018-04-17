    protected boolean store(Context context) throws DataStoreException, ServletException {
        Connection db = context.getConnection();
        Statement st = null;
        String q = null;
        Integer subscriber = context.getValueAsInteger("subscriber");
        int amount = 0;
        if (subscriber == null) {
            throw new DataAuthException("Don't know who moderator is");
        }
        Object response = context.get("Response");
        if (response == null) {
            throw new DataStoreException("Don't know what to moderate");
        } else {
            Context scratch = (Context) context.clone();
            TableDescriptor.getDescriptor("response", "response", scratch).fetch(scratch);
            Integer author = scratch.getValueAsInteger("author");
            if (subscriber.equals(author)) {
                throw new SelfModerationException("You may not moderate your own responses");
            }
        }
        context.put("moderator", subscriber);
        context.put("moderated", response);
        if (db != null) {
            try {
                st = db.createStatement();
                q = "select mods from subscriber where subscriber = " + subscriber.toString();
                ResultSet r = st.executeQuery(q);
                if (r.next()) {
                    if (r.getInt("mods") < 1) {
                        throw new DataAuthException("You have no moderation points left");
                    }
                } else {
                    throw new DataAuthException("Don't know who moderator is");
                }
                Object reason = context.get("reason");
                q = "select score from modreason where modreason = " + reason;
                r = st.executeQuery(q);
                if (r.next()) {
                    amount = r.getInt("score");
                    context.put("amount", new Integer(amount));
                } else {
                    throw new DataStoreException("Don't recognise reason (" + reason + ") to moderate");
                }
                context.put(keyField, null);
                if (super.store(context, db)) {
                    db.setAutoCommit(false);
                    q = "update RESPONSE set Moderation = " + "( select sum( Amount) from MODERATION " + "where Moderated = " + response + ") " + "where Response = " + response;
                    st.executeUpdate(q);
                    q = "update subscriber set mods = mods - 1 " + "where subscriber = " + subscriber;
                    st.executeUpdate(q);
                    q = "select author from response " + "where response = " + response;
                    r = st.executeQuery(q);
                    if (r.next()) {
                        int author = r.getInt("author");
                        if (author != 0) {
                            int points = -1;
                            if (amount > 0) {
                                points = 1;
                            }
                            StringBuffer qb = new StringBuffer("update subscriber ");
                            qb.append("set score = score + ").append(amount);
                            qb.append(", mods = mods + ").append(points);
                            qb.append(" where subscriber = ").append(author);
                            st.executeUpdate(qb.toString());
                        }
                    }
                    db.commit();
                }
            } catch (Exception e) {
                try {
                    db.rollback();
                } catch (Exception whoops) {
                    throw new DataStoreException("Shouldn't happen: " + "failed to back out " + "failed insert: " + whoops.getMessage());
                }
                throw new DataStoreException("Failed to store moderation: " + e.getMessage());
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (Exception noclose) {
                    }
                    context.releaseConnection(db);
                }
            }
        }
        return true;
    }
