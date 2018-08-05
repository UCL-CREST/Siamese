            public void update(Serializable pmsID, Serializable msg) throws RemoteException {
                ClassLoader classLoader = pms.getClassLoader();
                try {
                    byte[] msgBytes;
                    ByteArrayInputStream bain;
                    Class c;
                    Constructor con;
                    ObjectInputStream in;
                    Object obj;
                    msgBytes = (byte[]) msg;
                    bain = new ByteArrayInputStream(msgBytes);
                    c = classLoader.loadClass(LocalObjectInputStream.class.getName());
                    con = c.getConstructor(InputStream.class, Class.class);
                    in = (ObjectInputStream) con.newInstance(bain, pms.getMessageClass());
                    obj = in.readObject();
                    in.close();
                    contents.append(new Date()).append("\n");
                    for (Method i : getPropertyMethodList(obj.getClass())) {
                        contents.append(i.getName().substring(4)).append("\t");
                        contents.append(i.invoke(obj).toString()).append("\n");
                    }
                    contents.append("------------------------------------------\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
