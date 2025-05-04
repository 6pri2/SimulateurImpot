package com.kerware.modelrefac.config;

public class Constantes {

    public static final int[] TRANCHES = {0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE};
    public static final double[] TAUX = {0.0, 0.11, 0.3, 0.41, 0.45};

    public static final int[] TRANCHES_CEHR = {0, 250000, 500000, 1000000, Integer.MAX_VALUE};
    public static final double[] TAUX_CEHR_CELIB = {0.0, 0.03, 0.04, 0.04};
    public static final double[] TAUX_CEHR_COUPLE = {0.0, 0.0, 0.03, 0.04};

    public static final int ABATTEMENT_MAX = 14171;
    public static final int ABATTEMENT_MIN = 495;
    public static final double TAUX_ABATTEMENT = 0.1;
    public static final double MOITIE = 0.5;

    public static final double PLAFOND_DEMI_PART = 1759;
    public static final double SEUIL_DECOTE_SEUL = 1929;
    public static final double SEUIL_DECOTE_COUPLE = 3191;
    public static final double DECOTE_MAX_SEUL = 873;
    public static final double DECOTE_MAX_COUPLE = 1444;
    public static final double TAUX_DECOTE = 0.4525;

}
