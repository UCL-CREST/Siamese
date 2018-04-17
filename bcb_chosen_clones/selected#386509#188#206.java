    protected int searchSourcePosition(int id) throws Exception {
        int left = 0;
        int right = dataSourceInfos.size() - 1;
        if (dataSourceInfos.size() == 0) throw new Exception();
        while (left != right) {
            int middle = (left + right) / 2;
            DataSourceInfo dataSourceInfo = (DataSourceInfo) dataSourceInfos.get(middle);
            if (dataSourceInfo.getId() > id) {
                right = middle - 1;
            } else if (dataSourceInfo.getId() < id) {
                left = middle + 1;
            } else {
                return middle;
            }
        }
        DataSourceInfo dataSourceInfo = (DataSourceInfo) dataSourceInfos.get(left);
        if (dataSourceInfo.getId() == id) return left;
        throw new Exception();
    }
