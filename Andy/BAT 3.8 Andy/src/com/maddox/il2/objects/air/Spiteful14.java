// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 16.11.2019 08:56:56
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Spiteful14.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgOutput;
import com.maddox.rts.Property;
import java.io.IOException;

// Referenced classes of package com.maddox.il2.objects.air:
//            SPITFIRE9, TypeFighterAceMaker, TypeBNZFighter, PaintSchemeFMPar04, 
//            Aircraft, Cockpit, NetAircraft

public class Spiteful14 extends com.maddox.il2.objects.air.SPITFIRE9
    implements com.maddox.il2.objects.air.TypeFighterAceMaker, com.maddox.il2.objects.air.TypeBNZFighter
{

    public Spiteful14()
    {
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        flapps = 0.0F;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        com.maddox.il2.objects.air.Aircraft.xyz[2] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.55F);
        hierMesh().chunkSetLocate("Blister1_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        float f1 = (float)java.lang.Math.sin(com.maddox.il2.objects.air.Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
        {
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveFlap(float paramFloat)
    {
        float f = -50F * paramFloat;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos, boolean bDown)
    {
        if(bDown)
        {
            hiermesh.chunkSetAngles("GearC2_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(tailWheelPos, 0.75F, 0.925F, 0.0F, -55F), 0.0F);
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(tailWheelPos, 0.71F, 0.76F, 0.0F, -50F), 0.0F);
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(tailWheelPos, 0.7F, 0.75F, 0.0F, -50F), 0.0F);
        } else
        {
            hiermesh.chunkSetAngles("GearC2_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(tailWheelPos, 0.06F, 0.225F, 0.0F, -55F), 0.0F);
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(tailWheelPos, 0.02F, 0.07F, 0.0F, -50F), 0.0F);
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(tailWheelPos, 0.01F, 0.06F, 0.0F, -50F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(leftGearPos, 0.1F, 0.5F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(leftGearPos, 0.1F, 0.5F, 0.0F, -90F), 0.0F);
        if(leftGearPos < 0.5F)
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(leftGearPos, 0.01F, 0.12F, 0.0F, -90F), 0.0F);
        else
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(leftGearPos, 0.48F, 0.6F, -90F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(rightGearPos, 0.4F, 0.8F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(rightGearPos, 0.4F, 0.8F, 0.0F, -90F), 0.0F);
        if(rightGearPos < 0.5F)
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(rightGearPos, 0.3F, 0.42F, 0.0F, -90F), 0.0F);
        else
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(rightGearPos, 0.78F, 0.9F, -90F, 0.0F), 0.0F);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos)
    {
        com.maddox.il2.objects.air.Spiteful14.moveGear(hiermesh, leftGearPos, rightGearPos, tailWheelPos, true);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float tailWheelPos)
    {
        com.maddox.il2.objects.air.Spiteful14.moveGear(hierMesh(), leftGearPos, rightGearPos, tailWheelPos, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.GearControl > 0.5F);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float gearPos, boolean bDown)
    {
        com.maddox.il2.objects.air.Spiteful14.moveGear(hiermesh, gearPos, gearPos, gearPos, bDown);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float gearPos)
    {
        com.maddox.il2.objects.air.Spiteful14.moveGear(hiermesh, gearPos, gearPos, gearPos, true);
    }

    protected void moveGear(float gearPos)
    {
        com.maddox.il2.objects.air.Spiteful14.moveGear(hierMesh(), gearPos, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.GearControl > 0.5F);
    }

    public void moveSteering(float f1)
    {
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
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
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

    public void update(float f)
    {
        float radiatorPos = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator();
        if(java.lang.Math.abs(flapps - radiatorPos) > 0.01F)
        {
            flapps = radiatorPos;
            hierMesh().chunkSetAngles("Water7_D0", 0.0F, radiatorPos * -17F, 0.0F);
            hierMesh().chunkSetAngles("Water8_D0", 0.0F, radiatorPos * -17F, 0.0F);
        }
        super.update(f);
    }

    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private float flapps;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Spiteful14.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Spit");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Spiteful_FMkXIV(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        com.maddox.rts.Property.set(class1, "yearService", 1943F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1946.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Spiteful-F-XIV.fmd:SPITEFUL_XIV");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitSpiteful14.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.5926F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 1, 1
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04"
        });
    }
}