package com.kerware.modelrefac.service;

import com.kerware.modelrefac.config.Constantes;

public final class ContributionService {
    public long calculate(double revenuFiscal, boolean isCouple) {
        int[] limites = Constantes.TRANCHES_CEHR;
        double[] taux = isCouple ? Constantes.TAUX_CEHR_COUPLE : Constantes.TAUX_CEHR_CELIB;
        double contrib = 0;
        for (int i = 0; i < taux.length; i++) {
            double base = Math.min(revenuFiscal, limites[i+1]) - limites[i];
            if (base <= 0){ break;} 
            contrib += base * taux[i];
        }
        return Math.round(contrib);
    }
}