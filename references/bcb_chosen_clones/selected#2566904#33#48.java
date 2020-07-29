    private boolean setPayload() throws IOException {
        if (Index < Headers.length) {
            FileOutputStream fos = new FileOutputStream(Headers[Index], true);
            FileInputStream fis = new FileInputStream(HeadlessData);
            FileChannel fic = fis.getChannel();
            FileChannel foc = fos.getChannel();
            fic.transferTo(0, fic.size(), foc);
            fic.close();
            foc.close();
            setDestination(Destinations[Index]);
            setPayload(Headers[Index]);
            Index++;
            return true;
        }
        return false;
    }
