package com.kerware.simulateur.service;

import com.kerware.simulateur.config.Constantes;
import com.kerware.simulateur.model.SituationFamiliale;

public class AbattementService {
    public double calculate(int rev1, int rev2, SituationFamiliale sit) {
        long a1 = Math.round(rev1 * Constantes.TAUX_ABATTEMENT);
        long a2 = Math.round(rev2 * Constantes.TAUX_ABATTEMENT);
        a1 = Math.min(Math.max(a1, Constantes.ABATTEMENT_MIN), Constantes.ABATTEMENT_MAX);
        if (sit == SituationFamiliale.MARIE || sit == SituationFamiliale.PACSE) {
            a2 = Math.min(Math.max(a2, Constantes.ABATTEMENT_MIN), Constantes.ABATTEMENT_MAX);
        } else {
            a2 = 0;
        }
        return a1 + a2;
    }
}