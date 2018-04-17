        @Override
        protected Uri doInBackground(File... dests) {
            publishProgress(0);
            ErrorReporter.getInstance().addCustomData("nbPics", "" + mAdapter.getCount());
            ErrorReporter.getInstance().addCustomData("ExportSize", mPictureSize.name());
            File album;
            if (mAlbumType.equals(AlbumTypes.MAIL)) {
                ErrorReporter.getInstance().addCustomData("Format", "Mail Attachments");
                album = dests[0];
                int count = mAdapter.mContentModel.size();
                int itemNumber = 0;
                synchronized (mAdapter.mContentModel) {
                    for (AlbumItem item : mAdapter.mContentModel) {
                        ErrorReporter.getInstance().addCustomData("CurrentPic", "" + itemNumber);
                        try {
                            String picName = new Formatter().format("img%04d.jpg", itemNumber, item.uri.getLastPathSegment()).toString();
                            Bitmap bmp = BitmapLoader.load(getApplicationContext(), item.uri, mPictureSize.getWidth(), mPictureSize.getHeight(), Bitmap.Config.ARGB_8888, false);
                            if (bmp != null) {
                                ErrorReporter.getInstance().addCustomData("Apply rotation ?", "" + (item.rotation % 360 != 0));
                                if (item.rotation % 360 != 0) {
                                    bmp = BitmapUtil.rotate(bmp, item.rotation);
                                }
                                OutputStream out = new FileOutputStream(new File(album, picName));
                                ErrorReporter.getInstance().addCustomData("bmp is null ?", "" + (bmp == null));
                                bmp.compress(CompressFormat.JPEG, mPictureQuality, out);
                                mContentFileBuilder.put(picName, item.caption);
                                bmp.recycle();
                                out.close();
                            } else {
                                ErrorReporter.getInstance().addCustomData("item.uri", item.uri.toString());
                                ErrorReporter.getInstance().addCustomData("mPictureSize.getWidth()", "" + mPictureSize.getWidth());
                                ErrorReporter.getInstance().addCustomData("mPictureSize.getHeight()", "" + mPictureSize.getHeight());
                                ErrorReporter.getInstance().handleException(new Exception("Could not load image while creating archive! (BitmapLoader result is null)"));
                                new Toaster(EmailAlbumEditor.this, R.string.album_creation_image_error, Toast.LENGTH_LONG).start();
                            }
                            itemNumber++;
                            publishProgress((int) (((float) itemNumber / (float) count) * 100));
                            System.gc();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Error while creating temp pics", e);
                        }
                    }
                }
            } else {
                CharSequence timestamp = "";
                if (mAddTimestamp) {
                    timestamp = DateFormat.format("_yyyyMMdd_hhmm", Calendar.getInstance());
                }
                String albumExtension = ".jar";
                ErrorReporter.getInstance().addCustomData("Format", "EmailAlbum");
                if (mAlbumType == AlbumTypes.ZIP) {
                    ErrorReporter.getInstance().addCustomData("Format", "Zip");
                    albumExtension = ".zip";
                }
                album = new File(dests[0], mAlbumName.replaceAll("\\W", "_") + timestamp + albumExtension);
                try {
                    int count = (mAlbumType == AlbumTypes.EMAILALBUM ? 14 : 0) + mAdapter.mContentModel.size() + 1;
                    ZipEntry entry = null;
                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(album));
                    int entryNumber = 0;
                    if (mAlbumType == AlbumTypes.EMAILALBUM) {
                        ZipInputStream in = new ZipInputStream(getAssets().open(getAssets().list("")[0]));
                        while ((entry = in.getNextEntry()) != null) {
                            out.putNextEntry(new ZipEntry(entry.getName()));
                            byte[] buffer = new byte[2048];
                            int bytesRead = 0;
                            while ((bytesRead = in.read(buffer)) >= 0) {
                                out.write(buffer, 0, bytesRead);
                            }
                            out.closeEntry();
                            in.closeEntry();
                            entryNumber++;
                            publishProgress((int) (((float) entryNumber / (float) count) * 100));
                        }
                        in.close();
                    }
                    String entryName = "";
                    int itemNumber = 0;
                    synchronized (mAdapter.mContentModel) {
                        for (AlbumItem item : mAdapter.mContentModel) {
                            ErrorReporter.getInstance().addCustomData("CurrentPic", "" + itemNumber);
                            entryName = new Formatter().format("img%04d_%s.jpg", itemNumber, item.uri.getLastPathSegment()).toString();
                            mContentFileBuilder.put(entryName, item.caption);
                            if (mAlbumType == AlbumTypes.EMAILALBUM) {
                                entry = new ZipEntry(ALBUM_PICTURES_PATH + entryName);
                            } else {
                                entry = new ZipEntry(entryName);
                            }
                            Bitmap bmp = BitmapLoader.load(getApplicationContext(), item.uri, mPictureSize.getWidth(), mPictureSize.getHeight(), Bitmap.Config.ARGB_8888, false);
                            if (bmp != null) {
                                ErrorReporter.getInstance().addCustomData("Apply rotation ?", "" + (item.rotation % 360 != 0));
                                if (item.rotation % 360 != 0) {
                                    bmp = BitmapUtil.rotate(bmp, item.rotation);
                                }
                                out.putNextEntry(entry);
                                ErrorReporter.getInstance().addCustomData("bmp is null ?", "" + (bmp == null));
                                bmp.compress(CompressFormat.JPEG, mPictureQuality, out);
                                bmp.recycle();
                                out.closeEntry();
                            } else {
                                ErrorReporter.getInstance().addCustomData("item.uri", item.uri.toString());
                                ErrorReporter.getInstance().addCustomData("mPictureSize.getWidth()", "" + mPictureSize.getWidth());
                                ErrorReporter.getInstance().addCustomData("mPictureSize.getHeight()", "" + mPictureSize.getHeight());
                                ErrorReporter.getInstance().handleException(new Exception("Could not load image while creating archive! (BitmapLoader result is null)"));
                                new Toaster(EmailAlbumEditor.this, R.string.album_creation_image_error, Toast.LENGTH_LONG).start();
                            }
                            itemNumber++;
                            publishProgress((int) (((float) (entryNumber + itemNumber) / (float) count) * 100));
                            System.gc();
                        }
                    }
                    if (mAlbumType == AlbumTypes.EMAILALBUM) {
                        entry = new ZipEntry(ALBUM_CONTENT_FILE);
                        out.putNextEntry(entry);
                        mContentFileBuilder.store(out, mAlbumName);
                    } else {
                        entry = new ZipEntry(ZIP_CONTENT_FILE);
                        out.putNextEntry(entry);
                        mContentFileBuilder.storeHumanReadable(out, mAlbumName);
                    }
                    out.closeEntry();
                    out.finish();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error while creating album", e);
                }
            }
            publishProgress(100);
            return Uri.fromFile(album);
        }
