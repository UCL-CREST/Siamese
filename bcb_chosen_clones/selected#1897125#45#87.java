    public MapConfigFileReader(String path, Map enclosingMap, Model model, GameFlow flow) {
        String line;
        logger.info(String.format("Loading map config %s", path));
        this.flow = flow;
        this.model = model;
        this.tiles = new ArrayList<Tile>();
        this.tileObjects = new ArrayList<TileObject>();
        InputStream stream = ResourceLoader.getInstance().getStream(path);
        Scanner s = new Scanner(stream);
        mapName = s.nextLine();
        backgroundPath = s.nextLine();
        line = s.nextLine();
        String[] arr = line.split(",");
        int x = Integer.parseInt(arr[0]);
        int y = Integer.parseInt(arr[1]);
        vector = new Vector(x, y);
        if (enclosingMap != null) {
            while (s.hasNextLine()) {
                line = s.nextLine();
                if (line.isEmpty()) continue;
                arr = line.split(" ");
                x = Integer.parseInt(arr[1].split(",")[0]);
                y = Integer.parseInt(arr[1].split(",")[1]);
                Tile t = enclosingMap.getTile(new Vector(x, y));
                TileObject obj = null;
                try {
                    if (arr.length == 2) obj = (TileObject) Class.forName(arr[0].trim()).getConstructor(Tile.class).newInstance(t); else {
                        HexDirection dir = HexDirection.valueOf(arr[2]);
                        Integer i = Integer.parseInt(arr[3]);
                        obj = (TileObject) Class.forName(arr[0].trim()).getConstructor(Tile.class, HexDirection.class, Integer.class).newInstance(t, dir, i);
                    }
                    tileObjects.add(obj);
                    if (obj instanceof Updateable) model.addUpdateable((Updateable) obj);
                    t.registerTileObject(obj);
                    this.tiles.add(t);
                } catch (Exception e) {
                    logger.error("Fail reading cfg line.", e);
                }
            }
        }
        s.close();
        logger.info(String.format("Map image is: %s", backgroundPath));
    }
