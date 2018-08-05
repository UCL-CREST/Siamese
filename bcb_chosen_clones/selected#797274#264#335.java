    public void putMedia(Media m) {
        if (m == null) {
            return;
        }
        if (_conn == null) {
            log.error("DatabaseDatastore not connected!");
            return;
        }
        if (log.isTraceEnabled()) {
            log.trace("Writing Media " + m.toString() + " to database");
        }
        try {
            try {
                long trackid = getLocalID(m, _conn);
                if (m.isBaseDirty()) {
                    if (log.isTraceEnabled()) {
                        log.trace("Need to update base " + m.getID() + " to database");
                    }
                    Integer artist = getArtistID(m, _conn);
                    Integer author = getAuthorID(m, _conn);
                    Integer artistAlias = getArtistAliasID(m, _conn);
                    PreparedStatement s = _conn.prepareStatement("update media_track set track_name=?,track_artist_id=?,track_author_id=?,track_artist_alias_id=?,track_audit_timestamp=CURRENT_TIMESTAMP where track_id = ?");
                    s.setString(1, m.getName());
                    if (artist != null) {
                        s.setLong(2, artist);
                    } else {
                        s.setNull(2, Types.BIGINT);
                    }
                    if (author != null) {
                        s.setLong(3, author);
                    } else {
                        s.setNull(3, Types.BIGINT);
                    }
                    if (artistAlias != null) {
                        s.setLong(4, artistAlias);
                    } else {
                        s.setNull(4, Types.BIGINT);
                    }
                    s.setLong(5, trackid);
                    s.executeUpdate();
                    s.close();
                }
                if (m.isUserDirty()) {
                    if (log.isTraceEnabled()) {
                        log.trace("Need to update user " + m.getID() + " to database");
                    }
                    PreparedStatement s = _conn.prepareStatement("update media_track_rating set rating=?, play_count=? where track_id=? and user_id=?");
                    s.setFloat(1, m.getRating());
                    s.setLong(2, m.getPlayCount());
                    s.setLong(3, trackid);
                    s.setLong(4, userid);
                    if (s.executeUpdate() != 1) {
                        s.close();
                    }
                    s.close();
                }
                if (m.isContentDirty()) {
                    updateLocation(m, _conn);
                }
                _conn.commit();
                m.resetDirty();
                if (log.isTraceEnabled()) {
                    log.trace("Committed " + m.getID() + " to database");
                }
            } catch (Exception e) {
                log.error(e.toString(), e);
                _conn.rollback();
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }
