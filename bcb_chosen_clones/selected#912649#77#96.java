    public void updateFiles(String ourPath) {
        System.out.println("Update");
        DataInputStream dis = null;
        DataOutputStream dos = null;
        for (int i = 0; i < newFiles.size() && i < nameNewFiles.size(); i++) {
            try {
                dis = new DataInputStream(new FileInputStream((String) newFiles.get(i)));
                dos = new DataOutputStream(new FileOutputStream((new StringBuilder(String.valueOf(ourPath))).append("\\").append((String) nameNewFiles.get(i)).toString()));
            } catch (IOException e) {
                System.out.println(e.toString());
                System.exit(0);
            }
            try {
                do dos.writeChar(dis.readChar()); while (true);
            } catch (EOFException e) {
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }
