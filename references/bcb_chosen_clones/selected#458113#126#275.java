    public Controller(String m_hostname, String team, boolean m_shouldexit) throws InternalException {
        m_received_messages = new ConcurrentLinkedQueue<ReceivedMessage>();
        m_fragmsgs = new ArrayList<String>();
        m_customizedtaunts = new HashMap<Integer, String>();
        m_nethandler = new CachingNetHandler();
        m_drawingpanel = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
        m_user = System.getProperty("user.name");
        m_chatbuffer = new StringBuffer();
        this.m_shouldexit = m_shouldexit;
        isChatPaused = false;
        isRunning = true;
        m_lastbullet = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(HogsConstants.FRAGMSGS_FILE));
            String str;
            while ((str = in.readLine()) != null) {
                m_fragmsgs.add(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String newFile = PathFinder.getCustsFile();
        boolean exists = (new File(newFile)).exists();
        Reader reader = null;
        if (exists) {
            try {
                reader = new FileReader(newFile);
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
        } else {
            Object[] options = { "Yes, create a .hogsrc file", "No, use default taunts" };
            int n = JOptionPane.showOptionDialog(m_window, "You do not have customized taunts in your home\n " + "directory.  Would you like to create a customizable file?", "Hogs Customization", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {
                try {
                    FileChannel srcChannel = new FileInputStream(HogsConstants.CUSTS_TEMPLATE).getChannel();
                    FileChannel dstChannel;
                    dstChannel = new FileOutputStream(newFile).getChannel();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    srcChannel.close();
                    dstChannel.close();
                    reader = new FileReader(newFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    reader = new FileReader(HogsConstants.CUSTS_TEMPLATE);
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                }
            }
        }
        try {
            m_netengine = NetEngine.forHost(m_user, m_hostname, 1820, m_nethandler);
            m_netengine.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (NetException e) {
            e.printStackTrace();
        }
        m_gamestate = m_netengine.getCurrentState();
        m_gamestate.setInChatMode(false);
        m_gamestate.setController(this);
        try {
            readFromFile(reader);
        } catch (NumberFormatException e3) {
            e3.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        } catch (InternalException e3) {
            e3.printStackTrace();
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice m_graphicsdevice = ge.getDefaultScreenDevice();
        m_window = new GuiFrame(m_drawingpanel, m_gamestate);
        m_graphics = null;
        try {
            m_graphics = new GraphicsEngine(m_drawingpanel, m_gamestate);
        } catch (InternalException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
        m_drawingpanel.addGLEventListener(m_graphics);
        m_physics = new Physics();
        if (team == null) {
            team = HogsConstants.TEAM_NONE;
        }
        if (!(team.toLowerCase().equals(HogsConstants.TEAM_NONE) || team.toLowerCase().equals(HogsConstants.TEAM_RED) || team.toLowerCase().equals(HogsConstants.TEAM_BLUE))) {
            throw new InternalException("Invalid team name!");
        }
        String orig_team = team;
        Craft local_craft = m_gamestate.getLocalCraft();
        if (m_gamestate.getNumCrafts() == 0) {
            local_craft.setTeamname(team);
        } else if (m_gamestate.isInTeamMode()) {
            if (team == HogsConstants.TEAM_NONE) {
                int red_craft = m_gamestate.getNumOnTeam(HogsConstants.TEAM_RED);
                int blue_craft = m_gamestate.getNumOnTeam(HogsConstants.TEAM_BLUE);
                String new_team;
                if (red_craft > blue_craft) {
                    new_team = HogsConstants.TEAM_BLUE;
                } else if (red_craft < blue_craft) {
                    new_team = HogsConstants.TEAM_RED;
                } else {
                    new_team = Math.random() > 0.5 ? HogsConstants.TEAM_BLUE : HogsConstants.TEAM_RED;
                }
                m_gamestate.getLocalCraft().setTeamname(new_team);
            } else {
                local_craft.setTeamname(team);
            }
        } else {
            local_craft.setTeamname(HogsConstants.TEAM_NONE);
            if (orig_team != null) {
                m_window.displayText("You cannot join a team, this is an individual game.");
            }
        }
        if (!local_craft.getTeamname().equals(HogsConstants.TEAM_NONE)) {
            m_window.displayText("You are joining the " + local_craft.getTeamname() + " team.");
        }
        m_drawingpanel.setSize(m_drawingpanel.getWidth(), m_drawingpanel.getHeight());
        m_middlepos = new java.awt.Point(m_drawingpanel.getWidth() / 2, m_drawingpanel.getHeight() / 2);
        m_curpos = new java.awt.Point(m_drawingpanel.getWidth() / 2, m_drawingpanel.getHeight() / 2);
        GuiKeyListener k_listener = new GuiKeyListener();
        GuiMouseListener m_listener = new GuiMouseListener();
        m_window.addKeyListener(k_listener);
        m_drawingpanel.addKeyListener(k_listener);
        m_window.addMouseListener(m_listener);
        m_drawingpanel.addMouseListener(m_listener);
        m_window.addMouseMotionListener(m_listener);
        m_drawingpanel.addMouseMotionListener(m_listener);
        m_drawingpanel.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent evt) {
                m_window.setMouseTrapped(false);
                m_window.returnMouseToCenter();
            }
        });
        m_window.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent evt) {
                m_window.setMouseTrapped(false);
                m_window.returnMouseToCenter();
            }
        });
        m_window.requestFocus();
    }
