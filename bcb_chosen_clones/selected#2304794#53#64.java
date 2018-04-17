        public Object mapRow(ResultSet rs, int i) throws SQLException {
            try {
                BLOB blob = (BLOB) rs.getBlob(1);
                OutputStream outputStream = blob.setBinaryStream(0L);
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                throw new SQLException(e.getMessage());
            }
            return null;
        }
