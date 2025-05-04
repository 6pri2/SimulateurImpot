package com.kerware.simulateur;


import com.kerware.modelrefac.model.SituationFamiliale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests du simulateur d'impôt réusiné")
public class SimulateurImpotTest {

    private ICalculateurImpot calculateur;

    @BeforeEach
    void setUp() {
        calculateur = new AdaptateurSimulateur();
    }

    // Tests des parts fiscales
    @DisplayName("Validation des parts fiscales")
    @ParameterizedTest(name = "Situation={2}, Enfants={3}, Handicap={4}, Isolé={5} → Parts={6}")
    @CsvFileSource(resources = "/partsFiscales.csv", numLinesToSkip = 1)
    void testPartsFiscales(
        int revenuNetDecl1,
        int revenuNetDecl2,
        String situationFamiliale,
        int nbEnfants,
        int nbEnfantsHandicap,
        boolean parentIsole,
        double partsAttendues
    ) {
        calculateur.setRevenusNetDeclarant1(revenuNetDecl1);
        calculateur.setRevenusNetDeclarant2(revenuNetDecl2);
        calculateur.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        calculateur.setNbEnfantsACharge(nbEnfants);
        calculateur.setNbEnfantsSituationHandicap(nbEnfantsHandicap);
        calculateur.setParentIsole(parentIsole);
        calculateur.calculImpotSurRevenuNet();

        assertEquals(partsAttendues, calculateur.getNbPartsFoyerFiscal(), 0.01);
    }

    // Tests des abattements
    @DisplayName("Validation des abattements")
    @ParameterizedTest(name = "Revenu1={0}, Revenu2={1}, Situation={2} → Abattement={3}€")
    @CsvFileSource(resources = "/abattements.csv", numLinesToSkip = 1)
    void testAbattements(
        int revenuNetDecl1,
        int revenuNetDecl2,
        String situationFamiliale,
        int abattementAttendu
    ) {
        calculateur.setRevenusNetDeclarant1(revenuNetDecl1);
        calculateur.setRevenusNetDeclarant2(revenuNetDecl2);
        calculateur.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        calculateur.calculImpotSurRevenuNet();

        assertEquals(abattementAttendu, calculateur.getAbattement());
    }


    // Tests des tranches d'imposition
    @ParameterizedTest
    @CsvFileSource(resources = "/casTranchesImposition.csv", numLinesToSkip = 1)
    void testTranchesImposition(int revenu1, int revenu2, String situation, int impotAttendu) {
        calculateur.setRevenusNetDeclarant1(revenu1);
        calculateur.setRevenusNetDeclarant2(revenu2);
        calculateur.setSituationFamiliale(SituationFamiliale.valueOf(situation));
        
        calculateur.calculImpotSurRevenuNet();
        
        assertEquals(impotAttendu, calculateur.getImpotSurRevenuNet());
    }

    // Tests de robustesse
    @ParameterizedTest(name = "Revenu1={0}, Revenu2={1}, Situation={2}, Enfants={3}, Handicap={4}, Isolé={5}")
    @CsvFileSource(
        resources = "/robustesse.csv",
        numLinesToSkip = 1,
        nullValues = {"NULL"} // "NULL" dans le CSV → null
    )
    void testCasInvalides(
        int revenuNetDecl1,
        int revenuNetDecl2,
        String sitFam,
        int nbEnfants,
        int nbEnfantsHandicap,
        boolean parentIsole
    ) {
        assertThrows(IllegalArgumentException.class, () -> {
            // Configuration des paramètres
            calculateur.setRevenusNetDeclarant1(revenuNetDecl1);
            calculateur.setRevenusNetDeclarant2(revenuNetDecl2);
            
            if (sitFam != null) {
                calculateur.setSituationFamiliale(SituationFamiliale.valueOf(sitFam));
            } else {
                calculateur.setSituationFamiliale(null); // Teste les situations null
            }
            
            calculateur.setNbEnfantsACharge(nbEnfants);
            calculateur.setNbEnfantsSituationHandicap(nbEnfantsHandicap);
            calculateur.setParentIsole(parentIsole);
            
            // Déclenchement du calcul
            calculateur.calculImpotSurRevenuNet();
        });
    }

    // Test complet avec données externes
    @ParameterizedTest
    @CsvFileSource(resources = "/casComplets.csv", numLinesToSkip = 1)
    void testCasComplets(
        int revenu1, 
        int revenu2, 
        String situation, 
        int enfants, 
        int enfantsHandicap, 
        boolean isolé, 
        int impotAttendu
    ) {
        calculateur.setRevenusNetDeclarant1(revenu1);
        calculateur.setRevenusNetDeclarant2(revenu2);
        calculateur.setSituationFamiliale(SituationFamiliale.valueOf(situation));
        calculateur.setNbEnfantsACharge(enfants);
        calculateur.setNbEnfantsSituationHandicap(enfantsHandicap);
        calculateur.setParentIsole(isolé);
        
        calculateur.calculImpotSurRevenuNet();
        
        assertEquals(impotAttendu, calculateur.getImpotSurRevenuNet());
    }

    @DisplayName("Tests des méthodes d'accès de l'adaptateur")
    @ParameterizedTest(name = "[Getters] Revenu1={0}, Revenu2={1}, Situation={2}, Enfants={3}")
    @CsvFileSource(resources = "/casGetters.csv", numLinesToSkip = 1)
    void testAdapterGetters(
        int revenu1, 
        int revenu2, 
        String situation, 
        int enfants, 
        int expectedRev1,
        int expectedRev2,
        int expectedRefFiscale,
        int expectedAvantDecote,
        int expectedDecote,
        double expectedContrib
    ) {
        // Arrange
        calculateur.setRevenusNetDeclarant1(revenu1);
        calculateur.setRevenusNetDeclarant2(revenu2);
        calculateur.setSituationFamiliale(SituationFamiliale.valueOf(situation));
        calculateur.setNbEnfantsACharge(enfants);
        
        // Act
        calculateur.calculImpotSurRevenuNet();
        
        // Assert
        assertEquals(expectedRev1, calculateur.getRevenuNetDeclatant1());
        assertEquals(expectedRev2, calculateur.getRevenuNetDeclatant2());
        assertEquals(expectedRefFiscale, calculateur.getRevenuFiscalReference());
        assertEquals(expectedAvantDecote, calculateur.getImpotAvantDecote());
        assertEquals(expectedDecote, calculateur.getDecote());
        assertEquals(expectedContrib, calculateur.getContribExceptionnelle(), 0.01);
    }

    // Ajouter ce bloc MethodSource pour les cas complexes
    static Stream<Arguments> provideComplexCasesForGetters() {
        return Stream.of(
            Arguments.of(30000, 35000, "MARIE", 2, 30000, 35000, 58500, 2708, 219, 0),
            Arguments.of(50000, 0, "CELIBATAIRE", 0, 50000, 0, 45000, 6786, 0, 0)
        );
    }

    @DisplayName("Tests complexes des méthodes d'accès")
    @ParameterizedTest
    @MethodSource("provideComplexCasesForGetters")
    void testComplexAdapterGetters(
        int revenu1,
        int revenu2,
        String situation,
        int enfants,
        int expectedRev1,
        int expectedRev2,
        int expectedRefFiscale,
        int expectedAvantDecote,
        int expectedDecote,
        double expectedContrib
    ) {
        testAdapterGetters(
            revenu1, revenu2, situation, enfants,
            expectedRev1, expectedRev2, expectedRefFiscale,
            expectedAvantDecote, expectedDecote, expectedContrib
        );
    }
}