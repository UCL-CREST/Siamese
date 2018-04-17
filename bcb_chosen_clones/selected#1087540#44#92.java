    public void actionPerformed(ActionEvent evt) {
        logger.debug("Auto layout action starting...");
        try {
            layout = layout.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.debug("Created new Layout instance: " + layout);
        List<LayoutNode> nodes = new ArrayList<LayoutNode>();
        for (ContainerPane<?, ?> cp : getPlayPen().getSelectedContainers()) {
            if (cp instanceof LayoutNode) {
                nodes.add((LayoutNode) cp);
            }
        }
        List<LayoutNode> notLaidOut = extractLayoutNodes(getPlaypen());
        notLaidOut.removeAll(nodes);
        Point layoutAreaOffset = new Point();
        if (nodes.size() == 0 || nodes.size() == 1) {
            nodes = extractLayoutNodes(getPlaypen());
        } else if (nodes.size() != extractLayoutNodes(getPlaypen()).size()) {
            int maxWidth = 0;
            for (LayoutNode tp : notLaidOut) {
                int width = tp.getWidth() + tp.getX();
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
            layoutAreaOffset = new Point(maxWidth, 0);
        }
        List<LayoutEdge> edges = extractLayoutEdges(getPlaypen());
        logger.debug("About to do layout. nodes=" + nodes);
        logger.debug("About to do layout. edges=" + edges);
        Rectangle layoutArea = new Rectangle(layoutAreaOffset, layout.getNewArea(nodes));
        layout.setup(nodes, edges, layoutArea);
        LayoutAnimator anim = new LayoutAnimator(getPlaypen(), layout);
        anim.setAnimationEnabled(animationEnabled);
        anim.setFramesPerSecond(framesPerSecond);
        anim.startAnimation();
        Clip clip;
        try {
            if ((evt.getModifiers() & ActionEvent.SHIFT_MASK) != 0) {
                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(getClass().getResource("/sounds/boingoingoingoingoing.wav")));
                clip.start();
            }
        } catch (Exception ex) {
            logger.debug("Couldn't play sound. Sigh.", ex);
        }
    }
