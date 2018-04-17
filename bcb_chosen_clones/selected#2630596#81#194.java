    public static Cursor load(URL url, String descriptor) {
        if (url == null) {
            log.log(Level.WARNING, "Trying to load a cursor with a null url.");
            return null;
        }
        String cursorFile = url.getFile();
        BufferedReader reader = null;
        int lineNumber = 0;
        try {
            DirectoryTextureLoader loader;
            URL cursorUrl;
            if (cursorFile.endsWith(cursorDescriptorFile)) {
                cursorUrl = url;
                Cursor cached = cursorCache.get(url);
                if (cached != null) return cached;
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                loader = new DirectoryTextureLoader(url, false);
            } else if (cursorFile.endsWith(cursorArchiveFile)) {
                loader = new DirectoryTextureLoader(url, true);
                if (descriptor == null) descriptor = defaultDescriptorFile;
                cursorUrl = loader.makeUrl(descriptor);
                Cursor cached = cursorCache.get(url);
                if (cached != null) return cached;
                ZipInputStream zis = new ZipInputStream(url.openStream());
                ZipEntry entry;
                boolean found = false;
                while ((entry = zis.getNextEntry()) != null) {
                    if (descriptor.equals(entry.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IOException("Descriptor file \"" + descriptor + "\" was not found.");
                }
                reader = new BufferedReader(new InputStreamReader(zis));
            } else {
                log.log(Level.WARNING, "Invalid cursor fileName \"{0}\".", cursorFile);
                return null;
            }
            Cursor cursor = new Cursor();
            cursor.url = cursorUrl;
            List<Integer> delays = new ArrayList<Integer>();
            List<String> frameFileNames = new ArrayList<String>();
            Map<String, Texture> textureCache = new HashMap<String, Texture>();
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                int commentIndex = line.indexOf(commentString);
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex);
                }
                StringTokenizer tokens = new StringTokenizer(line, delims);
                if (!tokens.hasMoreTokens()) continue;
                String prefix = tokens.nextToken();
                if (prefix.equals(hotSpotXPrefix)) {
                    cursor.hotSpotOffset.x = Integer.valueOf(tokens.nextToken());
                } else if (prefix.equals(hotSpotYPrefix)) {
                    cursor.hotSpotOffset.y = Integer.valueOf(tokens.nextToken());
                } else if (prefix.equals(timePrefix)) {
                    delays.add(Integer.valueOf(tokens.nextToken()));
                    if (tokens.nextToken().equals(imagePrefix)) {
                        String file = tokens.nextToken("");
                        file = file.substring(file.indexOf('=') + 1);
                        file.trim();
                        frameFileNames.add(file);
                        if (textureCache.get(file) == null) {
                            textureCache.put(file, loader.loadTexture(file));
                        }
                    } else {
                        throw new NoSuchElementException();
                    }
                }
            }
            cursor.frameFileNames = frameFileNames.toArray(new String[0]);
            cursor.textureCache = textureCache;
            cursor.delays = new int[delays.size()];
            cursor.images = new Image[frameFileNames.size()];
            cursor.textures = new Texture[frameFileNames.size()];
            for (int i = 0; i < cursor.frameFileNames.length; i++) {
                cursor.textures[i] = textureCache.get(cursor.frameFileNames[i]);
                cursor.images[i] = cursor.textures[i].getImage();
                cursor.delays[i] = delays.get(i);
            }
            if (delays.size() == 1) cursor.delays = null;
            if (cursor.images.length == 0) {
                log.log(Level.WARNING, "The cursor has no animation frames.");
                return null;
            }
            cursor.width = cursor.images[0].getWidth();
            cursor.height = cursor.images[0].getHeight();
            cursorCache.put(cursor.url, cursor);
            return cursor;
        } catch (MalformedURLException mue) {
            log.log(Level.WARNING, "Unable to load cursor.", mue);
        } catch (IOException ioe) {
            log.log(Level.WARNING, "Unable to load cursor.", ioe);
        } catch (NumberFormatException nfe) {
            log.log(Level.WARNING, "Numerical error while parsing the " + "file \"{0}\" at line {1}", new Object[] { url, lineNumber });
        } catch (IndexOutOfBoundsException ioobe) {
            log.log(Level.WARNING, "Error, \"=\" expected in the file \"{0}\" at line {1}", new Object[] { url, lineNumber });
        } catch (NoSuchElementException nsee) {
            log.log(Level.WARNING, "Error while parsing the file \"{0}\" at line {1}", new Object[] { url, lineNumber });
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    log.log(Level.SEVERE, "Unable to close the steam.", ioe);
                }
            }
        }
        return null;
    }
