class SqrtAlgorithm {
    SqrtAlgorithm() {
        super();
    }
    public static void main ( String[] a ) {
        double d = 0.0;
        label0: while ( d <= 10000.0 ) {
            int i = 0;
            int i0 = 0;
            long j = 0L;
            long j0 = 0L;
            int i1 = 0;
            int i2 = 0;
            int i3 = 0;
            System.out.print ( "sqrt(" );
            System.out.print ( d );
            System.out.print ( ") = " );
            java.io.PrintStream a0 = System.out;
            long j1 = ( long ) d;
            long j2 = ( long ) ( ( d - ( double ) j1 ) * 1000000.0 );
            int i4 = 0;
            long j3 = j1;
            while ( j3 >= 10L ) {
                long j4 = j3 / 10L;
                i4 = i4 + 1;
                j3 = j4;
            }
            int i5 = i4 + 1;
            int i6 = 0;
            long j5 = j2;
            while ( j5 >= 10L ) {
                long j6 = j5 / 10L;
                i6 = i6 + 1;
                j5 = j6;
            }
            int i7 = i6 + 1;
            if ( i5 % 2 != 1 ) {
                i = i5;
                i0 = i7;
                j = j1;
                j0 = j2;
                i1 = 0;
                i2 = 0;
                i3 = 0;
            } else {
                i = i5;
                i0 = i7;
                j = j1;
                j0 = j2;
                i1 = 0;
                i2 = 1;
                i3 = 0;
            }
            while ( true ) {
                int i8 = 0;
                int i9 = 0;
                if ( i2 != 1 ) {
                    i8 = i - 2;
                    i9 = 0;
                } else {
                    i8 = i - 1;
                    i9 = 1;
                }
                int i10 = 0;
                int i11 = 1;
                while ( i10 < i8 ) {
                    int i12 = i11 * 10;
                    i10 = i10 + 1;
                    i11 = i12;
                }
                int i13 = ( int ) j / i11;
                int i14 = i1 << 1;
                int i15 = 1;
                int i16 = i14;
                while ( true ) {
                    int i17 = 0;
                    int i18 = 0;
                    double d0 = 0.0;
                    label4: {
                        label5: {
                            label6: {
                                int i19 = 0;
                                label7: {
                                    label8: {
                                        if ( i16 == 0 ) {
                                            break label8;
                                        }
                                        if ( i13 - i15 * ( i16 * 10 + i15 ) >= 0 ) {
                                            i19 = i16;
                                            break label7;
                                        } else {
                                            break label6;
                                        }
                                    }
                                    if ( i13 - i15 * i15 >= 0 ) {
                                        i19 = 0;
                                    } else {
                                        break label5;
                                    }
                                }
                                i15 = i15 + 1;
                                i16 = i19;
                                continue;
                            }
                            i17 = i16 / 2 * 10 + i15 - 1;
                            break label4;
                        }
                        i17 = i15 - 1;
                    }
                    int i20 = i17 / 10;
                    int i21 = i17 % 10;
                    long j7 = ( i20 != 0 ) ? j - ( long ) ( 0 - ( ( i20 << 1 ) * 10 + i21 ) * i21 * i11 ) : j - ( long ) ( 0 - i17 * i17 * i11 );
                    int i22 = ( j7 < 0L ) ? -1 : ( j7 == 0L ) ? 0 : 1;
                    label1: {
                        label3: {
                            if ( i22 != 0 ) {
                                break label3;
                            }
                            if ( j0 != 0L ) {
                                break label3;
                            }
                            if ( i8 <= 0 ) {
                                i18 = i17;
                                break label1;
                            } else {
                                int i23 = i8 / 2;
                                int i24 = 0;
                                int i25 = 1;
                                while ( i24 < i23 ) {
                                    int i26 = i25 * 10;
                                    i24 = i24 + 1;
                                    i25 = i26;
                                }
                                i18 = i17 * i25;
                                break label1;
                            }
                        }
                        int i27 = ( i9 != 1 ) ? i + -2 : i + -1;
                        if ( i27 > 0 ) {
                            i = i27;
                            j = j7;
                            i1 = i17;
                            i2 = 0;
                            break;
                        }
                        int i28 = ( j7 < 0L ) ? -1 : ( j7 == 0L ) ? 0 : 1;
                        label2: {
                            if ( i28 > 0 ) {
                                break label2;
                            }
                            if ( j0 <= 0L ) {
                                i18 = i17;
                                break label1;
                            }
                        }
                        if ( i3 >= 5 ) {
                            i18 = i17;
                        } else {
                            int i29 = 0;
                            long j8 = 0L;
                            long j9 = 0L;
                            int i30 = i3 + 1;
                            long j10 = j7 * 100L;
                            if ( j0 <= 0L ) {
                                i29 = i0;
                                j8 = j10;
                                j9 = j0;
                            } else {
                                int i31 = i0 + -2;
                                int i32 = 0;
                                int i33 = 1;
                                while ( i32 < i31 ) {
                                    int i34 = i33 * 10;
                                    i32 = i32 + 1;
                                    i33 = i34;
                                }
                                long j11 = j10 + j0 / ( long ) i33;
                                long j12 = j0 % ( long ) i33;
                                i29 = i31;
                                j8 = j11;
                                j9 = j12;
                            }
                            i = i27 + 2;
                            i0 = i29;
                            j = j8;
                            j0 = j9;
                            i1 = i17;
                            i2 = 0;
                            i3 = i30;
                            break;
                        }
                    }
                    if ( i3 != 0 ) {
                        int i35 = 0;
                        int i36 = 1;
                        while ( i35 < i3 ) {
                            int i37 = i36 * 10;
                            i35 = i35 + 1;
                            i36 = i37;
                        }
                        d0 = ( double ) i18 / ( double ) i36;
                    } else {
                        d0 = ( double ) i18;
                    }
                    a0.print ( d0 );
                    System.out.print ( ", " );
                    System.out.println ( Math.sqrt ( d ) );
                    d = d + 50.0;
                    continue label0;
                }
            }
        }
    }
}
