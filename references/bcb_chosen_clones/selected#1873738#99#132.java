    private PrecomputedAnimatedModel loadPrecomputedModel_(URL url) {
        if (precompCache.containsKey(url.toExternalForm())) {
            return (precompCache.get(url.toExternalForm()).copy());
        }
        TextureLoader.getInstance().getTexture("");
        List<SharedGroup> frames = new ArrayList<SharedGroup>();
        Map<String, Animation> animations = new Hashtable<String, Animation>();
        if (url.toExternalForm().endsWith(".amo")) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String objFileName = reader.readLine();
                objFileName = url.toExternalForm().substring(0, url.toExternalForm().lastIndexOf("/")) + "/" + objFileName;
                frames = loadOBJFrames(objFileName);
                String line;
                while ((line = reader.readLine()) != null) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    String animName = tokenizer.nextToken();
                    int from = Integer.valueOf(tokenizer.nextToken());
                    int to = Integer.valueOf(tokenizer.nextToken());
                    tokenizer.nextToken();
                    animations.put(animName, new Animation(animName, from, to));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            frames = loadOBJFrames(url.toExternalForm());
        }
        PrecomputedAnimatedModel precompModel = new PrecomputedAnimatedModel(frames, animations);
        precompCache.put(url.toExternalForm(), precompModel);
        return (precompModel);
    }
