package com.kerware.simulateur.adapter;

import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.model.SituationFamiliale;
import com.kerware.simulateur.service.SimulateurService;


public class AdaptateurSimulateur implements ICalculateurImpot {
    private final SimulateurService service = new SimulateurService();

    @Override public void setRevenusNetDeclarant1(int rn) { service.setRevenus(rn, service.getRev2()); }
    @Override public void setRevenusNetDeclarant2(int rn) { service.setRevenus(service.getRev1(), rn); }

    @Override public void setSituationFamiliale(SituationFamiliale sf) { service.setSituation(sf); }
    @Override public void setNbEnfantsACharge(int n) { service.setEnfants(n, service.getNbEnfH()); }
    @Override public void setNbEnfantsSituationHandicap(int h) { service.setEnfants(service.getNbEnf(), h); }
    @Override public void setParentIsole(boolean pi) { service.setParentIsole(pi); }

    @Override public void calculImpotSurRevenuNet() { service.calculate(); }
    @Override public int getRevenuNetDeclatant1() { return service.getRev1(); }
    @Override public int getRevenuNetDeclatant2() { return service.getRev2(); }
    @Override public double getContribExceptionnelle() { return service.getContribExceptionnelle(); }
    @Override public int getRevenuFiscalReference() { return (int)service.getRevenuFiscal(); }
    @Override public int getAbattement() { return (int)service.getAbattement(); }
    @Override public double getNbPartsFoyerFiscal() { /* pas exposé */ return 0; }
    @Override public int getImpotAvantDecote() { return (int)(service.getImpBrutFoyer() + service.getDecote() - service.getContribExceptionnelle()); }
    @Override public int getDecote() { return (int)service.getDecote(); }
    @Override public int getImpotSurRevenuNet() { return (int)service.getImpotNet(); }
}