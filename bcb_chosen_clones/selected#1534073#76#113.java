    public VisADGroup createSceneGraph(VisADCanvasJ2D c) throws DisplayException {
        VisADGroup root = getRoot();
        if (root != null) return root;
        try {
            Class[] param = new Class[] { DisplayRendererJ2D.class };
            Constructor mbConstructor = mouseBehaviorJ2DClass.getConstructor(param);
            mouse = (MouseBehaviorJ2D) mbConstructor.newInstance(new Object[] { this });
        } catch (Exception e) {
            throw new VisADError("cannot construct " + mouseBehaviorJ2DClass);
        }
        getDisplay().setMouseBehavior(mouse);
        box = new VisADAppearance();
        cursor = new VisADAppearance();
        root = createBasicSceneGraph(c, mouse, box, cursor);
        VisADLineArray box_array = new VisADLineArray();
        box_array.coordinates = box_verts;
        box_array.vertexCount = 8;
        float[] ctlBox = getRendererControl().getBoxColor();
        box.red = ctlBox[0];
        box.green = ctlBox[1];
        box.blue = ctlBox[2];
        box.color_flag = true;
        box.array = box_array;
        VisADGroup box_on = getBoxOnBranch();
        box_on.addChild(box);
        VisADLineArray cursor_array = new VisADLineArray();
        cursor_array.coordinates = cursor_verts;
        cursor_array.vertexCount = 4;
        float[] ctlCursor = getRendererControl().getCursorColor();
        cursor.red = ctlCursor[0];
        cursor.green = ctlCursor[1];
        cursor.blue = ctlCursor[2];
        cursor.color_flag = true;
        cursor.array = cursor_array;
        VisADGroup cursor_on = getCursorOnBranch();
        cursor_on.addChild(cursor);
        return root;
    }
