package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SPITFIREF24 extends SPITFIRE9
    implements TypeFighterAceMaker, TypeBNZFighter, TypeStormovik
{

    public SPITFIREF24()
    {
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        kl = 1.0F;
        kr = 1.0F;
        kc = 1.0F;
    }

    public void hitDaSilk()
    {
        if(okToJump)
            super.hitDaSilk();
        else
        if(this.FM.isPlayers() || isNetPlayer() || !this.FM.AS.isPilotDead(0))
            if(this.FM.CT.getCockpitDoor() < 1.0F && !bailingOut)
            {
                bailingOut = true;
                this.FM.AS.setCockpitDoor(this, 1);
            } else
            if(this.FM.CT.getCockpitDoor() == 1.0F && !bailingOut)
            {
                bailingOut = true;
                okToJump = true;
                canopyForward = true;
                super.hitDaSilk();
            }
        if(!sideDoorOpened && this.FM.AS.bIsAboutToBailout && !this.FM.AS.isPilotDead(0))
        {
            sideDoorOpened = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.4F, 1.0F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.2F, 0.4F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f * kl, 0.2F, 0.8F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f * kl, 0.0F, 0.2F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f * kr, 0.4F, 1.0F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f * kr, 0.2F, 0.4F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f * kc, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f * kc, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f * kc, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        if(this.FM.Gears.isHydroOperable())
        {
            kl = 1.0F;
            kr = 1.0F;
            kc = 1.0F;
        }
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, -0.247F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveCockpitDoor(float f)
    {
        if(bailingOut && f >= 1.0F && !canopyForward)
        {
            canopyForward = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        } else
        if(canopyForward && !sideDoorOpened)
        {
            hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
            if(f >= 1.0F)
            {
                okToJump = true;
                this.hitDaSilk();
            }
        } else
        {
            try
            {
                if(this.FM.CT.bMoveSideDoor)
                {
                    sideDoorOpened = true;
                    hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
                    return;
                }
            }
            catch(Throwable throwable) { }
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[1] = f * 0.548F;
            hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
            float f1 = (float)Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
            hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
            hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        }
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(okToJump && this.FM.CT.getCockpitDoor() >= 1.0F && canopyForward && bailingOut && !this.FM.AS.bIsAboutToBailout)
        {
            AircraftState.bCheckPlayerAircraft = false;
            this.hitDaSilk();
            AircraftState.bCheckPlayerAircraft = false;
        }
    }

    public void update(float f)
    {
        super.update(f);
        hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * kangle, 0.0F);
        kangle = 0.95F * kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        if(kangle > 1.0F)
            kangle = 1.0F;
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

    protected void gearDamageFX(String s)
    {
        if(s.startsWith("xgearl") || s.startsWith("GearL"))
        {
            if(this.FM.isPlayers())
                HUD.log("Left Gear:  Hydraulic system Failed");
            kl = World.Rnd().nextFloat();
            kr = World.Rnd().nextFloat() * kl;
            kc = 0.1F;
            cutGearCovers("L");
        } else
        if(s.startsWith("xgearr") || s.startsWith("GearR"))
        {
            if(this.FM.isPlayers())
                HUD.log("Right Gear:  Hydraulic system Failed");
            kr = World.Rnd().nextFloat();
            kl = World.Rnd().nextFloat() * kr;
            kc = 0.1F;
            cutGearCovers("R");
        } else
        {
            if(this.FM.isPlayers())
                HUD.log("Center Gear:  Hydraulic system Failed");
            kc = World.Rnd().nextFloat();
            kl = World.Rnd().nextFloat() * kc;
            kr = World.Rnd().nextFloat() * kc;
            cutGearCovers("C");
        }
        this.FM.CT.GearControl = 1.0F;
        this.FM.Gears.setHydroOperable(false);
    }

    private void cutGearCovers(String s)
    {
        Vector3d vector3d = new Vector3d();
        if(World.Rnd().nextFloat() < 0.3F)
        {
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Gear" + s + 5 + "_D0"));
            wreckage.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + 5 + "_D0", false);
            Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("Gear" + s + 6 + "_D0"));
            wreckage1.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage1.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + 6 + "_D0", false);
        } else
        if(World.Rnd().nextFloat() < 0.3F)
        {
            int i = World.Rnd().nextInt(2) + 5;
            Wreckage wreckage2 = new Wreckage(this, hierMesh().chunkFind("Gear" + s + i + "_D0"));
            wreckage2.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage2.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + i + "_D0", false);
        }
    }

    private static float kl = 1.0F;
    private static float kr = 1.0F;
    private static float kc = 1.0F;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private float kangle;
    private boolean bailingOut;
    private boolean canopyForward;
    private boolean okToJump;
    private boolean sideDoorOpened;

    static 
    {
        Class class1 = SPITFIREF24.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Property.set(class1, "iconFar_shortClassName", "Spitfire F Mk.24");
        Property.set(class1, "meshName", "3DO/Plane/SpitfireF22(Multi1)/hier24.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Spitfire-F24.fmd:SPITF22");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSpitF24.class
        });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 1, 1, 9, 3, 9, 9, 3, 3, 
            9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 
            2, 2, 2, 2, 2, 2, 2, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb01", 
            "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01", 
            "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev13"
        });
    }
}
