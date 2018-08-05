    private ObjectInfo readObjectFromFile(DataInputStream in, Hashtable table, int version) throws IOException, InvalidObjectException {
        ObjectInfo info = new ObjectInfo(null, new CoordinateSystem(in), in.readUTF());
        Class cls;
        Constructor con;
        Object3D obj;
        Object key;
        info.id = in.readInt();
        if (info.id >= nextID) nextID = info.id + 1;
        info.visible = in.readBoolean();
        key = new Integer(in.readInt());
        obj = (Object3D) table.get(key);
        if (obj == null) {
            try {
                String classname = in.readUTF();
                int len = in.readInt();
                byte bytes[] = new byte[len];
                in.readFully(bytes);
                try {
                    cls = ModellingApp.getClass(classname);
                    con = cls.getConstructor(new Class[] { DataInputStream.class, Scene.class });
                    obj = (Object3D) con.newInstance(new Object[] { new DataInputStream(new ByteArrayInputStream(bytes)), this });
                } catch (Exception ex) {
                    if (ex instanceof InvocationTargetException) ((InvocationTargetException) ex).getTargetException().printStackTrace(); else ex.printStackTrace();
                    obj = new NullObject();
                    info.name = "<unreadable> " + info.name;
                    errorsLoading = true;
                }
                table.put(key, obj);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new IOException();
            }
        }
        info.object = obj;
        if (version < 2 && obj.getTexture() != null) {
            TextureParameter texParam[] = obj.getTextureMapping().getParameters();
            ParameterValue paramValue[] = obj.getParameterValues();
            double val[] = new double[paramValue.length];
            boolean perVertex[] = new boolean[paramValue.length];
            for (int i = 0; i < val.length; i++) val[i] = in.readDouble();
            for (int i = 0; i < perVertex.length; i++) perVertex[i] = in.readBoolean();
            for (int i = 0; i < paramValue.length; i++) if (paramValue[i] == null) {
                if (perVertex[i]) paramValue[i] = new VertexParameterValue(obj, texParam[i]); else paramValue[i] = new ConstantParameterValue(val[i]);
            }
            obj.setParameterValues(paramValue);
        }
        int tracks = in.readInt();
        try {
            for (int i = 0; i < tracks; i++) {
                cls = ModellingApp.getClass(in.readUTF());
                con = cls.getConstructor(new Class[] { ObjectInfo.class });
                Track tr = (Track) con.newInstance(new Object[] { info });
                tr.initFromStream(in, this);
                info.addTrack(tr, i);
            }
            if (info.tracks == null) info.tracks = new Track[0];
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException();
        }
        return info;
    }
