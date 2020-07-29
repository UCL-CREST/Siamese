    private boolean addBookmark0(Bookmark bookmark, BookmarkFolder folder, PreparedStatement preparedStatement) throws SQLException {
        Object[] bindVariables = new Object[8];
        int[] types = new int[8];
        types[0] = Types.BOOLEAN;
        types[1] = Types.TIMESTAMP;
        types[2] = Types.TIMESTAMP;
        types[3] = Types.VARCHAR;
        types[4] = Types.VARCHAR;
        types[5] = Types.BIGINT;
        types[6] = Types.VARCHAR;
        types[7] = Types.VARCHAR;
        bindVariables[0] = Boolean.valueOf(bookmark.isFavorite());
        Date time = bookmark.getCreationTime();
        bindVariables[1] = new Timestamp(time == null ? System.currentTimeMillis() : time.getTime());
        time = bookmark.getLastAccess();
        bindVariables[2] = new Timestamp(time == null ? System.currentTimeMillis() : time.getTime());
        bindVariables[3] = bookmark.getName();
        bindVariables[4] = bookmark.getCommandText();
        bindVariables[5] = new Long(bookmark.getUseCount());
        bindVariables[6] = folder == null ? bookmark.getPath() : folder.getPath();
        ColorLabel colorLabel = bookmark.getColorLabel();
        bindVariables[7] = colorLabel == null ? null : colorLabel.name();
        boolean doBatch = (preparedStatement != null);
        boolean hasError = true;
        embeddedConnection.setAutoCommit(false);
        PreparedStatement statement = null;
        try {
            if (preparedStatement == null) {
                statement = embeddedConnection.prepareStatement(BOOKMARK_INSERT);
            } else {
                statement = preparedStatement;
            }
            for (int i = 0; i < bindVariables.length; i++) {
                if (bindVariables[i] == null) {
                    statement.setNull(i + 1, types[i]);
                } else {
                    statement.setObject(i + 1, bindVariables[i]);
                }
            }
            try {
                int affectedCount = statement.executeUpdate();
                long identityValue = getInsertedPrimaryKey();
                bookmark.setId(identityValue);
                addBindVariables(bookmark);
                hasError = false;
                return affectedCount == 1;
            } catch (SQLException exception) {
                if (CONSTRAINT_VIOLATION.equals(exception.getSQLState())) {
                    return false;
                }
                throw exception;
            }
        } finally {
            if (hasError) {
                embeddedConnection.rollback();
            } else {
                embeddedConnection.commit();
            }
            embeddedConnection.setAutoCommit(true);
            if (preparedStatement != null) {
                if (!doBatch) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ignored) {
                    }
                } else if (doBatch) {
                    preparedStatement.clearParameters();
                    preparedStatement.clearWarnings();
                }
            }
        }
    }
