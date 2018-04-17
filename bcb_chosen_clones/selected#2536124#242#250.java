    protected void commonInit(String displayName, IDataSource[] dataSource, Unit unit) {
        this.displayName = displayName;
        CRC32 crc = new CRC32();
        crc.update(canonicalName.getBytes());
        this.hashedId = crc.getValue();
        this.unit = unit;
        this.cursors = new long[this.dataSources.length];
        getStreamManager().addStream(this);
    }
