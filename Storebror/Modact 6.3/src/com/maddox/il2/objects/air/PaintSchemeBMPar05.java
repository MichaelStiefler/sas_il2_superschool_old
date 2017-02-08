package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;

public class PaintSchemeBMPar05 extends PaintScheme {

    public PaintSchemeBMPar05() {
    }

    public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k) {
        int l = regiment.gruppeNumber() - 1;
        if (regiment.country() == PaintScheme.countryGermany) {
            if (k < 1) {
                k = 1;
            }
            return regiment.id() + " + " + (char) (65 + ((k % 26) - 1)) + PaintScheme.psGermanBomberLetter[l][i];
        }
        if (regiment.country() == PaintScheme.countryNetherlands) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countrySouthAfricaS) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countrySouthAfricaB) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countryAngola) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countryMozambique) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countryGenericBlue) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countryGenericRed) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countryAlbania) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryCuba) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryVietnamNorth) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryChile) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryMyanmar) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryNigeria) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryElSalvador) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryAlgeria) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryCambodia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryEritrea) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryGuatemala) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryHonduras) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryIndonesia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryIreland) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryLibya) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryMalaysia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySingapore) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryVenezuala) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryGermanyBlue) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryEthiopia) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        }
        if (regiment.country() == PaintScheme.countryMorocco) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        }
        if (regiment.country() == PaintScheme.countryNicaragua) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        }
        if (regiment.country() == PaintScheme.countryPanama) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        }
        if (regiment.country() == PaintScheme.countryVietnamSouth) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        }
        if (regiment.country() == PaintScheme.countryJordan) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryLebanon) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySaudiArabia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryBiafra) {
            return "o " + k;
        }
        if (regiment.country() == PaintScheme.countryBorduria) {
            return "o " + k;
        }
        if (regiment.country() == PaintScheme.countryCostaRica) {
            return "o " + k;
        }
        if (regiment.country() == PaintScheme.countryCostaVerde) {
            return "o " + k;
        }
        if (regiment.country() == PaintScheme.countrySylvadia) {
            return "o " + k;
        }
        if (regiment.country() == PaintScheme.countryAustralia) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryNewZeeland) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryBotswana) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryUganda) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryFinland) {
            return PaintScheme.psFinnishFighterString[1][i] + k;
        }
        if (regiment.country() == PaintScheme.countryFrance) {
            return "o " + k;
        }
        if (regiment.country() == PaintScheme.countryBritain) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryBritainBlue) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryBritain) {
            return regiment.id() + " + " + (k < 10 ? "0" + k : "" + k);
        }
        if (regiment.country() == PaintScheme.countryBritainBlue) {
            return regiment.id() + " + " + (k < 10 ? "0" + k : "" + k);
        }
        if (regiment.country() == PaintScheme.countryItaly) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countryJapan) {
            return "" + k;
        }
        if (regiment.country() == PaintScheme.countryPoland) {
            return (k < 10 ? "0" + k : "" + k);
        }
        if (regiment.country() == PaintScheme.countryRomania) {
            return "+ " + (k < 10 ? "0" + k : "" + k);
        }
        if (regiment.country() == PaintScheme.countryRussia) {
            return (k >= 10 ? "" + k : "0" + k) + " *";
        }
        if (regiment.country() == PaintScheme.countryNewZealand) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countrySlovakia) {
            return k + " +";
        }
        if (regiment.country() == PaintScheme.countryUSA) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        }
        if (regiment.country() == PaintScheme.countryUSABlue) {
            return (k < 10 ? "0" + k : "" + k) + "*";
        }
        if (regiment.country() == PaintScheme.countrySwitzerland) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryChina) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySpainRep) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryManagua) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySpainNat) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryIsrael) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryIsraelBlue) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryCanada) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryCanadaBlue) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryItalyAllied) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryRomaniaAllied) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryFinlandAllied) {
            return PaintScheme.psFinnishFighterString[1][i] + k;
        }
        if (regiment.country() == PaintScheme.countryBelgium) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryBulgaria) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryNorway) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryGreece) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryBrazil) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySweden) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySouthAfrica) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryYugoslavia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryYugoPar) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryDenmark) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryPhilippines) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryCroatia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryBulgariaAllied) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryThailandBlue) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryThailand) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryRussianLiberationArmy) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryManchukuo) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryRussianEmpire) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryGDR) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryBolivia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryAbyssinia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryMexico) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryUruguay) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryHungaryAllied) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryTurkey) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryIraq) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryEgypt) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryEgyptRed) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryParaguay) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryArgentina) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryCzechoslovakia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryPolandAxis) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryPortugal) {
            k = this.clampToLiteral(k);
            return regiment.id() + " - " + (char) (65 + (k - 1));
        }
        if (regiment.country() == PaintScheme.countryIran) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryPRC) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryChinaRed) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryNorthKorea) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySouthKorea) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryNorthKoreaRed) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySouthKoreaBlue) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryEstonia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryLatvia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryLithuania) {
            return PaintScheme.psFinnishFighterString[1][i] + k;
        }
        if (regiment.country() == PaintScheme.countryPeru) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryColombia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySyria) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countrySyriaRed) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryAustria) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryItalyANR) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryVichyFrance) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryIndia) {
            return (k >= 10 ? "" + k : "0" + k);
        }
        if (regiment.country() == PaintScheme.countryPakistan) {
            return (k >= 10 ? "" + k : "0" + k);
        } else {
            return super.typedNameNum(class1, regiment, i, j, k);
        }
    }

    public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
        super.prepareNum(class1, hiermesh, regiment, i, j, k);
        int l = regiment.gruppeNumber() - 1;
        if (regiment.country() == PaintScheme.countryGermany) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "psBM05GERREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay3", "psBM05GERREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM05GERCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "German/" + (char) (65 + (k - 1)) + ".tga", "German/" + PaintScheme.psGermanBomberLetter[l][i] + ".tga", PaintScheme.psGermanBomberColor[i][0], PaintScheme.psGermanBomberColor[i][1], PaintScheme.psGermanBomberColor[i][2], 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psBM05GERCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "German/" + (char) (65 + (k - 1)) + ".tga", "German/" + PaintScheme.psGermanBomberLetter[l][i] + ".tga", PaintScheme.psGermanBomberColor[i][0], PaintScheme.psGermanBomberColor[i][1], PaintScheme.psGermanBomberColor[i][2], 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "balken4", "German/balken4.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "balken2", "German/balken2.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "haken3", "German/" + (World.cur().isHakenAllowed() ? "haken3.tga" : "hakenfake.tga"), 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNetherlands) {
            this.changeMat(class1, hiermesh, "Overlay6", "DutchTriangle", "Dutch/roundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "DutchTriangle", "Dutch/roundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay1", "psBM0SDUTCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psBM0SDUTCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySouthAfricaS) {
            this.changeMat(class1, hiermesh, "Overlay6", "SSWings", "SA/SSWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SSRoundel", "SA/SSRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "SATail", "SA/SATail.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "psBM05SASSNUM" + (k >= 10 ? "" + k : "0" + k), "SAPRE/" + (k / 10) + ".tga", "SA/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM05SASSNUM" + (k >= 10 ? "" + k : "0" + k), "SAPRE/" + (k / 10) + ".tga", "SA/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySouthAfricaB) {
            this.changeMat(class1, hiermesh, "Overlay6", "SBWing", "SA/SBWing.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SBRoundel", "SA/SBRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "psBM05SASBNUM" + (k >= 10 ? "" + k : "0" + k), "SAPRE/" + (k / 10) + ".tga", "SA/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM05SASBNUM" + (k >= 10 ? "" + k : "0" + k), "SAPRE/" + (k / 10) + ".tga", "SA/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            return;
        }
        if (regiment.country() == PaintScheme.countryAngola) {
            this.changeMat(hiermesh, "Overlay3", "psBM05ANGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(hiermesh, "Overlay2", "psBM05ANGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "AngolaWing", "AN/AngolaWing.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "AngolaRoundel", "AN/AngolaRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "AngolaTail", "AN/AngolaTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryMozambique) {
            this.changeMat(hiermesh, "Overlay3", "psBM05MZBCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(hiermesh, "Overlay2", "psBM05MZBCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "MozWing", "MZ/MozWing.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "MozRoundel", "MZ/MozRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "MozTail", "MZ/MozTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryAlbania) {
            this.changeMat(hiermesh, "Overlay4", "psBM00ALGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(hiermesh, "Overlay1", "psBM00ALGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "AlbaniaWing", "Albania/ALWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "AlbaniaRoundel", "Albania/ALRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "AlbaniaTail", "Albania/ALTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryCuba) {
            this.changeMat(hiermesh, "Overlay4", "psBM00CUGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(hiermesh, "Overlay1", "psBM00CUGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "CubaWing", "Cuba/CUWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CubaRoundel", "Cuba/CURoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "CubaTail", "Cuba/CUTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryVietnamNorth) {
            this.changeMat(hiermesh, "Overlay4", "psBM00NVGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(hiermesh, "Overlay1", "psBM00NVGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "VietnamNorthWing", "VietnamNorth/NVWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "VietnamNorthRoundel", "VietnamNorth/NVRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "VietnamNorthTail", "VietnamNorth/NVTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryChile) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "ChileRoundel", "Chile/CIWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "ChileRoundel", "Chile/CIRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "ChileTail", "Chile/CITail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryElSalvador) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "ElSalvadorRoundel", "ElSalvador/ELWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "ElSalvadorRoundel", "ElSalvador/ELRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "ElSalvadorTail", "ElSalvador/ELTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryMyanmar) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "MyanmarRoundel", "Myanmar/MMWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "MyanmarRoundel", "Myanmar/MMRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "MyanmarTail", "Myanmar/MMTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNigeria) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "NigeriaRoundel", "Nigeria/NGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "NigeriaRoundel", "Nigeria/NGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "NigeriaTail", "Nigeria/NGTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryAlgeria) {
            this.changeMat(hiermesh, "Overlay3", "psBM00AGGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00AGGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "AlgeriaWing", "Algeria/AGWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "AlgeriaRoundel", "Algeria/AGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "AlgeriaTail", "Algeria/AGTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryCambodia) {
            this.changeMat(hiermesh, "Overlay3", "psBM00CMGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00CMGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "CambodiaWing", "Cambodia/CMWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CambodiaRoundel", "Cambodia/CMRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "CambodiaTail", "Cambodia/CMTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryEritrea) {
            this.changeMat(hiermesh, "Overlay3", "psBM00ETGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00ETGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "EritreaWing", "Eritrea/ETWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "EritreaRoundel", "Eritrea/ETRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "EritreaTail", "Eritrea/ETTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryGuatemala) {
            this.changeMat(hiermesh, "Overlay3", "psBM00GLGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00GLGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "GuatemalaWing", "Guatemala/GLWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "GuatemalaRoundel", "Guatemala/GLRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "GuatemalaTail", "Guatemala/GLTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryHonduras) {
            this.changeMat(hiermesh, "Overlay3", "psBM00HOGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00HOGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "HondurasWing", "Honduras/HOWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "HondurasRoundel", "Honduras/HORoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "HondurasTail", "Honduras/HOTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryIndonesia) {
            this.changeMat(hiermesh, "Overlay3", "psBM00DOGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00DOGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "IndonesiaWing", "Indonesia/DOWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "IndonesiaRoundel", "Indonesia/DORoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "IndonesiaTail", "Indonesia/DOTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryIreland) {
            this.changeMat(hiermesh, "Overlay3", "psBM00ADGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00ADGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "IrelandWing", "Ireland/ADWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "IrelandRoundel", "Ireland/ADRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "IrelandTail", "Ireland/ADTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryLibya) {
            this.changeMat(hiermesh, "Overlay3", "psBM00LYGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00LYGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "LibyaWing", "Libya/LYWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "LibyaRoundel", "Libya/LYRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "LibyaTail", "Libya/LYTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryMalaysia) {
            this.changeMat(hiermesh, "Overlay3", "psBM00MYGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00MYGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "MalaysiaWing", "Malaysia/MYWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "MalaysiaRoundel", "Malaysia/MYRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "MalaysiaTail", "Malaysia/MYTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySingapore) {
            this.changeMat(hiermesh, "Overlay3", "psBM00SIGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00SIGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "SingaporeWing", "Singapore/SIWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SingaporeRoundel", "Singapore/SIRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "SingaporeTail", "Singapore/SITail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryVenezuala) {
            this.changeMat(hiermesh, "Overlay3", "psBM00VZGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00VZGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "VenezualaWing", "Venezuela/VZWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "VenezualaRoundel", "Venezuela/VZRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "VenezualaTail", "Venezuela/VZTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryGermanyBlue) {
            this.changeMat(hiermesh, "Overlay1", "psFM03GPNREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay3", "psFM03GPNREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psFM03GPCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psFM03GPCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "GermanyPostWings", "GermanyPost/GPWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "GermanyPostRoundel", "GermanyPost/GPRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "GermanyPostTail", "GermanyPost/GPTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryEthiopia) {
            this.changeMat(hiermesh, "Overlay1", "psBM0JEHACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psBM0JEHACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "EthiopiaWing", "Ethiopia/EHWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "EthiopiaRoundel", "Ethiopia/EHRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "EthiopiaTail", "Ethiopia/EHTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryMorocco) {
            this.changeMat(hiermesh, "Overlay1", "psBM0JMRACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psBM0JMRACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "MoroccoWing", "Morocco/MRWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "MoroccoRoundel", "Morocco/MRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "MoroccoTail", "Morocco/MRTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNicaragua) {
            this.changeMat(hiermesh, "Overlay1", "psBM0JNIACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "psBM0JNIACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "NicaraguaWing", "Nicaragua/NIWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "NicaraguaRoundel", "Nicaragua/NIRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "NicaraguaTail", "Nicaragua/NITail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryPanama) {
            this.changeMat(hiermesh, "Overlay1", "psBM0JPMACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "psBM0JPMACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "PanamaWing", "Panama/PMWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "PanamaRoundel", "Panama/PMRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "PanamaTail", "Panama/PMTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryVietnamSouth) {
            this.changeMat(hiermesh, "Overlay1", "psBM0JVNACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "psBM0JVNACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "VietnamSouthWing", "VietnamSouth/VNWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "VietnamSouthRoundel", "VietnamSouth/VNRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "VietnamSouthTail", "VietnamSouth/VNTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryJordan) {
            this.changeMat(hiermesh, "Overlay2", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "JordanRoundel", "Jordan/JORoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "JordanRoundel", "Jordan/JORoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "JordanTail", "Jordan/JOTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryLebanon) {
            this.changeMat(hiermesh, "Overlay2", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "LebanonRoundel", "Lebanon/LERoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "LebanonRoundel", "Lebanon/LERoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "LebanonTail", "Lebanon/LETail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySaudiArabia) {
            this.changeMat(hiermesh, "Overlay2", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "SaudiRoundel", "Saudi/SURoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SaudiRoundel", "Saudi/SURoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "SaudiTail", "Saudi/SUTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryBiafra) {
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay2", "psFM03BIALNUM" + l + i + k, "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03BIARNUM" + l + i + k, "null.tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.changeMat(hiermesh, "Overlay2", "psFM03BIACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03BIACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.changeMat(class1, hiermesh, "Overlay6", "BiafraWings", "Biafra/BIWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "BiafraRoundel", "Biafra/BIRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "BiafraTail", "Biafra/BITail.tga", 1.0F, 1.0F, 1.0F);
        }
        if (regiment.country() == PaintScheme.countryBorduria) {
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay2", "psFM03BDALNUM" + l + i + k, "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03BDARNUM" + l + i + k, "null.tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.changeMat(hiermesh, "Overlay2", "psFM03BDACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03BDACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.changeMat(class1, hiermesh, "Overlay6", "BorduriaWings", "Borduria/BDWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "BorduriaRoundel", "Borduria/BDRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "BorduriaTail", "Borduria/BDTail.tga", 1.0F, 1.0F, 1.0F);
        }
        if (regiment.country() == PaintScheme.countryCostaRica) {
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay2", "psFM03CRALNUM" + l + i + k, "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03CRARNUM" + l + i + k, "null.tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.changeMat(hiermesh, "Overlay2", "psFM03CRACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03CRACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.changeMat(class1, hiermesh, "Overlay6", "CostaRicaWings", "CostaRica/CRWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CostaRicaRoundel", "CostaRica/CRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "CostaRicaTail", "CostaRica/CRTail.tga", 1.0F, 1.0F, 1.0F);
        }
        if (regiment.country() == PaintScheme.countryCostaVerde) {
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay2", "psFM03CVALNUM" + l + i + k, "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03CVARNUM" + l + i + k, "null.tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.changeMat(hiermesh, "Overlay2", "psFM03CVACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03CVACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.changeMat(class1, hiermesh, "Overlay6", "CostaVerdeWings", "CostaVerde/CVWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CostaVerdeRoundel", "CostaVerde/CVRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "CostaVerdeTail", "CostaVerde/CVTail.tga", 1.0F, 1.0F, 1.0F);
        }
        if (regiment.country() == PaintScheme.countrySylvadia) {
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay2", "psFM03SLALNUM" + l + i + k, "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03SLARNUM" + l + i + k, "null.tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.changeMat(hiermesh, "Overlay2", "psFM03SLACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFM03SLACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.changeMat(class1, hiermesh, "Overlay6", "SylvadiaWings", "Sylvadia/SLWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SylvadiaRoundel", "Sylvadia/SLRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "SylvadiaTail", "Sylvadia/SLTail.tga", 1.0F, 1.0F, 1.0F);
        }
        if (regiment.country() == PaintScheme.countryAustralia) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "SAASRegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay3", "SAASRegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "ABritishLnum_" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "ABritishRnum_" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "AustraliaWings", "Australia/ASWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "AustraliaRoundel", "Australia/ASRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "AustraliaTail", "Australia/ASTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNewZeeland) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "NZSARegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay3", "NZSARegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "NZBritishLnum_" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "NZBritishRnum_" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "NewZeelandWings", "NewZeeland/NZWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "NewZeelandRoundel", "NewZeeland/NZRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "NewZeelandTail", "NewZeeland/NZTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryBotswana) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "BTSARegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay3", "BTSARegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "BTBritishLnum_" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "BTBritishRnum_" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "BotswanaWings", "Botswana/BTWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "BotswanaRoundel", "Botswana/BTRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "BotswanaTail", "Botswana/BTTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryUganda) {
            this.changeMat(hiermesh, "Overlay4", "psBM00UGGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishOrangeColor[0], PaintScheme.psBritishOrangeColor[1], PaintScheme.psBritishOrangeColor[2], PaintScheme.psBritishOrangeColor[0], PaintScheme.psBritishOrangeColor[1], PaintScheme.psBritishOrangeColor[2]);
            this.changeMat(hiermesh, "Overlay1", "psBM00UGGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishOrangeColor[0], PaintScheme.psBritishOrangeColor[1], PaintScheme.psBritishOrangeColor[2], PaintScheme.psBritishOrangeColor[0], PaintScheme.psBritishOrangeColor[1], PaintScheme.psBritishOrangeColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "UgandaWing", "Uganda/UGWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "UgandaRoundel", "Uganda/UGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "UgandaTail", "Uganda/UGTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryFinland) {
            char c = (char) (48 + (k % 10));
            this.changeMat(class1, hiermesh, "Overlay6", "FAFhaken", "Finnish/" + (World.cur().isHakenAllowed() ? "FAFhaken.tga" : "FAFroundel.tga"), 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "FAFhaken", "Finnish/" + (World.cur().isHakenAllowed() ? "FAFhaken.tga" : "FAFroundel.tga"), 1.0F, 1.0F, 1.0F);
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay8", "psBM05FINANUM" + l + i + "0" + k, "Finnish/0" + k + ".tga", PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2]);
            } else {
                this.changeMat(hiermesh, "Overlay8", "psBM05FINCNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2], PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2]);
            }
            String s1 = this.getFAFACCode(class1, i);
            this.changeMat(hiermesh, "Overlay2", "psBM05FINACOD" + s1 + c, "Finnish/" + s1 + ".tga", "Finnish/sn" + c + ".tga", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            this.changeMat(hiermesh, "Overlay3", "psBM05FINACOD" + s1 + c, "Finnish/" + s1 + ".tga", "Finnish/sn" + c + ".tga", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryFrance) {
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay2", "psFB05FRALNUM" + l + i + k, "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFB05FRARNUM" + l + i + k, "null.tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.changeMat(hiermesh, "Overlay2", "psFB05FRACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFB05FRACNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.changeMat(class1, hiermesh, "Overlay6", "frenchroundel", "French/roundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "frenchroundel", "French/roundel.tga", 1.0F, 1.0F, 1.0F);
        }
        if (regiment.country() == PaintScheme.countryBritain) {
            if ("ra".equals(regiment.branch()) || "rz".equals(regiment.branch()) || "rn".equals(regiment.branch())) {
                k = this.clampToLiteral(k);
                this.changeMat(hiermesh, "Overlay1", "psBM05BRINAVYREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
                this.changeMat(hiermesh, "Overlay4", "psBM05BRINAVYREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
                this.changeMat(hiermesh, "Overlay2", "psBM05BRINAVYLNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psBM05BRINAVYRNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
                this.changeMat(class1, hiermesh, "Overlay6", "britishroundel5n", "British/roundel5N.tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(class1, hiermesh, "Overlay7", "britishroundel5n", "British/roundel5N.tga", 1.0F, 1.0F, 1.0F);
            } else {
                k = this.clampToLiteral(k);
                this.changeMat(hiermesh, "Overlay1", "psBM05BRIREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
                this.changeMat(hiermesh, "Overlay4", "psBM05BRIREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
                this.changeMat(hiermesh, "Overlay2", "psBM05BRILNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psBM05BRIRNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
                this.changeMat(class1, hiermesh, "Overlay6", "britishroundel2c", "British/roundel2c.tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(class1, hiermesh, "Overlay7", "britishroundel3c", "British/roundel3c.tga", 1.0F, 1.0F, 1.0F);
            }
            return;
        }
        if (regiment.country() == PaintScheme.countryBritainBlue) {
            k = this.clampToLiteral(k);
            this.changeMat(class1, hiermesh, "Overlay8", "BBRitTail", "British/CATail.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay1", "psBBBBRIREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay3", "psBBBBRIREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBBBBRILNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psBBBBRIRNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "britishroundel2c", "British/roundel2c.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "britishroundel3c", "British/roundel3c.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryHungary) {
            this.changeMat(hiermesh, "Overlay1", "psBM05HUNREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay3", "psBM05HUNREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM05HUNCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "psBM05HUNCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "hungarianbalkennewer", "Hungarian/balkennewer.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "hungarianbalkennewer", "Hungarian/balkennewer.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryJapan) {
            this.changeMat(hiermesh, "Overlay2", "psBM05JAPCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", PaintScheme.psRussianBomberColor[0][0], PaintScheme.psRussianBomberColor[0][1], PaintScheme.psRussianBomberColor[0][2], PaintScheme.psRussianBomberColor[0][0], PaintScheme.psRussianBomberColor[0][1], PaintScheme.psRussianBomberColor[0][2]);
            this.changeMat(hiermesh, "Overlay3", "psBM05JAPCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", PaintScheme.psRussianBomberColor[0][0], PaintScheme.psRussianBomberColor[0][1], PaintScheme.psRussianBomberColor[0][2], PaintScheme.psRussianBomberColor[0][0], PaintScheme.psRussianBomberColor[0][1], PaintScheme.psRussianBomberColor[0][2]);
            this.changeMat(class1, hiermesh, "Overlay6", "JAR1", "Japanese/JAR.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "JAR1", "Japanese/JAR.tga", 1.0F, 1.0F, 1.0F);
        }
        if (regiment.country() == PaintScheme.countryItaly) {
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay2", "psFB05ITALNUM" + l + i + k, "Italian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFB05ITARNUM" + l + i + k, "null.tga", "Italian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.changeMat(hiermesh, "Overlay2", "psFB05ITACNUM" + l + i + k, "Italian/1" + (k / 10) + ".tga", "Italian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay3", "psFB05ITACNUM" + l + i + k, "Italian/1" + (k / 10) + ".tga", "Italian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.changeMat(class1, hiermesh, "Overlay6", "italian3", "Italian/roundel0.tga", 0.1F, 0.1F, 0.1F);
        }
        if (regiment.country() == PaintScheme.countryPoland) {
            this.changeMat(hiermesh, "Overlay1", "psBM05POLCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "psBM05POLCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "polishcheckerboard", "Polish/checkerboard.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "polishcheckerboard", "Polish/checkerboard.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryRomania) {
            this.changeMat(hiermesh, "Overlay8", "psFB05ROMCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "romaniancross", "Romanian/insignia.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "romaniancross", "Romanian/insignia.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryRussia) {
            this.changeMat(hiermesh, "Overlay1", "psBM05RUSCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "psBM05RUSCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "redstar3", "Russian/redstar3.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "redstar3", "Russian/redstar3.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNewZealand) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "psBM0SRZREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
            this.changeMat(hiermesh, "Overlay4", "psBM0SRZREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
            this.changeMat(hiermesh, "Overlay2", "psBM0SRZLNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "psBM0SRZRNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "newzealand6", "NewZealand/newzealand6.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "newzealand7", "NewZealand/newzealand7.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySlovakia) {
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay4", "psBM05SLVKLNUM" + l + i + "0" + k, "Finnish/" + k + ".tga", 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay1", "psBM05SLVKRNUM" + l + i + "0" + k, "null.tga", "Finnish/" + k + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.changeMat(hiermesh, "Overlay1", "psBM05SLVKCNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.changeMat(hiermesh, "Overlay4", "psBM05SLVKCNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.changeMat(class1, hiermesh, "Overlay6", "slovakiancross2", "Slovakian/cross2.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "slovakiancross2", "Slovakian/cross2.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryUSA) {
            this.changeMat(hiermesh, "Overlay1", "psBM0SUSACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "psBM0SUSACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryUSABlue) {
            this.changeMat(hiermesh, "Overlay1", "psBM0SUSACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "psBM0SUSACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + (k / 10) + ".tga", "States/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySwitzerland) {
            this.changeMat(class1, hiermesh, "Overlay6", "CHRoundel", "CH/CHRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CHRoundel", "CH/CHRoundel.tga", 1.0F, 1.0F, 1.0F);
            String s = this.getCHBMCode(class1);
            this.changeMat(hiermesh, "Overlay2", "CHCode_" + s + (k < 10 ? "0" + k : "" + k), "Swiss/" + s + ".tga", "Swiss/" + (k % 10) + ".tga", 0.95F, 0.95F, 0.95F, 0.95F, 0.95F, 0.95F);
            this.changeMat(hiermesh, "Overlay3", "CHCode_" + s + (k < 10 ? "0" + k : "" + k), "Swiss/" + s + ".tga", "Swiss/" + (k % 10) + ".tga", 0.95F, 0.95F, 0.95F, 0.95F, 0.95F, 0.95F);
            return;
        }
        if (regiment.country() == PaintScheme.countryChina) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "CNRoundel", "CN/CNRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CNRoundel", "CN/CNRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySpainRep) {
            this.changeMat(class1, hiermesh, "Overlay6", "ESRoundel", "ES/ESRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "ESRoundel", "ES/ESRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryManagua) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "MGRoundel", "MG/MGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "MGRoundel", "MG/MGRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySpainNat) {
            int i1 = Integer.parseInt(regiment.id());
            this.changeMat(hiermesh, "Overlay1", "BlackNum_00" + (i1 < 10 ? "0" + i1 : "" + i1), "Default/1" + (i1 / 10) + ".tga", "Default/1" + (i1 % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_00" + (i1 < 10 ? "0" + i1 : "" + i1), "Default/1" + (i1 / 10) + ".tga", "Default/1" + (i1 % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "SPWings", "SP/SPWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SPRoundel", "SP/SPRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryIsrael) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "ISRoundel", "IS/ISRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "ISRoundel", "IS/ISRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryIsraelBlue) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "ISRoundel", "IS/ISRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "ISRoundel", "IS/ISRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryCanada) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "CARegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(hiermesh, "Overlay3", "CARegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(hiermesh, "Overlay2", "BritishLnum_" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BritishRnum_" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "CARoundel", "CA/CARoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CARoundel", "CA/CARoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "CATail", "CA/CATail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryCanadaBlue) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "CABRegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay3", "CABRegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "CBritishLnum_" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay4", "CBritishRnum_" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "CARoundel", "CA/CARoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CARoundel", "CA/CARoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "CATail", "CA/CATail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryItalyAllied) {
            this.changeMat(hiermesh, "Overlay1", "ITLDefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "IA/1" + (k / 10) + ".tga", "IA/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "ITLDefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "IA/1" + (k / 10) + ".tga", "IA/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "ILRoundel", "IL/ILRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "ILRoundel", "IL/ILRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryRomaniaAllied) {
            this.changeMat(class1, hiermesh, "Overlay6", "RLRoundelLate", "RL/RLRoundelLate.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "RLRoundelLate", "RL/RLRoundelLate.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryFinlandAllied) {
            char c1 = (char) (48 + (k % 10));
            this.changeMat(class1, hiermesh, "Overlay6", "FLRoundel", "FL/FLRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "FLRoundel", "FL/FLRoundel.tga", 1.0F, 1.0F, 1.0F);
            if (k < 10) {
                this.changeMat(class1, hiermesh, "Overlay8", "psBM05FINANUM" + l + i + "0" + k, "Finnish/0" + k + ".tga", PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2]);
            } else {
                this.changeMat(hiermesh, "Overlay8", "psBM05FINCNUM" + l + i + k, "Finnish/" + (k / 10) + ".tga", "Finnish/" + (k % 10) + ".tga", PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2], PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2]);
            }
            String s2 = this.getFAFACCode(class1, i);
            this.changeMat(hiermesh, "Overlay2", "psBM05FINACOD" + s2 + c1, "Finnish/" + s2 + ".tga", "Finnish/sn" + c1 + ".tga", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            this.changeMat(hiermesh, "Overlay3", "psBM05FINACOD" + s2 + c1, "Finnish/" + s2 + ".tga", "Finnish/sn" + c1 + ".tga", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryBelgium) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "BERoundel", "BE/BERoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "BERoundel", "BE/BERoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "BETail", "BE/BETail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryBulgaria) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "BGRoundel", "BG/BGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "BGRoundel", "BG/BGRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNorway) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "NORegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(hiermesh, "Overlay3", "NORegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(hiermesh, "Overlay2", "BritishLnum_" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BritishRnum_" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "NOWings", "NO/NOWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "NORoundel", "NO/NORoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryGreece) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "GRRoundel", "GR/GRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "GRRoundel", "GR/GRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "GRTail", "GR/GRTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryBrazil) {
            this.changeMat(class1, hiermesh, "Overlay6", "BRRoundel", "BR/BRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "BRRoundel", "BR/BRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySweden) {
            int j1 = Integer.parseInt(regiment.id());
            this.changeMat(hiermesh, "Overlay2", "BlackNum_00" + (j1 < 10 ? "0" + j1 : "" + j1), "Default/1" + (j1 / 10) + ".tga", "Default/1" + (j1 % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_00" + (j1 < 10 ? "0" + j1 : "" + j1), "Default/1" + (j1 / 10) + ".tga", "Default/1" + (j1 % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "SWRoundel", "SW/SWRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SWRoundel", "SW/SWRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySouthAfrica) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "SARegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(hiermesh, "Overlay3", "SARegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(hiermesh, "Overlay2", "BritishLnum_" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BritishRnum_" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "SAWings", "SA/SAWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SARoundel", "SA/SARoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "SATail", "SA/SATail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryYugoslavia) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "YURoundel", "YU/YURoundelEarly.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "YURoundel", "YU/YURoundelEarly.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "YUTail", "YU/YUTailEarly.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryYugoPar) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "YUPRoundel", "YU/YURoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "YUPRoundel", "YU/YURoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "YUPTail", "YU/YUTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryDenmark) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "DKRoundel", "DK/DKRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "DKRoundel", "DK/DKRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "DKTail", "DK/DKTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryPhilippines) {
            this.changeMat(hiermesh, "Overlay1", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "PHRoundel", "PH/PHRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "PHRoundel", "PH/PHRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryCroatia) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "HRRoundel", "HR/HRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "HRRoundel", "HR/HRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "HRTail", "HR/HRTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryBulgariaAllied) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "BLRoundelLate", "BL/BLRoundelLate.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "BLTail", "BL/BLRoundelLate.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryThailand) {
            int k1 = Integer.parseInt(regiment.id());
            this.changeMat(hiermesh, "Overlay1", "BlackNum_00" + (k1 < 10 ? "0" + k1 : "" + k1), "Default/1" + (k1 / 10) + ".tga", "Default/1" + (k1 % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_00" + (k1 < 10 ? "0" + k1 : "" + k1), "Default/1" + (k1 / 10) + ".tga", "Default/1" + (k1 % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "THRoundelEarly", "TH/THRoundelEarly.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "THRoundelEarly", "TH/THRoundelEarly.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "THTail", "TH/THWings.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryThailandBlue) {
            int l1 = Integer.parseInt(regiment.id());
            this.changeMat(hiermesh, "Overlay1", "BlackNum_00" + (l1 < 10 ? "0" + l1 : "" + l1), "Default/1" + (l1 / 10) + ".tga", "Default/1" + (l1 % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_00" + (l1 < 10 ? "0" + l1 : "" + l1), "Default/1" + (l1 / 10) + ".tga", "Default/1" + (l1 % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "THRoundel", "TH/THRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "THRoundel", "TH/THRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "THTail", "TH/THWings.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryRussianLiberationArmy) {
            this.changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "RBRoundel", "RB/RBRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "RBRoundel", "RB/RBRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "RBTail", "RB/RBRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryManchukuo) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "MKWings", "MK/MKWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "MKTail", "MK/MKTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryRussianEmpire) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "RERoundel", "RE/RERoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "RERoundel", "RE/RERoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryGDR) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "DDRoundel", "DD/DDRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "DDRoundel", "DD/DDRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "DDRoundel", "DD/DDRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryBolivia) {
            this.changeMat(hiermesh, "Overlay1", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "BORoundel", "BO/BORoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "BORoundel", "BO/BORoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryAbyssinia) {
            this.changeMat(hiermesh, "Overlay1", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay2", "ABRoundel", "AB/ABRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay3", "ABRoundel", "AB/ABRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "ABWings", "AB/ABWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "ABTail", "AB/ABTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryMexico) {
            this.changeMat(hiermesh, "Overlay1", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "MXRoundel", "MX/MXRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "MXRoundel", "MX/MXRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryUruguay) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "URRoundel", "UR/URRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "URRoundel", "UR/URRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryHungaryAllied) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "HYRoundelLate", "HY/HYRoundelLate.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "HYRoundelLate", "HY/HYRoundelLate.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "HYTailLate", "HY/HYRoundelLate.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryTurkey) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "TKRoundel", "TK/TKRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "TKRoundel", "TK/TKRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "TKTail", "TK/TKTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryIraq) {
            this.changeMat(hiermesh, "Overlay2", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "IKRoundel", "IK/IKRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "IKRoundel", "IK/IKRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "IKTail", "IK/IKTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryEgypt) {
            this.changeMat(hiermesh, "Overlay2", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "EGRoundel", "EG/EGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "EGRoundel", "EG/EGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "EGTail", "EG/EGTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryEgyptRed) {
            this.changeMat(hiermesh, "Overlay2", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "EGRoundel", "EG/EGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "EGRoundel", "EG/EGRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "EGTail", "EG/EGTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryGenericBlue) {
            this.changeMat(hiermesh, "Overlay3", "psBM00BNGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(hiermesh, "Overlay2", "psBM00BNGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            this.changeMat(class1, hiermesh, "Overlay6", "GenBlueWing", "BU/BlueWing.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "GenBlueRoundel", "BU/BlueRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "GenBlueTail", "BU/BlueTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryGenericRed) {
            this.changeMat(hiermesh, "Overlay3", "psBM00RNGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(hiermesh, "Overlay2", "psBM00RNGCNUM" + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2], PaintScheme.psBritishRedColor[0], PaintScheme.psBritishRedColor[1], PaintScheme.psBritishRedColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "GenredWing", "RD/RedWing.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "GenredRoundel", "RD/RedRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "AGenredTail", "RD/RedTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryParaguay) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "PARoundel", "PA/PARoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "PATail", "PA/PATail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryArgentina) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "ARRoundel", "AR/ARRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "ARRoundel", "AR/ARRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "ARTail", "AR/ARTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryCzechoslovakia) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay7", "SAASczLRegi_" + regiment.id(), "CZ/" + regiment.aid()[0] + ".tga", "CZ/" + regiment.aid()[1] + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay1", "DefaultNumCZ_" + l + i + (k < 10 ? "0" + k : "" + k), "Slovakian/" + (k / 10) + ".tga", "Slovakian/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "DefaultNumCZ_" + l + i + (k < 10 ? "0" + k : "" + k), "Slovakian/" + (k / 10) + ".tga", "Slovakian/" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "CZRoundel", "CZ/CZRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "CZTail", "CZ/CZTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryPolandAxis) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "PNRoundel", "PN/PNRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "PNRoundel", "PN/PNRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryPortugal) {
            k = this.clampToLiteral(k);
            this.changeMat(hiermesh, "Overlay1", "PTRegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(hiermesh, "Overlay3", "PTRegi_" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(hiermesh, "Overlay2", "BritishLnum_" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char) ((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BritishRnum_" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char) ((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
            this.changeMat(class1, hiermesh, "Overlay6", "PTWings", "PT/PTWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "PTRoundel", "PT/PTRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "PTTail", "PT/PTTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryIran) {
            this.changeMat(hiermesh, "Overlay2", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "ArabicNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "IRRoundel", "IR/IRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "IRRoundel", "IR/IRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "IRTail", "IR/IRTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryPRC) {
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "CCRoundel", "CC/CCRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CCRoundel", "CC/CCRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryChinaRed) {
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "CCRoundel", "CC/CCRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CCRoundel", "CC/CCRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNorthKorea) {
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "KPRoundel", "KP/KPRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "KPRoundel", "KP/KPRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySouthKorea) {
            this.changeMat(hiermesh, "Overlay1", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "KRRoundel", "KR/KRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "KRRoundel", "KR/KRRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryNorthKoreaRed) {
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "KPRoundel", "KP/KPRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "KPRoundel", "KP/KPRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySouthKoreaBlue) {
            this.changeMat(hiermesh, "Overlay1", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "KRRoundel", "KR/KRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "KRRoundel", "KR/KRRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryEstonia) {
            this.changeMat(class1, hiermesh, "Overlay1", "EERoundelL", "EE/EERoundelL.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay4", "EERoundelR", "EE/EERoundelR.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "EEWings", "EE/EEWings.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryLatvia) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "LVWings", "LV/LVWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "LVRoundel", "LV/LVRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryLithuania) {
            char c2 = (char) (48 + (k % 10));
            String s3 = this.getFAFACCode(class1, i);
            this.changeMat(hiermesh, "Overlay2", "psBM05FINACID" + s3 + c2, "Finnish/" + s3 + ".tga", "Finnish/sn" + c2 + ".tga", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            this.changeMat(hiermesh, "Overlay3", "psBM05FINACID" + s3 + c2, "Finnish/" + s3 + ".tga", "Finnish/sn" + c2 + ".tga", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "LTRoundel", "LT/LTRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "LTTail", "LT/LTRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryPeru) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "PERoundel", "PE/PERoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "PERoundel", "PE/PERoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryColombia) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "CORoundel", "CO/CORoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "CORoundel", "CO/CORoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySyria) {
            this.changeMat(class1, hiermesh, "Overlay6", "SYRoundel", "SY/SYRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SYRoundel", "SY/SYRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay8", "SYTailNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySyriaRed) {
            this.changeMat(class1, hiermesh, "Overlay6", "SYRoundel", "SY/SYRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "SYRoundel", "SY/SYRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay8", "SYTailNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Arabic/1" + (k / 10) + ".tga", "Arabic/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryAustria) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "ATRoundel", "AT/ATRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "ATRoundel", "AT/ATRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryItalyANR) {
            this.changeMat(hiermesh, "Overlay1", "ANRDefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "IA/1" + (k / 10) + ".tga", "IA/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay4", "ANRDefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "IA/1" + (k / 10) + ".tga", "IA/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay2", "IATail1", "IA/IATail.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay3", "IATail1", "IA/IATail.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "IAWings", "IA/IAWings.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "IARoundel", "IA/IARoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "IATail2", "IA/IATail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryVichyFrance) {
            this.changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "FRRoundel", "FR/FRRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "FRRoundel", "FR/FRRoundel.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryIndia) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "IDRoundel", "ID/IDRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "IDRoundel", "ID/IDRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "IDTail", "ID/IDTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryPakistan) {
            this.changeMat(hiermesh, "Overlay2", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay3", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "PKRoundel", "PK/PKRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "PKRoundel", "PK/PKRoundel.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "PKTail", "PK/PKTail.tga", 1.0F, 1.0F, 1.0F);
            return;
        } else {
            return;
        }
    }
}
