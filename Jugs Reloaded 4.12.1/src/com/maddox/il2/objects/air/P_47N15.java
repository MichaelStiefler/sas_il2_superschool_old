package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class P_47N15 extends P_47Z
    implements TypeFighterAceMaker
{

    public P_47N15()
    {
        bCanopyInitState = false;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
        ((FlightModelMain) (super.FM)).CT.dvCockpitDoor = 1.0F;
    }

    public void update(float f)
    {
        super.update(f);
        if(!bCanopyInitState && super.FM.isStationedOnGround())
        {
            ((FlightModelMain) (super.FM)).AS.setCockpitDoor((Aircraft)((Interpolate) (super.FM)).actor, 1);
            ((FlightModelMain) (super.FM)).CT.cockpitDoorControl = 1.0F;
            bCanopyInitState = true;
            System.out.println("*** Initial canopy state: " + (((FlightModelMain) (super.FM)).CT.getCockpitDoor() == 1.0F ? "open" : "closed"));
        }
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[2] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private boolean bCanopyInitState;

    static 
    {
        Class class1 = P_47N15.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47N-15(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47N-15(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-47N15.fmd:P47N_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_47N15K.class });
        Property.set(class1, "LOSElevation", 1.1104F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 1, 1, 1, 1, 9, 3, 
            3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb02", 
            "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock01", "_ExternalRock02", 
            "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", 
            "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev10"
        });
        String as[] = new String[35];
        as[0] = "MGunBrowning50k 300";
        as[1] = "MGunBrowning50k 300";
        as[2] = "MGunBrowning50k 300";
        as[3] = "MGunBrowning50k 300";
        as[4] = "MGunBrowning50k 300";
        as[5] = "MGunBrowning50k 300";
        as[6] = "MGunBrowning50k 300";
        as[7] = "MGunBrowning50k 300";
        Aircraft.weaponsRegister(class1, "default300rpg", as);
        String as1[] = new String[35];
        as1[0] = "MGunBrowning50k 500";
        as1[1] = "MGunBrowning50k 500";
        as1[2] = "MGunBrowning50k 500";
        as1[3] = "MGunBrowning50k 500";
        as1[4] = "MGunBrowning50k 500";
        as1[5] = "MGunBrowning50k 500";
        as1[6] = "MGunBrowning50k 500";
        as1[7] = "MGunBrowning50k 500";
        Aircraft.weaponsRegister(class1, "extra500rpg", as1);
        String as2[] = new String[35];
        as2[0] = "MGunBrowning50k 267";
        as2[1] = "MGunBrowning50k 267";
        as2[2] = "MGunBrowning50k 267";
        as2[3] = "MGunBrowning50k 267";
        as2[4] = "MGunBrowning50k 267";
        as2[5] = "MGunBrowning50k 267";
        as2[6] = "MGunBrowning50k 267";
        as2[7] = "MGunBrowning50k 267";
        as2[9] = "BombGun500lbs 1";
        as2[10] = "BombGun500lbs 1";
        Aircraft.weaponsRegister(class1, "2x500lbs267rpg", as2);
        String as3[] = new String[35];
        as3[0] = "MGunBrowning50k 267";
        as3[1] = "MGunBrowning50k 267";
        as3[2] = "MGunBrowning50k 267";
        as3[3] = "MGunBrowning50k 267";
        as3[4] = "MGunBrowning50k 267";
        as3[5] = "MGunBrowning50k 267";
        as3[6] = "MGunBrowning50k 267";
        as3[7] = "MGunBrowning50k 267";
        as3[13] = "BombGun1000lbs 1";
        Aircraft.weaponsRegister(class1, "1x1000lbs", as3);
        String as4[] = new String[35];
        as4[0] = "MGunBrowning50k 267";
        as4[1] = "MGunBrowning50k 267";
        as4[2] = "MGunBrowning50k 267";
        as4[3] = "MGunBrowning50k 267";
        as4[4] = "MGunBrowning50k 267";
        as4[5] = "MGunBrowning50k 267";
        as4[6] = "MGunBrowning50k 267";
        as4[7] = "MGunBrowning50k 267";
        as4[9] = "BombGun500lbs 1";
        as4[10] = "BombGun500lbs 1";
        as4[13] = "BombGun1000lbs 1";
        Aircraft.weaponsRegister(class1, "2x500lbs1x1000lbs", as4);
        String as5[] = new String[35];
        as5[0] = "MGunBrowning50k 267";
        as5[1] = "MGunBrowning50k 267";
        as5[2] = "MGunBrowning50k 267";
        as5[3] = "MGunBrowning50k 267";
        as5[4] = "MGunBrowning50k 267";
        as5[5] = "MGunBrowning50k 267";
        as5[6] = "MGunBrowning50k 267";
        as5[7] = "MGunBrowning50k 267";
        as5[11] = "BombGun1000lbs 1";
        as5[12] = "BombGun1000lbs 1";
        Aircraft.weaponsRegister(class1, "2x1000lbs", as5);
        String as6[] = new String[35];
        as6[0] = "MGunBrowning50k 267";
        as6[1] = "MGunBrowning50k 267";
        as6[2] = "MGunBrowning50k 267";
        as6[3] = "MGunBrowning50k 267";
        as6[4] = "MGunBrowning50k 267";
        as6[5] = "MGunBrowning50k 267";
        as6[6] = "MGunBrowning50k 267";
        as6[7] = "MGunBrowning50k 267";
        as6[11] = "BombGun1000lbs 1";
        as6[12] = "BombGun1000lbs 1";
        as6[13] = "BombGun500lbs 1";
        Aircraft.weaponsRegister(class1, "2x1000lbs1x500lbs", as6);
        String as7[] = new String[35];
        as7[0] = "MGunBrowning50k 267";
        as7[1] = "MGunBrowning50k 267";
        as7[2] = "MGunBrowning50k 267";
        as7[3] = "MGunBrowning50k 267";
        as7[4] = "MGunBrowning50k 267";
        as7[5] = "MGunBrowning50k 267";
        as7[6] = "MGunBrowning50k 267";
        as7[7] = "MGunBrowning50k 267";
        as7[11] = "BombGun1000lbs 1";
        as7[12] = "BombGun1000lbs 1";
        as7[13] = "BombGun1000lbs 1";
        Aircraft.weaponsRegister(class1, "3x1000lbs", as7);
        String as8[] = new String[35];
        as8[0] = "MGunBrowning50k 267";
        as8[1] = "MGunBrowning50k 267";
        as8[2] = "MGunBrowning50k 267";
        as8[3] = "MGunBrowning50k 267";
        as8[4] = "MGunBrowning50k 267";
        as8[5] = "MGunBrowning50k 267";
        as8[6] = "MGunBrowning50k 267";
        as8[7] = "MGunBrowning50k 267";
        as8[18] = "RocketGunHVAR5 1";
        as8[19] = "RocketGunHVAR5 1";
        as8[20] = "RocketGunHVAR5 1";
        as8[21] = "RocketGunHVAR5 1";
        as8[22] = "RocketGunHVAR5 1";
        as8[23] = "RocketGunHVAR5 1";
        as8[24] = "RocketGunHVAR5 1";
        as8[25] = "RocketGunHVAR5 1";
        as8[26] = "RocketGunHVAR5 1";
        as8[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "10xHVAR", as8);
        String as9[] = new String[35];
        as9[0] = "MGunBrowning50k 267";
        as9[1] = "MGunBrowning50k 267";
        as9[2] = "MGunBrowning50k 267";
        as9[3] = "MGunBrowning50k 267";
        as9[4] = "MGunBrowning50k 267";
        as9[5] = "MGunBrowning50k 267";
        as9[6] = "MGunBrowning50k 267";
        as9[7] = "MGunBrowning50k 267";
        as9[9] = "BombGun500lbs 1";
        as9[10] = "BombGun500lbs 1";
        as9[18] = "RocketGunHVAR5 1";
        as9[19] = "RocketGunHVAR5 1";
        as9[20] = "RocketGunHVAR5 1";
        as9[21] = "RocketGunHVAR5 1";
        as9[22] = "RocketGunHVAR5 1";
        as9[23] = "RocketGunHVAR5 1";
        as9[24] = "RocketGunHVAR5 1";
        as9[25] = "RocketGunHVAR5 1";
        as9[26] = "RocketGunHVAR5 1";
        as9[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "10xHVAR2x500lbs", as9);
        String as10[] = new String[35];
        as10[0] = "MGunBrowning50k 267";
        as10[1] = "MGunBrowning50k 267";
        as10[2] = "MGunBrowning50k 267";
        as10[3] = "MGunBrowning50k 267";
        as10[4] = "MGunBrowning50k 267";
        as10[5] = "MGunBrowning50k 267";
        as10[6] = "MGunBrowning50k 267";
        as10[7] = "MGunBrowning50k 267";
        as10[13] = "BombGun1000lbs 1";
        as10[18] = "RocketGunHVAR5 1";
        as10[19] = "RocketGunHVAR5 1";
        as10[20] = "RocketGunHVAR5 1";
        as10[21] = "RocketGunHVAR5 1";
        as10[22] = "RocketGunHVAR5 1";
        as10[23] = "RocketGunHVAR5 1";
        as10[24] = "RocketGunHVAR5 1";
        as10[25] = "RocketGunHVAR5 1";
        as10[26] = "RocketGunHVAR5 1";
        as10[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "10xHVAR1x1000lbs", as10);
        String as11[] = new String[35];
        as11[0] = "MGunBrowning50k 267";
        as11[1] = "MGunBrowning50k 267";
        as11[2] = "MGunBrowning50k 267";
        as11[3] = "MGunBrowning50k 267";
        as11[4] = "MGunBrowning50k 267";
        as11[5] = "MGunBrowning50k 267";
        as11[6] = "MGunBrowning50k 267";
        as11[7] = "MGunBrowning50k 267";
        as11[9] = "BombGun500lbs 1";
        as11[10] = "BombGun500lbs 1";
        as11[13] = "BombGun1000lbs 1";
        as11[18] = "RocketGunHVAR5 1";
        as11[19] = "RocketGunHVAR5 1";
        as11[20] = "RocketGunHVAR5 1";
        as11[21] = "RocketGunHVAR5 1";
        as11[22] = "RocketGunHVAR5 1";
        as11[23] = "RocketGunHVAR5 1";
        as11[24] = "RocketGunHVAR5 1";
        as11[25] = "RocketGunHVAR5 1";
        as11[26] = "RocketGunHVAR5 1";
        as11[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "10xHVAR2x500lbs1x1000lbs", as11);
        String as12[] = new String[35];
        as12[0] = "MGunBrowning50k 267";
        as12[1] = "MGunBrowning50k 267";
        as12[2] = "MGunBrowning50k 267";
        as12[3] = "MGunBrowning50k 267";
        as12[4] = "MGunBrowning50k 267";
        as12[5] = "MGunBrowning50k 267";
        as12[6] = "MGunBrowning50k 267";
        as12[7] = "MGunBrowning50k 267";
        as12[11] = "BombGun1000lbs 1";
        as12[12] = "BombGun1000lbs 1";
        as12[18] = "RocketGunHVAR5 1";
        as12[19] = "RocketGunHVAR5 1";
        as12[20] = "RocketGunHVAR5 1";
        as12[21] = "RocketGunHVAR5 1";
        as12[22] = "RocketGunHVAR5 1";
        as12[23] = "RocketGunHVAR5 1";
        as12[24] = "RocketGunHVAR5 1";
        as12[25] = "RocketGunHVAR5 1";
        as12[26] = "RocketGunHVAR5 1";
        as12[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "10xHVAR2x1000lbs", as12);
        String as13[] = new String[35];
        as13[0] = "MGunBrowning50k 267";
        as13[1] = "MGunBrowning50k 267";
        as13[2] = "MGunBrowning50k 267";
        as13[3] = "MGunBrowning50k 267";
        as13[4] = "MGunBrowning50k 267";
        as13[5] = "MGunBrowning50k 267";
        as13[6] = "MGunBrowning50k 267";
        as13[7] = "MGunBrowning50k 267";
        as13[16] = "PylonP47Nextra 1";
        as13[17] = "PylonP47Nextra 1";
        as13[18] = "RocketGunHVAR5 1";
        as13[19] = "RocketGunHVAR5 1";
        as13[20] = "RocketGunHVAR5 1";
        as13[21] = "RocketGunHVAR5 1";
        as13[22] = "RocketGunHVAR5 1";
        as13[23] = "RocketGunHVAR5 1";
        as13[24] = "RocketGunHVAR5 1";
        as13[25] = "RocketGunHVAR5 1";
        as13[26] = "RocketGunHVAR5 1";
        as13[27] = "RocketGunHVAR5 1";
        as13[28] = "RocketGunHVAR5 1";
        as13[29] = "RocketGunHVAR5 1";
        as13[30] = "RocketGunHVAR5 1";
        as13[31] = "RocketGunHVAR5 1";
        as13[32] = "RocketGunHVAR5 1";
        as13[33] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "16xHVAR", as13);
        String as14[] = new String[35];
        as14[0] = "MGunBrowning50k 267";
        as14[1] = "MGunBrowning50k 267";
        as14[2] = "MGunBrowning50k 267";
        as14[3] = "MGunBrowning50k 267";
        as14[4] = "MGunBrowning50k 267";
        as14[5] = "MGunBrowning50k 267";
        as14[6] = "MGunBrowning50k 267";
        as14[7] = "MGunBrowning50k 267";
        as14[13] = "BombGun500lbs 1";
        as14[16] = "PylonP47Nextra 1";
        as14[17] = "PylonP47Nextra 1";
        as14[18] = "RocketGunHVAR5 1";
        as14[19] = "RocketGunHVAR5 1";
        as14[20] = "RocketGunHVAR5 1";
        as14[21] = "RocketGunHVAR5 1";
        as14[22] = "RocketGunHVAR5 1";
        as14[23] = "RocketGunHVAR5 1";
        as14[24] = "RocketGunHVAR5 1";
        as14[25] = "RocketGunHVAR5 1";
        as14[26] = "RocketGunHVAR5 1";
        as14[27] = "RocketGunHVAR5 1";
        as14[28] = "RocketGunHVAR5 1";
        as14[29] = "RocketGunHVAR5 1";
        as14[30] = "RocketGunHVAR5 1";
        as14[31] = "RocketGunHVAR5 1";
        as14[32] = "RocketGunHVAR5 1";
        as14[33] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "16xHVAR1x500lbs", as14);
        String as15[] = new String[35];
        as15[0] = "MGunBrowning50k 267";
        as15[1] = "MGunBrowning50k 267";
        as15[2] = "MGunBrowning50k 267";
        as15[3] = "MGunBrowning50k 267";
        as15[4] = "MGunBrowning50k 267";
        as15[5] = "MGunBrowning50k 267";
        as15[6] = "MGunBrowning50k 267";
        as15[7] = "MGunBrowning50k 267";
        as15[13] = "BombGun1000lbs 1";
        as15[16] = "PylonP47Nextra 1";
        as15[17] = "PylonP47Nextra 1";
        as15[18] = "RocketGunHVAR5 1";
        as15[19] = "RocketGunHVAR5 1";
        as15[20] = "RocketGunHVAR5 1";
        as15[21] = "RocketGunHVAR5 1";
        as15[22] = "RocketGunHVAR5 1";
        as15[23] = "RocketGunHVAR5 1";
        as15[24] = "RocketGunHVAR5 1";
        as15[25] = "RocketGunHVAR5 1";
        as15[26] = "RocketGunHVAR5 1";
        as15[27] = "RocketGunHVAR5 1";
        as15[28] = "RocketGunHVAR5 1";
        as15[29] = "RocketGunHVAR5 1";
        as15[30] = "RocketGunHVAR5 1";
        as15[31] = "RocketGunHVAR5 1";
        as15[32] = "RocketGunHVAR5 1";
        as15[33] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "16xHVAR1x1000lbs", as15);
        String as16[] = new String[35];
        as16[0] = "MGunBrowning50k 267";
        as16[1] = "MGunBrowning50k 267";
        as16[2] = "MGunBrowning50k 267";
        as16[3] = "MGunBrowning50k 267";
        as16[4] = "MGunBrowning50k 267";
        as16[5] = "MGunBrowning50k 267";
        as16[6] = "MGunBrowning50k 267";
        as16[7] = "MGunBrowning50k 267";
        as16[8] = "FuelTankGun_Tank75gal 1";
        as16[9] = "BombGun500lbs 1";
        as16[10] = "BombGun500lbs 1";
        Aircraft.weaponsRegister(class1, "tank75gal2x500lbs", as16);
        String as17[] = new String[35];
        as17[0] = "MGunBrowning50k 267";
        as17[1] = "MGunBrowning50k 267";
        as17[2] = "MGunBrowning50k 267";
        as17[3] = "MGunBrowning50k 267";
        as17[4] = "MGunBrowning50k 267";
        as17[5] = "MGunBrowning50k 267";
        as17[6] = "MGunBrowning50k 267";
        as17[7] = "MGunBrowning50k 267";
        as17[8] = "FuelTankGun_Tank75gal 1";
        as17[18] = "RocketGunHVAR5 1";
        as17[19] = "RocketGunHVAR5 1";
        as17[20] = "RocketGunHVAR5 1";
        as17[21] = "RocketGunHVAR5 1";
        as17[22] = "RocketGunHVAR5 1";
        as17[23] = "RocketGunHVAR5 1";
        as17[24] = "RocketGunHVAR5 1";
        as17[25] = "RocketGunHVAR5 1";
        as17[26] = "RocketGunHVAR5 1";
        as17[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "tank75gal10xHVAR", as17);
        String as18[] = new String[35];
        as18[0] = "MGunBrowning50k 267";
        as18[1] = "MGunBrowning50k 267";
        as18[2] = "MGunBrowning50k 267";
        as18[3] = "MGunBrowning50k 267";
        as18[4] = "MGunBrowning50k 267";
        as18[5] = "MGunBrowning50k 267";
        as18[6] = "MGunBrowning50k 267";
        as18[7] = "MGunBrowning50k 267";
        as18[8] = "FuelTankGun_Tank75gal 1";
        as18[9] = "BombGun500lbs 1";
        as18[10] = "BombGun500lbs 1";
        as18[18] = "RocketGunHVAR5 1";
        as18[19] = "RocketGunHVAR5 1";
        as18[20] = "RocketGunHVAR5 1";
        as18[21] = "RocketGunHVAR5 1";
        as18[22] = "RocketGunHVAR5 1";
        as18[23] = "RocketGunHVAR5 1";
        as18[24] = "RocketGunHVAR5 1";
        as18[25] = "RocketGunHVAR5 1";
        as18[26] = "RocketGunHVAR5 1";
        as18[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "tank75gal10xHVAR2x500lbs", as18);
        String as19[] = new String[35];
        as19[0] = "MGunBrowning50k 267";
        as19[1] = "MGunBrowning50k 267";
        as19[2] = "MGunBrowning50k 267";
        as19[3] = "MGunBrowning50k 267";
        as19[4] = "MGunBrowning50k 267";
        as19[5] = "MGunBrowning50k 267";
        as19[6] = "MGunBrowning50k 267";
        as19[7] = "MGunBrowning50k 267";
        as19[8] = "FuelTankGun_Tank75gal 1";
        as19[16] = "PylonP47Nextra 1";
        as19[17] = "PylonP47Nextra 1";
        as19[18] = "RocketGunHVAR5 1";
        as19[19] = "RocketGunHVAR5 1";
        as19[20] = "RocketGunHVAR5 1";
        as19[21] = "RocketGunHVAR5 1";
        as19[22] = "RocketGunHVAR5 1";
        as19[23] = "RocketGunHVAR5 1";
        as19[24] = "RocketGunHVAR5 1";
        as19[25] = "RocketGunHVAR5 1";
        as19[26] = "RocketGunHVAR5 1";
        as19[27] = "RocketGunHVAR5 1";
        as19[28] = "RocketGunHVAR5 1";
        as19[29] = "RocketGunHVAR5 1";
        as19[30] = "RocketGunHVAR5 1";
        as19[31] = "RocketGunHVAR5 1";
        as19[32] = "RocketGunHVAR5 1";
        as19[33] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "16xHVAR1xtank75gal", as19);
        Aircraft.weaponsRegister(class1, "2x1000lbs1x110gal", new String[] {
            "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", null, null, 
            null, "BombGun1000lbs 1", "BombGun1000lbs 1", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, "FuelTankGun_Tank110gal2 1"
        });
        String as20[] = new String[35];
        as20[0] = "MGunBrowning50k 267";
        as20[1] = "MGunBrowning50k 267";
        as20[2] = "MGunBrowning50k 267";
        as20[3] = "MGunBrowning50k 267";
        as20[4] = "MGunBrowning50k 267";
        as20[5] = "MGunBrowning50k 267";
        as20[6] = "MGunBrowning50k 267";
        as20[7] = "MGunBrowning50k 267";
        as20[9] = "FuelTankGun_Tank108gal2 1";
        as20[10] = "FuelTankGun_Tank108gal2 1";
        as20[18] = "RocketGunHVAR5 1";
        as20[19] = "RocketGunHVAR5 1";
        as20[20] = "RocketGunHVAR5 1";
        as20[21] = "RocketGunHVAR5 1";
        as20[22] = "RocketGunHVAR5 1";
        as20[23] = "RocketGunHVAR5 1";
        as20[24] = "RocketGunHVAR5 1";
        as20[25] = "RocketGunHVAR5 1";
        as20[26] = "RocketGunHVAR5 1";
        as20[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "10xHVAR2x108galDTK", as20);
        String as21[] = new String[35];
        as21[0] = "MGunBrowning50k 267";
        as21[1] = "MGunBrowning50k 267";
        as21[2] = "MGunBrowning50k 267";
        as21[3] = "MGunBrowning50k 267";
        as21[4] = "MGunBrowning50k 267";
        as21[5] = "MGunBrowning50k 267";
        as21[6] = "MGunBrowning50k 267";
        as21[7] = "MGunBrowning50k 267";
        as21[14] = "FuelTankGun_Tank165gal 1";
        as21[15] = "FuelTankGun_Tank165gal 1";
        as21[18] = "RocketGunHVAR5 1";
        as21[19] = "RocketGunHVAR5 1";
        as21[20] = "RocketGunHVAR5 1";
        as21[21] = "RocketGunHVAR5 1";
        as21[22] = "RocketGunHVAR5 1";
        as21[23] = "RocketGunHVAR5 1";
        as21[24] = "RocketGunHVAR5 1";
        as21[25] = "RocketGunHVAR5 1";
        as21[26] = "RocketGunHVAR5 1";
        as21[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "10xHVAR2x165galDTK", as21);
        String as22[] = new String[35];
        as22[0] = "MGunBrowning50k 267";
        as22[1] = "MGunBrowning50k 267";
        as22[2] = "MGunBrowning50k 267";
        as22[3] = "MGunBrowning50k 267";
        as22[4] = "MGunBrowning50k 267";
        as22[5] = "MGunBrowning50k 267";
        as22[6] = "MGunBrowning50k 267";
        as22[7] = "MGunBrowning50k 267";
        as22[13] = "BombGun1000lbs 1";
        as22[14] = "FuelTankGun_Tank165gal 1";
        as22[15] = "FuelTankGun_Tank165gal 1";
        as22[18] = "RocketGunHVAR5 1";
        as22[19] = "RocketGunHVAR5 1";
        as22[20] = "RocketGunHVAR5 1";
        as22[21] = "RocketGunHVAR5 1";
        as22[22] = "RocketGunHVAR5 1";
        as22[23] = "RocketGunHVAR5 1";
        as22[24] = "RocketGunHVAR5 1";
        as22[25] = "RocketGunHVAR5 1";
        as22[26] = "RocketGunHVAR5 1";
        as22[27] = "RocketGunHVAR5 1";
        Aircraft.weaponsRegister(class1, "10xHVAR2x165galDTK1x1000lbs", as22);
        String as23[] = new String[35];
        as23[0] = "MGunBrowning50k 267";
        as23[1] = "MGunBrowning50k 267";
        as23[2] = "MGunBrowning50k 267";
        as23[3] = "MGunBrowning50k 267";
        as23[4] = "MGunBrowning50k 267";
        as23[5] = "MGunBrowning50k 267";
        as23[6] = "MGunBrowning50k 267";
        as23[7] = "MGunBrowning50k 267";
        as23[8] = "FuelTankGun_Tank75gal 1";
        Aircraft.weaponsRegister(class1, "tank75gal", as23);
        Aircraft.weaponsRegister(class1, "tank108gal", new String[] {
            "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, "FuelTankGun_Tank108gal2 1"
        });
        String as24[] = new String[35];
        as24[0] = "MGunBrowning50k 267";
        as24[1] = "MGunBrowning50k 267";
        as24[2] = "MGunBrowning50k 267";
        as24[3] = "MGunBrowning50k 267";
        as24[4] = "MGunBrowning50k 267";
        as24[5] = "MGunBrowning50k 267";
        as24[6] = "MGunBrowning50k 267";
        as24[7] = "MGunBrowning50k 267";
        as24[8] = "FuelTankGun_Tank200gal 1";
        Aircraft.weaponsRegister(class1, "tank200gal", as24);
        String as25[] = new String[35];
        as25[0] = "MGunBrowning50k 267";
        as25[1] = "MGunBrowning50k 267";
        as25[2] = "MGunBrowning50k 267";
        as25[3] = "MGunBrowning50k 267";
        as25[4] = "MGunBrowning50k 267";
        as25[5] = "MGunBrowning50k 267";
        as25[6] = "MGunBrowning50k 267";
        as25[7] = "MGunBrowning50k 267";
        as25[9] = "FuelTankGun_Tank108gal2 1";
        as25[10] = "FuelTankGun_Tank108gal2 1";
        Aircraft.weaponsRegister(class1, "2x108galDTK", as25);
        String as26[] = new String[35];
        as26[0] = "MGunBrowning50k 267";
        as26[1] = "MGunBrowning50k 267";
        as26[2] = "MGunBrowning50k 267";
        as26[3] = "MGunBrowning50k 267";
        as26[4] = "MGunBrowning50k 267";
        as26[5] = "MGunBrowning50k 267";
        as26[6] = "MGunBrowning50k 267";
        as26[7] = "MGunBrowning50k 267";
        as26[14] = "FuelTankGun_Tank110gal2 1";
        as26[15] = "FuelTankGun_Tank110gal2 1";
        Aircraft.weaponsRegister(class1, "2x110galDTK", as26);
        String as27[] = new String[35];
        as27[0] = "MGunBrowning50k 267";
        as27[1] = "MGunBrowning50k 267";
        as27[2] = "MGunBrowning50k 267";
        as27[3] = "MGunBrowning50k 267";
        as27[4] = "MGunBrowning50k 267";
        as27[5] = "MGunBrowning50k 267";
        as27[6] = "MGunBrowning50k 267";
        as27[7] = "MGunBrowning50k 267";
        as27[8] = "FuelTankGun_Tank75gal 1";
        as27[14] = "FuelTankGun_Tank75gal 1";
        as27[15] = "FuelTankGun_Tank75gal 1";
        Aircraft.weaponsRegister(class1, "3xtank75galDTK", as27);
        String as28[] = new String[35];
        as28[0] = "MGunBrowning50k 267";
        as28[1] = "MGunBrowning50k 267";
        as28[2] = "MGunBrowning50k 267";
        as28[3] = "MGunBrowning50k 267";
        as28[4] = "MGunBrowning50k 267";
        as28[5] = "MGunBrowning50k 267";
        as28[6] = "MGunBrowning50k 267";
        as28[7] = "MGunBrowning50k 267";
        as28[8] = "FuelTankGun_Tank75gal 1";
        as28[9] = "FuelTankGun_Tank108gal2 1";
        as28[10] = "FuelTankGun_Tank108gal2 1";
        Aircraft.weaponsRegister(class1, "tank75gal2x108galDTK", as28);
        String as29[] = new String[35];
        as29[0] = "MGunBrowning50k 267";
        as29[1] = "MGunBrowning50k 267";
        as29[2] = "MGunBrowning50k 267";
        as29[3] = "MGunBrowning50k 267";
        as29[4] = "MGunBrowning50k 267";
        as29[5] = "MGunBrowning50k 267";
        as29[6] = "MGunBrowning50k 267";
        as29[7] = "MGunBrowning50k 267";
        as29[8] = "FuelTankGun_Tank75gal 1";
        as29[14] = "FuelTankGun_Tank110gal2 1";
        as29[15] = "FuelTankGun_Tank110gal2 1";
        Aircraft.weaponsRegister(class1, "tank75gal2x110galDTK", as29);
        Aircraft.weaponsRegister(class1, "3x108galDTK", new String[] {
            "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", "MGunBrowning50k 267", null, "FuelTankGun_Tank108gal2 1", 
            "FuelTankGun_Tank108gal2 1", null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, "FuelTankGun_Tank108gal2 1"
        });
        String as30[] = new String[35];
        as30[0] = "MGunBrowning50k 267";
        as30[1] = "MGunBrowning50k 267";
        as30[2] = "MGunBrowning50k 267";
        as30[3] = "MGunBrowning50k 267";
        as30[4] = "MGunBrowning50k 267";
        as30[5] = "MGunBrowning50k 267";
        as30[6] = "MGunBrowning50k 267";
        as30[7] = "MGunBrowning50k 267";
        as30[14] = "FuelTankGun_Tank165gal 1";
        as30[15] = "FuelTankGun_Tank165gal 1";
        Aircraft.weaponsRegister(class1, "2x165galDTK", as30);
        String as31[] = new String[35];
        as31[0] = "MGunBrowning50k 267";
        as31[1] = "MGunBrowning50k 267";
        as31[2] = "MGunBrowning50k 267";
        as31[3] = "MGunBrowning50k 267";
        as31[4] = "MGunBrowning50k 267";
        as31[5] = "MGunBrowning50k 267";
        as31[6] = "MGunBrowning50k 267";
        as31[7] = "MGunBrowning50k 267";
        as31[8] = "FuelTankGun_Tank75gal 1";
        as31[14] = "FuelTankGun_Tank165gal 1";
        as31[15] = "FuelTankGun_Tank165gal 1";
        Aircraft.weaponsRegister(class1, "tank75gal2x165galDTK", as31);
        String as32[] = new String[35];
        as32[0] = "MGunBrowning50k 267";
        as32[1] = "MGunBrowning50k 267";
        as32[2] = "MGunBrowning50k 267";
        as32[3] = "MGunBrowning50k 267";
        as32[4] = "MGunBrowning50k 267";
        as32[5] = "MGunBrowning50k 267";
        as32[6] = "MGunBrowning50k 267";
        as32[7] = "MGunBrowning50k 267";
        as32[14] = "FuelTankGun_Tank200gal 1";
        as32[15] = "FuelTankGun_Tank200gal 1";
        Aircraft.weaponsRegister(class1, "2x200galDTK", as32);
        String as33[] = new String[35];
        as33[0] = "MGunBrowning50k 267";
        as33[1] = "MGunBrowning50k 267";
        as33[2] = "MGunBrowning50k 267";
        as33[3] = "MGunBrowning50k 267";
        as33[4] = "MGunBrowning50k 267";
        as33[5] = "MGunBrowning50k 267";
        as33[6] = "MGunBrowning50k 267";
        as33[7] = "MGunBrowning50k 267";
        as33[14] = "FuelTankGun_TankP38 1";
        as33[15] = "FuelTankGun_TankP38 1";
        Aircraft.weaponsRegister(class1, "2x300galDTK", as33);
        String as34[] = new String[35];
        as34[0] = "MGunBrowning50k 267";
        as34[1] = "MGunBrowning50k 267";
        as34[2] = "MGunBrowning50k 267";
        as34[3] = "MGunBrowning50k 267";
        as34[4] = "MGunBrowning50k 267";
        as34[5] = "MGunBrowning50k 267";
        as34[6] = "MGunBrowning50k 267";
        as34[7] = "MGunBrowning50k 267";
        as34[8] = "FuelTankGun_Tank75gal 1";
        as34[14] = "FuelTankGun_TankP38 1";
        as34[15] = "FuelTankGun_TankP38 1";
        Aircraft.weaponsRegister(class1, "tank75gal2x300galDTK", as34);
        String as35[] = new String[35];
        as35[0] = "MGunBrowning50k 0";
        as35[1] = "MGunBrowning50k 0";
        as35[2] = "MGunBrowning50k 0";
        as35[3] = "MGunBrowning50k 0";
        as35[4] = "MGunBrowning50k 0";
        as35[5] = "MGunBrowning50k 0";
        as35[6] = "MGunBrowning50k 0";
        as35[7] = "MGunBrowning50k 0";
        Aircraft.weaponsRegister(class1, "none", as35);
    }
}
