    public Notation() {
        model = App.Project;
        App.addProjectObserver(this, App.Source.MODEL);
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Euterpe.ttf"));
            GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
            g.registerFont(font);
        } catch (IOException e) {
            GUIHelper.displayError(e);
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        NoteSystem noteSystem = new NoteSystem(model);
        notationToolBar = new NotationToolBar();
        JScrollPane scrollPane = new JScrollPane(noteSystem);
        add(notationToolBar, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }
