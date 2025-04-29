package com.kerware.simulateur.service;

import com.kerware.simulateur.config.Constantes;

import java.awt.*;

public class ImpotService {
    public long calculateBrut(double revenuImposable, int parts) {
        int[] limites = Constantes.TRANCHES;
        double[] taux = Constantes.TAUX;
        double imp = 0;
        for (int i = 0; i < taux.length; i++) {
            double base = Math.min(revenuImposable, limites[i+1]) - limites[i];
            if (base <= 0) break;
            imp += base * taux[i];
        }
        return Math.round(imp * parts);
    }
}