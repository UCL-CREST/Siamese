    public static void main(String[] args) {
        String email = "josh888@byu.net";
        String username = "josh8573";
        String password = "josh8573";
        String IDnumber = "3030";
        double[] apogee = { 1000 };
        double[] perigee = apogee;
        double[] inclination = { 58.0 };
        int[] trp_solmax = { 0, 1, 2 };
        double[] init_long_ascend = { 0 };
        double[] init_displ_ascend = { 0 };
        double[] displ_perigee_ascend = { 0 };
        double[] orbit_sect = null;
        boolean[] gtrn_weather = { false, true };
        boolean print_altitude = true;
        boolean print_inclination = false;
        boolean print_gtrn_weather = true;
        boolean print_ita = false;
        boolean print_ida = false;
        boolean print_dpa = false;
        ORBIT[] orbit_array;
        orbit_array = ORBIT.CreateOrbits(apogee, perigee, inclination, gtrn_weather, trp_solmax, init_long_ascend, init_displ_ascend, displ_perigee_ascend, orbit_sect, print_altitude, print_inclination, print_gtrn_weather, print_ita, print_ida, print_dpa);
        TRP[] trp_array = {};
        GTRN[] gtrn_array = {};
        if (orbit_array != null) {
            Vector trp_vector = new Vector();
            for (int i = 0; i < orbit_array.length; i++) {
                TRP temp_t = orbit_array[i].getTRP();
                if (temp_t != null) {
                    trp_vector.add(temp_t);
                }
            }
            if (trp_vector.size() != 0) {
                TRP[] trp_to_convert = new TRP[trp_vector.size()];
                trp_array = (TRP[]) trp_vector.toArray(trp_to_convert);
            }
            Vector gtrn_vector = new Vector();
            for (int i = 0; i < orbit_array.length; i++) {
                GTRN temp_g = orbit_array[i].getGTRN();
                if (temp_g != null) {
                    gtrn_vector.add(temp_g);
                }
            }
            if (gtrn_vector.size() != 0) {
                GTRN[] gtrn_to_convert = new GTRN[gtrn_vector.size()];
                gtrn_array = (GTRN[]) gtrn_vector.toArray(gtrn_to_convert);
            }
        }
        int[] flux_min_element = { 1 };
        int[] flux_max_element = { 92 };
        int[] weather_flux = { 00, 01, 11, 12, 13 };
        boolean print_weather = true;
        boolean print_min_elem = false;
        boolean print_max_elem = false;
        ORBIT[] orbit_array_into_flux = orbit_array;
        FLUX[] flux_array;
        flux_array = FLUX.CreateFLUX_URF(flux_min_element, flux_max_element, weather_flux, orbit_array_into_flux, print_weather, print_min_elem, print_max_elem);
        FLUX[] flx_objects_into_trans = flux_array;
        int[] units = { 1 };
        double[] thickness = { 100 };
        boolean print_shielding = false;
        TRANS[] trans_array;
        trans_array = TRANS.CreateTRANS_URF(flx_objects_into_trans, units, thickness, print_shielding);
        URFInterface[] input_files_for_letspec = trans_array;
        int[] letspec_min_element = { 2 };
        int[] letspec_max_element = { 0 };
        double[] min_energy_value = { .1 };
        boolean[] diff_spect = { false };
        boolean print_min_energy = false;
        LETSPEC[] letspec_array;
        letspec_array = LETSPEC.CreateLETSPEC_URF(input_files_for_letspec, letspec_min_element, letspec_max_element, min_energy_value, diff_spect, print_min_energy);
        URFInterface[] input_files_for_pup = trans_array;
        double[] pup_params = { 20, 4, 0.5, .0153 };
        PUP_Device[][] pup_device_array = { { new PUP_Device("sample", null, null, 50648448, 4, pup_params) } };
        boolean print_bits_in_device_pup = false;
        boolean print_weibull_onset_pup = false;
        boolean print_weibull_width_pup = false;
        boolean print_weibull_exponent_pup = false;
        boolean print_weibull_cross_sect_pup = false;
        PUP[] pup_array;
        pup_array = PUP.CreatePUP_URF(input_files_for_pup, pup_device_array, print_bits_in_device_pup, print_weibull_onset_pup, print_weibull_width_pup, print_weibull_exponent_pup, print_weibull_cross_sect_pup);
        LETSPEC[] let_objects_into_hup = letspec_array;
        double[][] weib_params = { { 9.74, 30.25, 2.5, 22600 }, { 9.74, 30.25, 2.5, 2260 }, { 9.74, 30.25, 2.5, 226 }, { 9.74, 30.25, 2.5, 22.6 }, { 9.74, 30.25, 2.5, 2.26 }, { 9.74, 30.25, 2.5, .226 }, { 9.74, 30.25, 2.5, .0226 } };
        HUP_Device[][] hup_device_array = new HUP_Device[7][1];
        double z_depth = (float) 0.01;
        for (int i = 0; i < 7; i++) {
            hup_device_array[i][0] = new HUP_Device("sample", null, null, 0, 0, (Math.sqrt(weib_params[i][3]) / 100), 0, (int) Math.pow(10, i), 4, weib_params[i]);
            z_depth += .01;
        }
        boolean print_label = false;
        boolean print_commenta = false;
        boolean print_commentb = false;
        boolean print_RPP_x = false;
        boolean print_RPP_y = false;
        boolean print_RPP_z = false;
        boolean print_funnel_length = false;
        boolean print_bits_in_device_hup = true;
        boolean print_weibull_onset_hup = false;
        boolean print_weibull_width_hup = false;
        boolean print_weibull_exponent_hup = false;
        boolean print_weibull_cross_sect_hup = false;
        HUP[] hup_array;
        hup_array = HUP.CreateHUP_URF(let_objects_into_hup, hup_device_array, print_label, print_commenta, print_commentb, print_RPP_x, print_RPP_y, print_RPP_z, print_funnel_length, print_bits_in_device_hup, print_weibull_onset_hup, print_weibull_width_hup, print_weibull_exponent_hup, print_weibull_cross_sect_hup);
        System.out.println("Finished creating User Request Files");
        int num_of_files = trp_array.length + gtrn_array.length + flux_array.length + trans_array.length + letspec_array.length + pup_array.length + hup_array.length;
        int index = 0;
        String[] files_to_upload = new String[num_of_files];
        for (int a = 0; a < trp_array.length; a++) {
            files_to_upload[index] = trp_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < gtrn_array.length; a++) {
            files_to_upload[index] = gtrn_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < flux_array.length; a++) {
            files_to_upload[index] = flux_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < trans_array.length; a++) {
            files_to_upload[index] = trans_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < letspec_array.length; a++) {
            files_to_upload[index] = letspec_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < pup_array.length; a++) {
            files_to_upload[index] = pup_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < hup_array.length; a++) {
            files_to_upload[index] = hup_array[a].getThisFileName();
            index++;
        }
        Logger log = Logger.getLogger(CreateAStudy.class);
        String host = "creme96.nrl.navy.mil";
        String user = "anonymous";
        String ftppass = email;
        Logger.setLevel(Level.ALL);
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.setRemoteHost(host);
            FTPMessageCollector listener = new FTPMessageCollector();
            ftp.setMessageListener(listener);
            log.info("Connecting");
            ftp.connect();
            log.info("Logging in");
            ftp.login(user, ftppass);
            log.debug("Setting up passive, ASCII transfers");
            ftp.setConnectMode(FTPConnectMode.ACTIVE);
            ftp.setType(FTPTransferType.BINARY);
            log.info("Putting file");
            for (int u = 0; u < files_to_upload.length; u++) {
                ftp.put(files_to_upload[u], files_to_upload[u]);
            }
            log.info("Quitting client");
            ftp.quit();
            log.debug("Listener log:");
            log.info("Test complete");
        } catch (Exception e) {
            log.error("Demo failed", e);
            e.printStackTrace();
        }
        System.out.println("Finished FTPing User Request Files to common directory");
        Upload_Files.upload(files_to_upload, username, password, IDnumber);
        System.out.println("Finished transfering User Request Files to your CREME96 personal directory");
        RunRoutines.routines(files_to_upload, username, password, IDnumber);
        System.out.println("Finished running all of your uploaded routines");
    }
