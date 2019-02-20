package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SeafangFMk32 extends Seafang
    implements TypeBNZFighter
{

    public SeafangFMk32()
    {
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        flapps = 0.0F;
        SecondProp = 0;
        arrestor = 0.0F;
        plega = true;
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(isNet() && isNetMirror())
            return;
        if(s.startsWith("Hook"))
        {
            return;
        } else
        {
            super.msgCollision(actor, s, s1);
            return;
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = -65F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, -30F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, -0.247F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float)Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook2_D0", 0.0F, -35F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = 0.3385F * f;
        hierMesh().chunkSetLocate("Hook1_D0", Aircraft.xyz, Aircraft.ypr);
        arrestor = f;
    }

    protected void moveFan(float f)
    {
        hierMesh().chunkFind(Aircraft.Props[1][0]);
        SecondProp = 1;
        int i = 0;
        for(int j = 0; j < (SecondProp != 1 ? 1 : 2); j++)
        {
            if(oldProp[j] < 2)
            {
                i = Math.abs((int)(FM.EI.engines[0].getw() * 0.06F));
                if(i >= 1)
                    i = 1;
                if(i != oldProp[j] && hierMesh().isChunkVisible(Aircraft.Props[j][oldProp[j]]))
                {
                    hierMesh().chunkVisible(Aircraft.Props[j][oldProp[j]], false);
                    oldProp[j] = i;
                    hierMesh().chunkVisible(Aircraft.Props[j][i], true);
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
                hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
            else
                hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, -propPos[j], 0.0F);
        }

    }

    public void hitProp(int i, int j, Actor actor)
    {
        if(i > FM.EI.getNum() - 1 || oldProp[i] == 2)
            return;
        if((isChunkAnyDamageVisible("Prop" + (i + 1)) || isChunkAnyDamageVisible("PropRot" + (i + 1))) && SecondProp == 1)
        {
            hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
            hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
            hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
        }
        super.hitProp(i, j, actor);
    }

    public void update(float f)
    {
        super.update(f);
        float f1 = FM.CT.getArrestor();
        float f2 = 81F * f1 * f1 * f1 * f1 * f1 * f1 * f1;
        if(f1 > 0.01F)
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                arrestor = Aircraft.cvt(FM.Gears.arrestorVAngle, -f2, f2, -f2, f2);
                moveArrestorHook(f1);
                if(FM.Gears.arrestorVAngle >= -81F);
            } else
            {
                float f3 = 58F * FM.Gears.arrestorVSink;
                if(f3 > 0.0F && FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                arrestor += f3;
                if(arrestor > f2)
                    arrestor = f2;
                if(arrestor < -f2)
                    arrestor = -f2;
                moveArrestorHook(f1);
            }
        float f4 = this.FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f4) > 0.01F)
        {
            flapps = f4;
            hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * f4, 0.0F);
            hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * f4, 0.0F);
        }
        if(plega)
        {
            hierMesh().chunkVisible("BraceR", false);
            hierMesh().chunkVisible("BraceL", false);
        } else
        if(FM.Gears.onGround())
        {
            hierMesh().chunkVisible("BraceR", true);
            hierMesh().chunkVisible("BraceL", true);
        }
        FM.EI.engines[0].addVside *= 9.9999999999999995E-008D;
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -85F * f, 0.0F);
    }

    public void moveWingFold(float f)
    {
        moveWingFold(hierMesh(), f);
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
            plega = true;
        } else
        {
            setGunPodsOn(false);
            FM.CT.WeaponControl[0] = false;
            hideWingWeapons(true);
            plega = false;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19:
            FM.CT.bHasArrestorControl = false;
            break;
        }
        return super.cutFM(i, j, actor);
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

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private float arrestor;
    private float flapps;
    protected int SecondProp;
    public static boolean bChangedPit = false;
    private boolean plega;

    static 
    {
        Class class1 = SeafangFMk32.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Seafang");
        Property.set(class1, "meshName", "3DO/Plane/SeafangFMk32/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1974F);
        Property.set(class1, "FlightModel", "FlightModels/Seafang.fmd:Seafang_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSeafangFMk32.class
        });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 1, 1, 9, 3, 9, 9, 
            3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 2, 2, 2, 2, 2, 2, 2, 2, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON04", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", 
            "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", 
            "_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev13"
        });
    }
}
