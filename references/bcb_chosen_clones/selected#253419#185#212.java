    public long binarySearch(Grabable obj) {
        if (clase == null) return -1;
        if (!isOk(obj)) return -1;
        long pos = -1;
        try {
            long actual = bytePos();
            long n = count();
            long izq = 0, der = n - 1;
            while (izq <= der && pos == -1) {
                long c = (izq + der) / 2;
                seekRegister(c);
                Register reg = this.read();
                if (reg.getData().equals(obj) && reg.getState() == Register.ACTIVO) {
                    long p = bytePos() - reg.sizeOf();
                    p = p - marca;
                    pos = p / reg.sizeOf();
                    break;
                } else {
                    if (obj.compareTo(reg.getData()) > 0) izq = c + 1; else der = c - 1;
                }
            }
            seekByte(actual);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el registro: " + e.getMessage());
            System.exit(1);
        }
        return pos;
    }
