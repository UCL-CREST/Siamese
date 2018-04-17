    public void postProcess() throws StopWriterVisitorException {
        dxfWriter.postProcess();
        try {
            FileChannel fcinDxf = new FileInputStream(fTemp).getChannel();
            FileChannel fcoutDxf = new FileOutputStream(m_Fich).getChannel();
            DriverUtilities.copy(fcinDxf, fcoutDxf);
            fTemp.delete();
        } catch (FileNotFoundException e) {
            throw new StopWriterVisitorException(getName(), e);
        } catch (IOException e) {
            throw new StopWriterVisitorException(getName(), e);
        }
    }
