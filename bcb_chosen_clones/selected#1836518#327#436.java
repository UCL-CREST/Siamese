    public void initialize() {
        menuBar = new CommonMenuBar(getClient());
        initializeFrame();
        try {
            client.game.addGameListener(gameListener);
            Class<?> c = getClass().getClassLoader().loadClass(System.getProperty("megamek.client.ui.AWT.boardView", "megamek.client.ui.swing.BoardView1"));
            bv = (IBoardView) c.getConstructor(IGame.class).newInstance(client.game);
            bvc = bv.getComponent();
            bvc.setName("BoardView");
            bv.addBoardViewListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            doAlertDialog(Messages.getString("ClientGUI.FatalError.title"), Messages.getString("ClientGUI.FatalError.message") + e);
            die();
        }
        layoutFrame();
        frame.setVisible(true);
        menuBar.addActionListener(this);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                saveSettings();
                die();
            }
        });
        UnitLoadingDialog unitLoadingDialog = new UnitLoadingDialog(frame);
        if (!MechSummaryCache.getInstance().isInitialized()) {
            unitLoadingDialog.setVisible(true);
        }
        cb2 = new ChatterBox2(this, bv);
        bv.addDisplayable(cb2);
        bv.addKeyListener(cb2);
        uo = new UnitOverview(this);
        bv.addDisplayable(uo);
        Dimension screenSize = frame.getToolkit().getScreenSize();
        int x;
        int y;
        int h;
        int w;
        mechW = new JDialog(frame, Messages.getString("ClientGUI.MechDisplay"), false);
        x = GUIPreferences.getInstance().getDisplayPosX();
        y = GUIPreferences.getInstance().getDisplayPosY();
        h = GUIPreferences.getInstance().getDisplaySizeHeight();
        w = GUIPreferences.getInstance().getDisplaySizeWidth();
        if ((x + w) > screenSize.width) {
            x = 0;
            w = Math.min(w, screenSize.width);
        }
        if ((y + h) > screenSize.height) {
            y = 0;
            h = Math.min(h, screenSize.height);
        }
        mechW.setLocation(x, y);
        mechW.setSize(w, h);
        mechW.setResizable(true);
        mechW.addWindowListener(this);
        mechD = new MechDisplay(this);
        mechD.addMechDisplayListener(bv);
        mechW.add(mechD);
        Ruler.color1 = GUIPreferences.getInstance().getRulerColor1();
        Ruler.color2 = GUIPreferences.getInstance().getRulerColor2();
        ruler = new Ruler(frame, client, bv);
        x = GUIPreferences.getInstance().getRulerPosX();
        y = GUIPreferences.getInstance().getRulerPosY();
        h = GUIPreferences.getInstance().getRulerSizeHeight();
        w = GUIPreferences.getInstance().getRulerSizeWidth();
        if ((x + w) > screenSize.width) {
            x = 0;
            w = Math.min(w, screenSize.width);
        }
        if ((y + h) > screenSize.height) {
            y = 0;
            h = Math.min(h, screenSize.height);
        }
        ruler.setLocation(x, y);
        ruler.setSize(w, h);
        minimapW = new JDialog(frame, Messages.getString("ClientGUI.MiniMap"), false);
        x = GUIPreferences.getInstance().getMinimapPosX();
        y = GUIPreferences.getInstance().getMinimapPosY();
        try {
            minimap = new MiniMap(minimapW, this, bv);
        } catch (IOException e) {
            e.printStackTrace();
            doAlertDialog(Messages.getString("ClientGUI.FatalError.title"), Messages.getString("ClientGUI.FatalError.message1") + e);
            die();
        }
        h = minimap.getSize().height;
        w = minimap.getSize().width;
        if (((x + 10) >= screenSize.width) || ((x + w) < 10)) {
            x = screenSize.width - w;
        }
        if (((y + 10) > screenSize.height) || ((y + h) < 10)) {
            y = screenSize.height - h;
        }
        minimapW.setLocation(x, y);
        minimapW.addWindowListener(this);
        minimapW.add(minimap);
        cb = new ChatterBox(this);
        cb.setChatterBox2(cb2);
        cb2.setChatterBox(cb);
        client.changePhase(IGame.Phase.PHASE_UNKNOWN);
        mechSelectorDialog = new MechSelectorDialog(this, unitLoadingDialog);
        customFSDialog = new CustomFighterSquadronDialog(this, unitLoadingDialog);
        randomArmyDialog = new RandomArmyDialog(this);
        randomSkillDialog = new RandomSkillDialog(this);
        randomNameDialog = new RandomNameDialog(this);
        new Thread(mechSelectorDialog, "Mech Selector Dialog").start();
    }
