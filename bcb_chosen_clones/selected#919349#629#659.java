    public void postProcess() throws StopWriterVisitorException {
        shpWriter.postProcess();
        try {
            FileChannel fcinShp = new FileInputStream(fTemp).getChannel();
            FileChannel fcoutShp = new FileOutputStream(fileShp).getChannel();
            DriverUtilities.copy(fcinShp, fcoutShp);
            File shxFile = SHP.getShxFile(fTemp);
            FileChannel fcinShx = new FileInputStream(shxFile).getChannel();
            FileChannel fcoutShx = new FileOutputStream(SHP.getShxFile(fileShp)).getChannel();
            DriverUtilities.copy(fcinShx, fcoutShx);
            File dbfFile = getDataFile(fTemp);
            short originalEncoding = DbfEncodings.getInstance().getDbfIdForCharset(shpWriter.getCharset());
            RandomAccessFile fo = new RandomAccessFile(dbfFile, "rw");
            fo.seek(29);
            fo.writeByte(originalEncoding);
            fo.close();
            FileChannel fcinDbf = new FileInputStream(dbfFile).getChannel();
            FileChannel fcoutDbf = new FileOutputStream(getDataFile(fileShp)).getChannel();
            DriverUtilities.copy(fcinDbf, fcoutDbf);
            fTemp.delete();
            shxFile.delete();
            dbfFile.delete();
            reload();
        } catch (FileNotFoundException e) {
            throw new StopWriterVisitorException(getName(), e);
        } catch (IOException e) {
            throw new StopWriterVisitorException(getName(), e);
        } catch (ReloadDriverException e) {
            throw new StopWriterVisitorException(getName(), e);
        }
    }
