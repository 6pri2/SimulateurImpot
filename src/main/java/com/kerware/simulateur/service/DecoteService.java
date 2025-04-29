package com.kerware.simulateur.service;

import com.kerware.simulateur.config.Constantes;

public class DecoteService {
    public long apply(long impBrut, int partsDeclarants) {
        double seuil = partsDeclarants == 1 ? Constantes.SEUIL_DECOTE_SEUL : Constantes.SEUIL_DECOTE_COUPLE;
        double max = partsDeclarants == 1 ? Constantes.DECOTE_MAX_SEUL : Constantes.DECOTE_MAX_COUPLE;
        if (impBrut >= seuil) return 0;
        double decote = max - impBrut * Constantes.TAUX_DECOTE;
        decote = Math.min(decote, impBrut);
        return Math.round(decote);
    }
}