package com.kerware.modelrefac.service;

import com.kerware.modelrefac.config.Constantes;
import com.kerware.modelrefac.model.SituationFamiliale;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.*;

import java.text.MessageFormat;

class SimulateurServiceTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    void testSimulationComplete(
        int rev1,
        int rev2,
        String situation,
        int nbEnf,
        int nbEnfH,
        boolean parentIso,
        double expectedAbattement,
        double expectedRevenuFiscal,
        long expectedContribEx,
        long expectedImpBrutFoyer,
        long expectedDecote,
        long expectedImpotNet
    ) {
        // Setup SimulateurService
        SimulateurService simulateur = new SimulateurService();
        simulateur.setRevenus(rev1, rev2);
        simulateur.setSituation(SituationFamiliale.valueOf(situation));
        simulateur.setEnfants(nbEnf, nbEnfH);
        simulateur.setParentIsole(parentIso);
        
        // Execute calculation
        simulateur.calculate();

        // Verify results
        assertAll(
            () -> assertEquals(expectedAbattement, simulateur.getAbattement(), 0.01, "Abattement incorrect"),
            () -> assertEquals(expectedRevenuFiscal, simulateur.getRevenuFiscal(), 0.01, "Revenu fiscal incorrect"),
            () -> assertEquals(expectedContribEx, simulateur.getContribExceptionnelle(), "Contribution exceptionnelle incorrecte"),
            () -> assertEquals(expectedDecote, simulateur.getDecote(), "Décote incorrecte"),
            () -> assertEquals(expectedImpotNet, simulateur.getImpotNet(), "Impôt net incorrect")
        );
    }


    // Tests pour les services individuels (ajouter dans la même classe)

    @ParameterizedTest
    @CsvFileSource(resources = "/abattement-test-data.csv", numLinesToSkip = 1)
    void testAbattementService(int rev1, int rev2, String situation, double expected) {
        AbattementService service = new AbattementService();
        double actual = service.calculate(rev1, rev2, SituationFamiliale.valueOf(situation));
        assertEquals(expected, actual, 0.01, MessageFormat.format("Abattement: rev1={0}, rev2={1}, sit={2}", rev1, rev2, situation));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/contribution-test-data.csv", numLinesToSkip = 1)
    void testContributionService(double revenuFiscal, boolean isCouple, long expected) {
        ContributionService service = new ContributionService();
        long actual = service.calculate(revenuFiscal, isCouple);
        assertEquals(expected, actual, "Contribution exceptionnelle incorrecte");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/decote-test-data.csv", numLinesToSkip = 1)
    void testDecoteService(long impBrut, int parts, long expected) {
        DecoteService service = new DecoteService();
        long actual = service.apply(impBrut, parts);
        assertEquals(expected, actual, "Décote incorrecte");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/impot-test-data.csv", numLinesToSkip = 1)
    void testImpotService(double revenuImposable, int parts, long expected) {
        ImpotService service = new ImpotService();
        long actual = service.calculateBrut(revenuImposable, parts);
        assertEquals(expected, actual, "Impôt brut incorrect");
    }
}