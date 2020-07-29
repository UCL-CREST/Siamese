    public void load(String fileName) {
        this.fileName = fileName;
        FileLoader loader = new FileLoader();
        short[] b = loader.loadFile(fileName, nes.getGui());
        if (b == null || b.length == 0) {
            nes.gui.showErrorMsg("Unable to load ROM file.");
            valid = false;
        }
        header = new short[16];
        for (int i = 0; i < 16; i++) {
            header[i] = b[i];
        }
        String fcode = new String(new byte[] { (byte) b[0], (byte) b[1], (byte) b[2], (byte) b[3] });
        if (!fcode.equals("NES" + new String(new byte[] { 0x1A }))) {
            valid = false;
            return;
        }
        romCount = header[4];
        vromCount = header[5] * 2;
        mirroring = ((header[6] & 1) != 0 ? 1 : 0);
        batteryRam = (header[6] & 2) != 0;
        trainer = (header[6] & 4) != 0;
        fourScreen = (header[6] & 8) != 0;
        mapperType = (header[6] >> 4) | (header[7] & 0xF0);
        if (batteryRam) {
            loadBatteryRam();
        }
        boolean foundError = false;
        for (int i = 8; i < 16; i++) {
            if (header[i] != 0) {
                foundError = true;
                break;
            }
        }
        if (foundError) {
            mapperType &= 0xF;
        }
        rom = new short[romCount][16384];
        vrom = new short[vromCount][4096];
        vromTile = new Tile[vromCount][256];
        int offset = 16;
        for (int i = 0; i < romCount; i++) {
            for (int j = 0; j < 16384; j++) {
                if (offset + j >= b.length) {
                    break;
                }
                rom[i][j] = b[offset + j];
            }
            offset += 16384;
        }
        for (int i = 0; i < vromCount; i++) {
            for (int j = 0; j < 4096; j++) {
                if (offset + j >= b.length) {
                    break;
                }
                vrom[i][j] = b[offset + j];
            }
            offset += 4096;
        }
        for (int i = 0; i < vromCount; i++) {
            for (int j = 0; j < 256; j++) {
                vromTile[i][j] = new Tile();
            }
        }
        int tileIndex;
        int leftOver;
        for (int v = 0; v < vromCount; v++) {
            for (int i = 0; i < 4096; i++) {
                tileIndex = i >> 4;
                leftOver = i % 16;
                if (leftOver < 8) {
                    vromTile[v][tileIndex].setScanline(leftOver, vrom[v][i], vrom[v][i + 8]);
                } else {
                    vromTile[v][tileIndex].setScanline(leftOver - 8, vrom[v][i - 8], vrom[v][i]);
                }
            }
        }
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        byte[] tempArray = new byte[rom.length + vrom.length];
        crc.update(tempArray);
        crc32 = crc.getValue();
        tempArray = null;
        System.out.println("CRC Value: " + crc32 + "");
        valid = true;
    }
