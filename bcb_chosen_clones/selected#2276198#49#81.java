    public static String replaceRangePartition(String sql, int partitions, int partitionID) throws ParseException {
        Pattern p = Pattern.compile("(" + THIS_GET_RANGE_PARTITION.replace(".", "\\.").replace("(", "\\(") + ").*\\)");
        Matcher m = p.matcher(sql);
        try {
            String code = sql;
            while (m.find()) {
                code = code.substring(m.start(), m.end());
                code = code.replace(THIS_GET_RANGE_PARTITION, "").trim();
                String[] params = code.substring(0, code.length() - 1).split(",");
                int startId = Integer.parseInt(params[0].trim());
                int endId = Integer.parseInt(params[1].trim());
                int pSize = (endId - startId) / partitions;
                int pStart, pEnd;
                if (partitionID == 0) {
                    pStart = startId;
                    pEnd = pSize + startId;
                } else if (partitionID == partitions - 1) {
                    pStart = (pSize * partitionID) + 1 + startId;
                    pEnd = endId;
                } else {
                    pStart = (pSize * partitionID) + startId + 1;
                    pEnd = (pSize * (partitionID + 1)) + startId;
                }
                code = pStart + " and " + pEnd;
                return m.replaceAll(code);
            }
            return code;
        } catch (Throwable e) {
            ParseException e1 = new ParseException("Invalid parameters for auto range partitioning syntax should be " + THIS_GET_RANGE_PARTITION + "start range, end range) - " + e.getMessage() + ", whilst parsing \"" + sql + "\"", m.start());
            e1.setStackTrace(e.getStackTrace());
            throw e1;
        }
    }
