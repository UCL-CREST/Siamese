    private BoardSquare createSquare(int x, int y, BoardSquare.SquareIdentifier identifier) {
        BoardSquare s;
        try {
            Constructor c = identifier.getSpecializedClass().getConstructor(squareConstructionArgs);
            s = (BoardSquare) c.newInstance(squareSize, identifier);
            s.createActionDialog();
            s.useBackgroundColor(JunesLogic.options.getUseBackgroundColor());
        } catch (Exception e) {
            s = new DefaultBoardSquare(squareSize, identifier);
        }
        s.setLocation(x, y);
        s.setSize(squareSize);
        gui.loadingProgress(1);
        return s;
    }
