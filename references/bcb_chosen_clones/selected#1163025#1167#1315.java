    @Override
    public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand();
        if ("axis_rotation".equals(event)) {
            orientationFrame.dispose();
            orientationFrame.add(can_orientation);
            orientationFrame.pack();
            orientationFrame.setVisible(true);
            orientationFrame.setAlwaysOnTop(true);
            orientationFrame.setResizable(false);
            orientationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
        if ("zoomIn".equals(event)) {
            transformation.setInterpolation(true);
            setTransform(transformation.ZOOM_IN);
        }
        if ("zoomOut".equals(event)) {
            transformation.setInterpolation(true);
            setTransform(transformation.ZOOM_OUT);
        }
        if ("move_left".equals(event)) {
            transformation.setInterpolation(true);
            setTransform(transformation.MOVE_LEFT);
        }
        if ("move_right".equals(event)) {
            transformation.setInterpolation(true);
            setTransform(transformation.MOVE_RIGHT);
        }
        if ("move_up".equals(event)) {
            transformation.setInterpolation(true);
            setTransform(transformation.MOVE_UP);
        }
        if ("move_down".equals(event)) {
            transformation.setInterpolation(true);
            setTransform(transformation.MOVE_DOWN);
        }
        if ("rotate_left".equals(event)) {
            transformation.setInterpolation(true);
            if (transformation.getTransform() > 5) {
                Transform3D translation = new Transform3D();
                translation.setIdentity();
                Vector3d trans = new Vector3d();
                getObjTransformTransformation().get(trans);
                trans.negate();
                translation.setTranslation(trans);
                translation.mul(getObjTransformTransformation());
                setObjTransform(translation);
            }
            setTransform(transformation.ROT_LEFT);
        }
        if ("rotate_right".equals(event)) {
            transformation.setInterpolation(true);
            if (transformation.getTransform() > 5) {
                Transform3D translation = new Transform3D();
                translation.setIdentity();
                Vector3d trans = new Vector3d();
                getObjTransformTransformation().get(trans);
                trans.negate();
                translation.setTranslation(trans);
                translation.mul(getObjTransformTransformation());
                setObjTransform(translation);
            }
            setTransform(transformation.ROT_RIGHT);
        }
        if ("rotate_up".equals(event)) {
            transformation.setInterpolation(true);
            if (transformation.getTransform() > 5) {
                Transform3D translation = new Transform3D();
                translation.setIdentity();
                Vector3d trans = new Vector3d();
                getObjTransformTransformation().get(trans);
                trans.negate();
                translation.setTranslation(trans);
                translation.mul(getObjTransformTransformation());
                setObjTransform(translation);
            }
            setTransform(transformation.ROT_UP);
        }
        if ("rotate_down".equals(event)) {
            transformation.setInterpolation(true);
            if (transformation.getTransform() > 5) {
                Transform3D translation = new Transform3D();
                translation.setIdentity();
                Vector3d trans = new Vector3d();
                getObjTransformTransformation().get(trans);
                trans.negate();
                translation.setTranslation(trans);
                translation.mul(getObjTransformTransformation());
                setObjTransform(translation);
            }
            setTransform(transformation.ROT_DOWN);
        }
        if ("rotate_forward".equals(event)) {
            transformation.setInterpolation(true);
            if (transformation.getTransform() > 5) {
                Transform3D translation = new Transform3D();
                translation.setIdentity();
                Vector3d trans = new Vector3d();
                getObjTransformTransformation().get(trans);
                trans.negate();
                translation.setTranslation(trans);
                translation.mul(getObjTransformTransformation());
                setObjTransform(translation);
            }
            setTransform(transformation.ROT_FORWARD);
        }
        if ("rotate_backward".equals(event)) {
            transformation.setInterpolation(true);
            if (transformation.getTransform() > 5) {
                Transform3D translation = new Transform3D();
                translation.setIdentity();
                Vector3d trans = new Vector3d();
                getObjTransformTransformation().get(trans);
                trans.negate();
                translation.setTranslation(trans);
                translation.mul(getObjTransformTransformation());
                setObjTransform(translation);
            }
            setTransform(transformation.ROT_BACKWARD);
        }
        if ("move_home".equals(event)) {
            transformation.setTransform(-1);
            orbit.goHome();
            Transform3D transform = new Transform3D();
            objTransform.setTransform(transform);
            objTransform_orientation.setTransform(transform);
            orbit.setRotationCenter(new Point3d(0, 0, 0));
            setRotationCenter();
            centerSelf();
        }
        if ("screen_cap".equals(event)) {
            Robot robot;
            try {
                robot = new Robot();
                Point point = this.getLocationOnScreen();
                Rectangle rect = new Rectangle();
                Dimension dim = can.getSize();
                if (this.MAXIMIZED_BOTH != 0) {
                    rect.setBounds((int) point.getX() + 4, (int) point.getY() + 79, (int) dim.getWidth(), (int) dim.getHeight());
                } else {
                    rect.setBounds((int) point.getX() + 4, (int) point.getY() + 87, (int) dim.getWidth(), (int) dim.getHeight());
                }
                BufferedImage bufferedImage = robot.createScreenCapture(rect);
                new WriteGraphPicture().writeFile(bufferedImage);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }
    }
