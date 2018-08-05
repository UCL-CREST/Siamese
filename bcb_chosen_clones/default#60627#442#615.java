    public void getFlop() {
        char couleur = 'o';
        int[] pix = new int[54];
        BufferedImage image1 = this.bot.createScreenCapture(new Rectangle(i + 165, j + 97, 3, 18));
        PixelGrabber pg = new PixelGrabber(image1, 0, 0, 3, 18, pix, 0, 3);
        try {
            ImageIO.write(image1, "png", new File("coincoin.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            pg.grabPixels();
        } catch (InterruptedException d) {
            System.err.println("en attente des pixels");
        }
        if ((pix[53] >> 8 & 0xFF) == (pix[53] >> 16 & 0xFF) && (pix[53] >> 8 & 0xFF) == (pix[53] >> 0 & 0xFF)) {
            couleur = 's';
        } else if ((pix[53] >> 16 & 0xFF) > (pix[53] >> 8 & 0xFF) && (pix[53] >> 16 & 0xFF) > (pix[53] >> 0 & 0xFF)) {
            couleur = 'h';
        } else if ((pix[53] >> 0 & 0xFF) > (pix[53] >> 16 & 0xFF) && (pix[53] >> 0 & 0xFF) > (pix[53] >> 8 & 0xFF)) {
            couleur = 'd';
        } else {
            couleur = 'c';
        }
        if (pix[7] == -1 && pix[18] == pix[19] && pix[5] == pix[8] && pix[11] == pix[14]) {
            if (pix[20] != -1) {
                this.flop[0] = new Card('K', couleur);
            } else {
                this.flop[0] = new Card('J', couleur);
            }
        } else if (pix[15] == pix[18] && pix[3] == -1 && pix[20] != -1 & pix[35] != -1) {
            if (pix[20] == pix[19] && pix[19] == pix[16] && pix[0] != pix[1]) {
                this.flop[0] = new Card('T', couleur);
            } else if (pix[22] == -1) {
                if (pix[25] == -1) {
                    this.flop[0] = new Card('9', couleur);
                } else {
                    this.flop[0] = new Card('5', couleur);
                }
            } else if (pix[7] == -1) {
                if (pix[5] != -1) {
                    this.flop[0] = new Card('8', couleur);
                } else {
                    this.flop[0] = new Card('A', couleur);
                }
            } else {
                this.flop[0] = new Card('6', couleur);
            }
        } else if (pix[1] == pix[2] && pix[4] == pix[5] && pix[7] == pix[8] && pix[7] != pix[10]) {
            this.flop[0] = new Card('7', couleur);
        } else if (pix[2] == -1 && pix[15] != pix[16] && pix[4] == -1 && pix[4] == pix[5]) {
            this.flop[0] = new Card('4', couleur);
        } else {
            if (pix[15] == -1 && pix[18] == -1 && pix[14] == -1) {
                if (pix[7] != -1) {
                    this.flop[0] = new Card('2', couleur);
                } else {
                    this.flop[0] = new Card('3', couleur);
                }
            } else if (pix[2] == pix[4] && pix[7] != -1) {
                this.flop[0] = new Card('Q', couleur);
            }
        }
        image1 = this.bot.createScreenCapture(new Rectangle(i + 197, j + 97, 3, 18));
        pg = new PixelGrabber(image1, 0, 0, 3, 18, pix, 0, 3);
        try {
            pg.grabPixels();
        } catch (InterruptedException d) {
            System.err.println("en attente des pixels");
        }
        if ((pix[53] >> 8 & 0xFF) == (pix[53] >> 16 & 0xFF) && (pix[53] >> 8 & 0xFF) == (pix[53] >> 0 & 0xFF)) {
            couleur = 's';
        } else if ((pix[53] >> 16 & 0xFF) > (pix[53] >> 8 & 0xFF) && (pix[53] >> 16 & 0xFF) > (pix[53] >> 0 & 0xFF)) {
            couleur = 'h';
        } else if ((pix[53] >> 0 & 0xFF) > (pix[53] >> 16 & 0xFF) && (pix[53] >> 0 & 0xFF) > (pix[53] >> 8 & 0xFF)) {
            couleur = 'd';
        } else {
            couleur = 'c';
        }
        if (pix[7] == -1 && pix[18] == pix[19] && pix[5] == pix[8] && pix[11] == pix[14]) {
            if (pix[20] != -1) {
                this.flop[1] = new Card('K', couleur);
            } else {
                this.flop[1] = new Card('J', couleur);
            }
        } else if (pix[15] == pix[18] && pix[3] == -1 && pix[20] != -1 & pix[35] != -1) {
            if (pix[20] == pix[19] && pix[19] == pix[16] && pix[0] != pix[1]) {
                this.flop[1] = new Card('T', couleur);
            } else if (pix[22] == -1) {
                if (pix[25] == -1) {
                    this.flop[1] = new Card('9', couleur);
                } else {
                    this.flop[1] = new Card('5', couleur);
                }
            } else if (pix[7] == -1) {
                if (pix[5] != -1) {
                    this.flop[1] = new Card('8', couleur);
                } else {
                    this.flop[1] = new Card('A', couleur);
                }
            } else {
                this.flop[1] = new Card('6', couleur);
            }
        } else if (pix[1] == pix[2] && pix[4] == pix[5] && pix[7] == pix[8] && pix[7] != pix[10]) {
            this.flop[1] = new Card('7', couleur);
        } else if (pix[2] == -1 && pix[15] != pix[16] && pix[4] == -1 && pix[4] == pix[5]) {
            this.flop[1] = new Card('4', couleur);
        } else {
            if (pix[15] == -1 && pix[18] == -1 && pix[14] == -1) {
                if (pix[7] != -1) {
                    this.flop[1] = new Card('2', couleur);
                } else {
                    this.flop[1] = new Card('3', couleur);
                }
            } else if (pix[2] == pix[4] && pix[7] != -1) {
                this.flop[1] = new Card('Q', couleur);
            }
        }
        image1 = this.bot.createScreenCapture(new Rectangle(i + 229, j + 97, 3, 18));
        pg = new PixelGrabber(image1, 0, 0, 3, 18, pix, 0, 3);
        try {
            pg.grabPixels();
        } catch (InterruptedException d) {
            System.err.println("en attente des pixels");
        }
        if ((pix[53] >> 8 & 0xFF) == (pix[53] >> 16 & 0xFF) && (pix[53] >> 8 & 0xFF) == (pix[53] >> 0 & 0xFF)) {
            couleur = 's';
        } else if ((pix[53] >> 16 & 0xFF) > (pix[53] >> 8 & 0xFF) && (pix[53] >> 16 & 0xFF) > (pix[53] >> 0 & 0xFF)) {
            couleur = 'h';
        } else if ((pix[53] >> 0 & 0xFF) > (pix[53] >> 16 & 0xFF) && (pix[53] >> 0 & 0xFF) > (pix[53] >> 8 & 0xFF)) {
            couleur = 'd';
        } else {
            couleur = 'c';
        }
        if (pix[7] == -1 && pix[18] == pix[19] && pix[5] == pix[8] && pix[11] == pix[14]) {
            if (pix[20] != -1) {
                this.flop[2] = new Card('K', couleur);
            } else {
                this.flop[2] = new Card('J', couleur);
            }
        } else if (pix[15] == pix[18] && pix[3] == -1 && pix[20] != -1 & pix[35] != -1) {
            if (pix[20] == pix[19] && pix[19] == pix[16] && pix[0] != pix[1]) {
                this.flop[2] = new Card('T', couleur);
            } else if (pix[22] == -1) {
                if (pix[25] == -1) {
                    this.flop[2] = new Card('9', couleur);
                } else {
                    this.flop[2] = new Card('5', couleur);
                }
            } else if (pix[7] == -1) {
                if (pix[5] != -1) {
                    this.flop[2] = new Card('8', couleur);
                } else {
                    this.flop[2] = new Card('A', couleur);
                }
            } else {
                this.flop[2] = new Card('6', couleur);
            }
        } else if (pix[1] == pix[2] && pix[4] == pix[5] && pix[7] == pix[8] && pix[7] != pix[10]) {
            this.flop[2] = new Card('7', couleur);
        } else if (pix[2] == -1 && pix[15] != pix[16] && pix[4] == -1 && pix[4] == pix[5]) {
            this.flop[2] = new Card('4', couleur);
        } else {
            if (pix[15] == -1 && pix[18] == -1 && pix[14] == -1) {
                if (pix[7] != -1) {
                    this.flop[2] = new Card('2', couleur);
                } else {
                    this.flop[2] = new Card('3', couleur);
                }
            } else if (pix[2] == pix[4] && pix[7] != -1) {
                this.flop[2] = new Card('Q', couleur);
            }
        }
    }
