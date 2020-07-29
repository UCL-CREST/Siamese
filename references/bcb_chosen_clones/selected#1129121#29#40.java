    public void create() throws IOException {
        FileChannel fc = new FileInputStream(sourceFile).getChannel();
        for (RangeArrayElement element : array) {
            FileChannel fc_ = fc.position(element.starting());
            File part = new File(destinationDirectory, "_0x" + Long.toHexString(element.starting()) + ".partial");
            FileChannel partfc = new FileOutputStream(part).getChannel();
            partfc.transferFrom(fc_, 0, element.getSize());
            partfc.force(true);
            partfc.close();
        }
        fc.close();
    }
