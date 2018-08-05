    public WpCustomSurface(Position position, SurfaceType surface, int level, int[][] description, boolean passing, int layer) throws WorldException {
        super(description[0].length, description.length + level, position);
        this.layer = layer;
        this.passing = passing;
        this.level = level;
        this.surface = surface;
        this.description = new int[x][y];
        for (int i = 0; i < description[0].length; i++) {
            for (int j = 0; j < description.length; j++) {
                this.description[i][j] = description[j][i];
            }
        }
    }
