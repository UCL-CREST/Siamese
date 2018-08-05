    public BufferedImage extract() throws DjatokaException {
        boolean useRegion = false;
        int left = 0;
        int top = 0;
        int width = 50;
        int height = 50;
        boolean useleftDouble = false;
        Double leftDouble = 0.0;
        boolean usetopDouble = false;
        Double topDouble = 0.0;
        boolean usewidthDouble = false;
        Double widthDouble = 0.0;
        boolean useheightDouble = false;
        Double heightDouble = 0.0;
        if (params.getRegion() != null) {
            StringTokenizer st = new StringTokenizer(params.getRegion(), "{},");
            String token;
            if ((token = st.nextToken()).contains(".")) {
                topDouble = Double.parseDouble(token);
                usetopDouble = true;
            } else top = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                leftDouble = Double.parseDouble(token);
                useleftDouble = true;
            } else left = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                heightDouble = Double.parseDouble(token);
                useheightDouble = true;
            } else height = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                widthDouble = Double.parseDouble(token);
                usewidthDouble = true;
            } else width = Integer.parseInt(token);
            useRegion = true;
        }
        try {
            if (is != null) {
                File f = File.createTempFile("tmp", ".jp2");
                f.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(f);
                sourceFile = f.getAbsolutePath();
                IOUtils.copyStream(is, fos);
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            throw new DjatokaException(e);
        }
        try {
            Jp2_source inputSource = new Jp2_source();
            Kdu_compressed_source input = null;
            Jp2_family_src jp2_family_in = new Jp2_family_src();
            Jp2_locator loc = new Jp2_locator();
            jp2_family_in.Open(sourceFile, true);
            inputSource.Open(jp2_family_in, loc);
            inputSource.Read_header();
            input = inputSource;
            Kdu_codestream codestream = new Kdu_codestream();
            codestream.Create(input);
            Kdu_channel_mapping channels = new Kdu_channel_mapping();
            if (inputSource.Exists()) channels.Configure(inputSource, false); else channels.Configure(codestream);
            int ref_component = channels.Get_source_component(0);
            Kdu_coords ref_expansion = getReferenceExpansion(ref_component, channels, codestream);
            Kdu_dims image_dims = new Kdu_dims();
            codestream.Get_dims(ref_component, image_dims);
            Kdu_coords imageSize = image_dims.Access_size();
            Kdu_coords imagePosition = image_dims.Access_pos();
            if (useleftDouble) left = imagePosition.Get_x() + (int) Math.round(leftDouble * imageSize.Get_x());
            if (usetopDouble) top = imagePosition.Get_y() + (int) Math.round(topDouble * imageSize.Get_y());
            if (useheightDouble) height = (int) Math.round(heightDouble * imageSize.Get_y());
            if (usewidthDouble) width = (int) Math.round(widthDouble * imageSize.Get_x());
            if (useRegion) {
                imageSize.Set_x(width);
                imageSize.Set_y(height);
                imagePosition.Set_x(left);
                imagePosition.Set_y(top);
            }
            int reduce = 1 << params.getLevelReductionFactor();
            imageSize.Set_x(imageSize.Get_x() * ref_expansion.Get_x());
            imageSize.Set_y(imageSize.Get_y() * ref_expansion.Get_y());
            imagePosition.Set_x(imagePosition.Get_x() * ref_expansion.Get_x() / reduce - ((ref_expansion.Get_x() / reduce - 1) / 2));
            imagePosition.Set_y(imagePosition.Get_y() * ref_expansion.Get_y() / reduce - ((ref_expansion.Get_y() / reduce - 1) / 2));
            Kdu_dims view_dims = new Kdu_dims();
            view_dims.Assign(image_dims);
            view_dims.Access_size().Set_x(imageSize.Get_x());
            view_dims.Access_size().Set_y(imageSize.Get_y());
            int region_buf_size = imageSize.Get_x() * imageSize.Get_y();
            int[] region_buf = new int[region_buf_size];
            Kdu_region_decompressor decompressor = new Kdu_region_decompressor();
            decompressor.Start(codestream, channels, -1, params.getLevelReductionFactor(), 16384, image_dims, ref_expansion, new Kdu_coords(1, 1), false, Kdu_global.KDU_WANT_OUTPUT_COMPONENTS);
            Kdu_dims new_region = new Kdu_dims();
            Kdu_dims incomplete_region = new Kdu_dims();
            Kdu_coords viewSize = view_dims.Access_size();
            incomplete_region.Assign(image_dims);
            int[] imgBuffer = new int[viewSize.Get_x() * viewSize.Get_y()];
            int[] kduBuffer = null;
            while (decompressor.Process(region_buf, image_dims.Access_pos(), 0, 0, region_buf_size, incomplete_region, new_region)) {
                Kdu_coords newOffset = new_region.Access_pos();
                Kdu_coords newSize = new_region.Access_size();
                newOffset.Subtract(view_dims.Access_pos());
                kduBuffer = region_buf;
                int imgBuffereIdx = newOffset.Get_x() + newOffset.Get_y() * viewSize.Get_x();
                int kduBufferIdx = 0;
                int xDiff = viewSize.Get_x() - newSize.Get_x();
                for (int j = 0; j < newSize.Get_y(); j++, imgBuffereIdx += xDiff) {
                    for (int i = 0; i < newSize.Get_x(); i++) {
                        imgBuffer[imgBuffereIdx++] = kduBuffer[kduBufferIdx++];
                    }
                }
            }
            BufferedImage image = new BufferedImage(imageSize.Get_x(), imageSize.Get_y(), BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, viewSize.Get_x(), viewSize.Get_y(), imgBuffer, 0, viewSize.Get_x());
            if (params.getRotationDegree() > 0) {
                image = ImageProcessingUtils.rotate(image, params.getRotationDegree());
            }
            decompressor.Native_destroy();
            channels.Native_destroy();
            if (codestream.Exists()) codestream.Destroy();
            inputSource.Native_destroy();
            input.Native_destroy();
            jp2_family_in.Native_destroy();
            return image;
        } catch (KduException e) {
            e.printStackTrace();
            throw new DjatokaException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DjatokaException(e);
        }
    }
