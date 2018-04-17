    private void collectImageFile(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
        final File file = new File(collectedDirectory, ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.image.name"));
        final FileOutputStream fos = new FileOutputStream(file);
        try {
            final FileChannel outChannel = fos.getChannel();
            try {
                final int numOfFaceObjects = faceObjects.size();
                progress.setLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectImages"), numOfFaceObjects);
                final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                final Charset charset = Charset.forName("ISO-8859-1");
                int i = 0;
                for (final FaceObject faceObject : faceObjects) {
                    final String face = faceObject.getFaceName();
                    final String path = archFaceProvider.getFilename(face);
                    try {
                        final FileInputStream fin = new FileInputStream(path);
                        try {
                            final FileChannel inChannel = fin.getChannel();
                            final long imageSize = inChannel.size();
                            byteBuffer.clear();
                            byteBuffer.put(("IMAGE " + (faceObjects.isIncludeFaceNumbers() ? i + " " : "") + imageSize + " " + face + "\n").getBytes(charset));
                            byteBuffer.flip();
                            outChannel.write(byteBuffer);
                            inChannel.transferTo(0L, imageSize, outChannel);
                        } finally {
                            fin.close();
                        }
                    } catch (final FileNotFoundException ignored) {
                        ACTION_BUILDER.showMessageDialog(progress.getParentComponent(), "archCollectErrorFileNotFound", path);
                        return;
                    } catch (final IOException e) {
                        ACTION_BUILDER.showMessageDialog(progress.getParentComponent(), "archCollectErrorIOException", path, e);
                        return;
                    }
                    if (i++ % 100 == 0) {
                        progress.setValue(i);
                    }
                }
                progress.setValue(faceObjects.size());
            } finally {
                outChannel.close();
            }
        } finally {
            fos.close();
        }
    }
