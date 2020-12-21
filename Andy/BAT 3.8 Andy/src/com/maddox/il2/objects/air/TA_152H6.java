// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 15.07.2020 17:13:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   TA_152H6.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            TA_152H_Paulus, PaintSchemeFMPar05, TypeFighter, TypeFighterAceMaker, 
//            Aircraft, NetAircraft

public class TA_152H6 extends com.maddox.il2.objects.air.TA_152H_Paulus
    implements com.maddox.il2.objects.air.TypeFighter, com.maddox.il2.objects.air.TypeFighterAceMaker
{

    public TA_152H6()
    {
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        kangle = 0.0F;
        SecondProp = 0;
        flapps = -0.5F;
    }

    public float getEyeLevelCorrection()
    {
        return 0.09F;
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 20F * f, 0.0F, 0.0F);
        float f1 = java.lang.Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        com.maddox.il2.objects.air.Aircraft.xyz[0] = com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.xyz[2] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.ypr[0] = com.maddox.il2.objects.air.Aircraft.ypr[1] = com.maddox.il2.objects.air.Aircraft.ypr[2] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.xyz[2] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.1F);
        hiermesh.chunkSetLocate("poleL_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        hiermesh.chunkSetLocate("poleR_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
    }

    protected void moveGear(float f)
    {
        com.maddox.il2.objects.air.TA_152H6.moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.getGear() < 0.98F)
        {
            return;
        } else
        {
            hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.44F, 0.0F, 0.44F);
        hierMesh().chunkSetLocate("GearL2a_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.44F, 0.0F, 0.44F);
        hierMesh().chunkSetLocate("GearR2a_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
    }

    protected void moveFan(float f)
    {
        hierMesh().chunkFind(com.maddox.il2.objects.air.Aircraft.Props[1][0]);
        SecondProp = 1;
        int i = 0;
        for(int j = 0; j < (SecondProp == 1 ? 2 : 1); j++)
        {
            if(oldProp[j] < 2)
            {
                i = java.lang.Math.abs((int)(FM.EI.engines[0].getw() * 0.06F));
                if(i >= 1)
                    i = 1;
                if(i != oldProp[j] && hierMesh().isChunkVisible(com.maddox.il2.objects.air.Aircraft.Props[j][oldProp[j]]))
                {
                    hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[j][oldProp[j]], false);
                    oldProp[j] = i;
                    hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[j][i], true);
                }
            }
            if(i == 0)
            {
                propPos[j] = (propPos[j] + 57.3F * FM.EI.engines[0].getw() * f) % 360F;
            } else
            {
                float f1 = 57.3F * FM.EI.engines[0].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if(f1 <= 0.5F)
                    f1 *= 2.0F;
                else
                    f1 = f1 * 2.0F - 2.0F;
                f1 *= 1200F;
                propPos[j] = (propPos[j] + f1 * f) % 360F;
            }
            if(j == 0)
                hierMesh().chunkSetAngles(com.maddox.il2.objects.air.Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
            else
                hierMesh().chunkSetAngles(com.maddox.il2.objects.air.Aircraft.Props[j][i], 0.0F, propPos[j] - 20F, 0.0F);
        }

    }

    public void hitProp(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        if(i > FM.EI.getNum() - 1 || oldProp[i] == 2)
            return;
        if((isChunkAnyDamageVisible("Prop" + (i + 1)) || isChunkAnyDamageVisible("PropRot" + (i + 1))) && SecondProp == 1)
        {
            hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[i + 1][0], false);
            hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[i + 1][1], false);
            hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[i + 1][2], true);
        }
        super.hitProp(i, j, actor);
    }

    public void update(float f)
    {
        if(java.lang.Math.abs(flapps - kangle) > 0.01F)
        {
            flapps = kangle;
            for(int i = 1; i < 15; i++)
            {
                java.lang.String s = "Water" + i + "_D0";
                hierMesh().chunkSetAngles(s, 0.0F, -10F * kangle, 0.0F);
            }

        }
        kangle = 0.95F * kangle + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator();
        if(((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Loc)).z > 9000D)
        {
            if(!((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getControlAfterburner())
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].setAfterburnerType(2);
        } else
        if(!((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getControlAfterburner())
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].setAfterburnerType(1);
        com.maddox.il2.ai.World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
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
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted)
        throws java.io.IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput)
        throws java.io.IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
    }


    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private float kangle;
    private float flapps;
    protected int SecondProp;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.TA_152H6.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Ta.152");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Ta-152H-5/hierCRP.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "yearService", 1945.5F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1948F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Ta-152H-6.fmd:Ta152H_Paulus_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitTA_152H6.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.764106F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 1, 1, 1, 1, 9
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01"
        });
    }
}