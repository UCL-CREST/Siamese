    public void postProcess() throws StopWriterVisitorException {
        dbfWriter.postProcess();
        try {
            short originalEncoding = dbf.getDbaseHeader().getLanguageID();
            File dbfFile = fTemp;
            FileChannel fcinDbf = new FileInputStream(dbfFile).getChannel();
            FileChannel fcoutDbf = new FileOutputStream(file).getChannel();
            DriverUtilities.copy(fcinDbf, fcoutDbf);
            fTemp.delete();
            close();
            RandomAccessFile fo = new RandomAccessFile(file, "rw");
            fo.seek(29);
            fo.writeByte(originalEncoding);
            fo.close();
            open(file);
        } catch (FileNotFoundException e) {
            throw new StopWriterVisitorException(getName(), e);
        } catch (IOException e) {
            throw new StopWriterVisitorException(getName(), e);
        } catch (CloseDriverException e) {
            throw new StopWriterVisitorException(getName(), e);
        } catch (OpenDriverException e) {
            throw new StopWriterVisitorException(getName(), e);
        }
    }
