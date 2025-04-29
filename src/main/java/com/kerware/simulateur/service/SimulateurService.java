package com.kerware.simulateur.service;

import com.kerware.simulateur.config.Constantes;
import com.kerware.simulateur.model.SituationFamiliale;
import com.kerware.simulateur.util.ValidationUtil;

public class SimulateurService {
    private int rev1, rev2, nbEnf, nbEnfH;
    private SituationFamiliale sit;
    private boolean parentIso;
    private double abattement, revenuFiscal;
    private long impBrutDecl, impBrutFoyer, decote, contribEx;

    private final AbattementService abtService = new AbattementService();
    private final ContributionService ceService = new ContributionService();
    private final ImpotService impService = new ImpotService();
    private final DecoteService decoteService = new DecoteService();

    public void setRevenus(int r1, int r2) {
        ValidationUtil.checkNonNegative(r1, "Revenu 1 négatif");
        ValidationUtil.checkNonNegative(r2, "Revenu 2 négatif");
        this.rev1 = r1; this.rev2 = r2;
    }
    public void setSituation(SituationFamiliale s) { ValidationUtil.checkNotNull(s, "Situation nulle"); this.sit = s; }
    public void setEnfants(int enfants, int enfantsH) {
        ValidationUtil.checkNonNegative(enfants, "Nb enfants négatif");
        ValidationUtil.checkNonNegative(enfantsH, "Nb enfants handicapés négatif");
        this.nbEnf = enfants; this.nbEnfH = enfantsH;
    }
    public void setParentIsole(boolean pi) { this.parentIso = pi; }

    public void calculate() {
        // Abattement
        abattement = abtService.calculate(rev1, rev2, sit);
        revenuFiscal = rev1 + rev2 - abattement;
        if (revenuFiscal < 0) revenuFiscal = 0;
        // Parts
        int partsDecl = (sit == SituationFamiliale.MARIE || sit == SituationFamiliale.PACSE) ? 2 : 1;
        double parts = partsDecl + nbEnf * 0.5 + (nbEnf>2 ? (nbEnf-2)*1.0 : 0) + nbEnfH*0.5 + (parentIso && nbEnf>0 ? 0.5:0);

        // Contribution exceptionnelle
        contribEx = ceService.calculate(revenuFiscal, partsDecl==2);

        // Impôt brut
        impBrutDecl = impService.calculateBrut(revenuFiscal/partsDecl, partsDecl);
        impBrutFoyer = impService.calculateBrut(revenuFiscal/parts, (int)Math.round(parts));

        // Plafonnement
        double diffPts = parts - partsDecl;
        double plafond = (diffPts/0.5) * Constantes.PLAFOND_DEMI_PART;
        if (impBrutDecl - impBrutFoyer > plafond) impBrutFoyer = Math.round(impBrutDecl - plafond);

        // Décote
        decote = decoteService.apply(impBrutFoyer, partsDecl);
        long impotNet = impBrutFoyer - decote + contribEx;
        this.impBrutFoyer = impotNet;
    }

    public double getAbattement() { return abattement; }
    public double getRevenuFiscal() { return revenuFiscal; }
    public long getContribExceptionnelle() { return contribEx; }
    public long getDecote() { return decote; }
    public long getImpotNet() { return impBrutFoyer; }
}