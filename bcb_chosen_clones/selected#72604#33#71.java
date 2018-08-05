    protected static void scaleLayout(InputStream in, String name, ZipOutputStream out, double scaleX, double scaleY) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = in.read()) != -1) baos.write(i);
        String str = new String(baos.toByteArray());
        out.putNextEntry(new ZipEntry(name));
        i = 0;
        int oldI = i;
        String buf = "";
        while ((i = str.indexOf("x=\"", i)) != -1) {
            i += 3;
            buf += str.substring(oldI, i);
            int j = str.indexOf("\"", i);
            double newX = Integer.parseInt(str.substring(i, j));
            System.out.print("X: " + newX + " -> ");
            newX *= scaleX;
            System.out.println("" + newX);
            buf += ("" + (int) newX);
            oldI = j;
        }
        buf += str.substring(oldI);
        i = 0;
        oldI = i;
        str = buf;
        buf = "";
        while ((i = str.indexOf("y=\"", i)) != -1) {
            i += 3;
            buf += str.substring(oldI, i);
            int j = str.indexOf("\"", i);
            double newY = Integer.parseInt(str.substring(i, j));
            System.out.print("Y: " + newY + " -> ");
            newY *= scaleY;
            System.out.println(newY);
            buf += ("" + (int) newY);
            oldI = j;
        }
        buf += str.substring(oldI);
        out.write(buf.getBytes());
    }
