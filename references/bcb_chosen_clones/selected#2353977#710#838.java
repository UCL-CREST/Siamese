    private void initFromStream(DataInputStream in, boolean fullScene) throws IOException, InvalidObjectException {
        int i, j, count;
        short version = in.readShort();
        Hashtable table;
        Class cls;
        Constructor con;
        if (version < 0 || version > 3) throw new InvalidObjectException("");
        ambientColor = new RGBColor(in);
        fogColor = new RGBColor(in);
        fog = in.readBoolean();
        fogDist = in.readDouble();
        showGrid = in.readBoolean();
        snapToGrid = in.readBoolean();
        gridSpacing = in.readDouble();
        gridSubdivisions = in.readInt();
        framesPerSecond = in.readInt();
        nextID = 1;
        count = in.readInt();
        images = new Vector(count);
        for (i = 0; i < count; i++) {
            if (version == 0) {
                images.addElement(new MIPMappedImage(in, (short) 0));
                continue;
            }
            String classname = in.readUTF();
            try {
                cls = ModellingApp.getClass(classname);
                if (cls == null) throw new IOException("Unknown class: " + classname);
                con = cls.getConstructor(new Class[] { DataInputStream.class });
                images.addElement(con.newInstance(new Object[] { in }));
            } catch (Exception ex) {
                throw new IOException("Error loading image: " + ex.getMessage());
            }
        }
        count = in.readInt();
        materials = new Vector(count);
        for (i = 0; i < count; i++) {
            try {
                String classname = in.readUTF();
                int len = in.readInt();
                byte bytes[] = new byte[len];
                in.readFully(bytes);
                cls = ModellingApp.getClass(classname);
                try {
                    if (cls == null) throw new IOException("Unknown class: " + classname);
                    con = cls.getConstructor(new Class[] { DataInputStream.class, Scene.class });
                    materials.addElement(con.newInstance(new Object[] { new DataInputStream(new ByteArrayInputStream(bytes)), this }));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    UniformMaterial m = new UniformMaterial();
                    m.setName("<unreadable>");
                    materials.addElement(m);
                    errorsLoading = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new IOException();
            }
        }
        count = in.readInt();
        textures = new Vector(count);
        for (i = 0; i < count; i++) {
            try {
                String classname = in.readUTF();
                int len = in.readInt();
                byte bytes[] = new byte[len];
                in.readFully(bytes);
                cls = ModellingApp.getClass(classname);
                try {
                    if (cls == null) throw new IOException("Unknown class: " + classname);
                    con = cls.getConstructor(new Class[] { DataInputStream.class, Scene.class });
                    textures.addElement(con.newInstance(new Object[] { new DataInputStream(new ByteArrayInputStream(bytes)), this }));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    UniformTexture t = new UniformTexture();
                    t.setName("<unreadable>");
                    textures.addElement(t);
                    errorsLoading = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new IOException();
            }
        }
        count = in.readInt();
        objects = new Vector(count);
        table = new Hashtable(count);
        for (i = 0; i < count; i++) objects.addElement(readObjectFromFile(in, table, version));
        selection = new Vector();
        for (i = 0; i < objects.size(); i++) {
            ObjectInfo info = (ObjectInfo) objects.elementAt(i);
            int num = in.readInt();
            for (j = 0; j < num; j++) {
                ObjectInfo child = (ObjectInfo) objects.elementAt(in.readInt());
                info.addChild(child, j);
            }
        }
        environMode = (int) in.readShort();
        if (environMode == ENVIRON_SOLID) {
            environColor = new RGBColor(in);
            environTexture = (Texture) textures.elementAt(0);
            environMapping = environTexture.getDefaultMapping();
            environParamValue = new ParameterValue[0];
        } else {
            int texIndex = in.readInt();
            if (texIndex == -1) {
                environTexture = new LayeredTexture();
                String mapClassName = in.readUTF();
                if (!LayeredMapping.class.getName().equals(mapClassName)) throw new InvalidObjectException("");
                environMapping = environTexture.getDefaultMapping();
                ((LayeredMapping) environMapping).readFromFile(in, this);
            } else {
                environTexture = ((Texture) textures.elementAt(texIndex));
                try {
                    Class mapClass = ModellingApp.getClass(in.readUTF());
                    con = mapClass.getConstructor(new Class[] { DataInputStream.class, Texture.class });
                    environMapping = (TextureMapping) con.newInstance(new Object[] { in, environTexture });
                } catch (Exception ex) {
                    throw new IOException();
                }
            }
            environColor = new RGBColor(0.0f, 0.0f, 0.0f);
            environParamValue = new ParameterValue[environMapping.getParameters().length];
            if (version > 2) for (i = 0; i < environParamValue.length; i++) environParamValue[i] = Object3D.readParameterValue(in);
        }
        textureListeners = new Vector();
        materialListeners = new Vector();
        setTime(0.0);
    }
