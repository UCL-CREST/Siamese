    private DbPartitionHelper getDbPartitionHelperFromTableAnnotation(Table table) {
        DbPartitionHelper dbPartitionHelper = null;
        if (!table.partitionid().equals("")) {
            dbPartitionHelper = (DbPartitionHelper) HaloUtil.getBean(table.partitionid());
            if (dbPartitionHelper == null) {
                throw new RuntimeException("can not found spring bean id [ " + table.partitionid() + " ]");
            }
        } else {
            try {
                dbPartitionHelper = (DbPartitionHelper) table.partitionClass().getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return dbPartitionHelper;
    }
