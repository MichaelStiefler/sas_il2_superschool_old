// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/12/2012 8:09:49 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Mig_19.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;

import java.io.IOException;
import java.io.PrintStream;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme2, Aircraft, TypeSupersonic, TypeFighter, 
//            TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, 
//            Cockpit, PaintScheme, EjectionSeat

public class Mig_19 extends Scheme2
    implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeRadar
{

    public float getDragForce(float f, float f1, float f2, float f3)
    {
        throw new UnsupportedOperationException("getDragForce not supported anymore.");
    }

    public float getDragInGravity(float f, float f1, float f2, float f3, float f4, float f5)
    {
        throw new UnsupportedOperationException("getDragInGravity supported anymore.");
    }

    public float getForceInGravity(float f, float f1, float f2)
    {
        throw new UnsupportedOperationException("getForceInGravity supported anymore.");
    }

    public float getDegPerSec(float f, float f1)
    {
        throw new UnsupportedOperationException("getDegPerSec supported anymore.");
    }

    public float getGForce(float f, float f1)
    {
        throw new UnsupportedOperationException("getGForce supported anymore.");
    }

    public Mig_19()
    {
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        bSlatsOff = false;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        AirBrakeControl = 0.0F;
        DragChuteControl = 0.0F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        gearTargetAngle = -1F;
        gearCurrentAngle = -1F;
        hasHydraulicPressure = true;
        bCanopyInitState = false;
        radarmode = 0;
        targetnum = 0;
        lockrange = 0.035F;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(2.5F, 2.5F, 2.0F, 3F, 3F, 3F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
        actl = ((FlightModelMain) (super.FM)).SensRoll;
        ectl = ((FlightModelMain) (super.FM)).SensPitch;
        rctl = ((FlightModelMain) (super.FM)).SensYaw;
        ((FlightModelMain) (super.FM)).CT.DiffBrakesType = 3;
        System.out.println("*** Diff Brakes Set to Type: " + ((FlightModelMain) (super.FM)).CT.DiffBrakesType);
        ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
        ((FlightModelMain) (super.FM)).CT.dvCockpitDoor = 1.0F;
    }

    public void checkHydraulicStatus()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getStage() < 6 && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() < 6 && ((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr > 0)
        {
            gearTargetAngle = 90F;
            hasHydraulicPressure = false;
            ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
            ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
            ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
        } else
        if(!hasHydraulicPressure)
        {
            gearTargetAngle = 0.0F;
            hasHydraulicPressure = true;
            ((FlightModelMain) (super.FM)).CT.bHasAileronControl = true;
            ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = true;
            ((FlightModelMain) (super.FM)).CT.bHasAirBrakeControl = true;
        }
    }

    public void moveHydraulics(float f)
    {
        if(gearTargetAngle >= 0.0F)
            if(gearCurrentAngle < gearTargetAngle)
            {
                gearCurrentAngle += 90F * f * 0.8F;
                if(gearCurrentAngle >= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            } else
            {
                gearCurrentAngle -= 90F * f * 0.8F;
                if(gearCurrentAngle <= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            }
    }

    public void updateLLights()
    {
        super.pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = new LightPointWorld[4];
                for(int i = 0; i < 4; i++)
                {
                    lLight[i] = new LightPointWorld();
                    lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    lLight[i].setEmit(0.0F, 0.0F);
                    try
                    {
                        lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    }
                    catch(Exception exception) { }
                }

            }
        } else
        {
            for(int j = 0; j < 4; j++)
                if(((FlightModelMain) (super.FM)).AS.astateLandingLightEffects[j] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    Engine.land();
                    if(Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                    {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        lLight[j].setPos(lLightP2);
                        float f = (float)lLightP1.distance(lLightPL);
                        float f1 = f * 0.5F + 60F;
                        float f2 = 0.7F - (0.8F * f * lightTime) / 2000F;
                        lLight[j].setEmit(f2, f1);
                    } else
                    {
                        lLight[j].setEmit(0.0F, 0.0F);
                    }
                } else
                if(lLight[j].getR() != 0.0F)
                    lLight[j].setEmit(0.0F, 0.0F);

        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(super.FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(super.FM.isPlayers())
            bChangedPit = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = super.FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            Mig_19 mig_19 = this;
            float f1 = mn;
            World.cur().getClass();
            mig_19.mn = f1 / Atmosphere.sonicSpeed((float)((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if((((FlightModelMain) (super.FM)).Gears.nearGround() || ((FlightModelMain) (super.FM)).Gears.onGround()) && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if((((FlightModelMain) (super.FM)).Gears.nearGround() || ((FlightModelMain) (super.FM)).Gears.onGround()) && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask2_D0", false);
        else
            hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
            if(((FlightModelMain) (super.FM)).AP.way.isLanding() && super.FM.getSpeed() > ((FlightModelMain) (super.FM)).VmaxFLAPS && super.FM.getSpeed() > ((FlightModelMain) (super.FM)).AP.way.curr().getV() * 1.4F)
            {
                if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 1.0F)
                    ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 25 && ((FlightModelMain) (super.FM)).AP.way.isLanding() && super.FM.getSpeed() < ((FlightModelMain) (super.FM)).VmaxFLAPS * 1.2F)
            {
                if(super.FM.getSpeed() > ((FlightModelMain) (super.FM)).VminFLAPS * 0.5F && ((FlightModelMain) (super.FM)).Gears.nearGround())
                {
                    if(((FlightModelMain) (super.FM)).Gears.onGround())
                    {
                        if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 1.0F)
                            ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
                        ((FlightModelMain) (super.FM)).CT.DragChuteControl = 1.0F;
                    } else
                    if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 1.0F)
                        ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
                } else
                if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 0.0F)
                    ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 66)
            {
                if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 0.0F)
                    ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
            } else
            if(((Maneuver)super.FM).get_maneuver() == 7)
            {
                if(((FlightModelMain) (super.FM)).CT.AirBrakeControl != 1.0F)
                    ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
            } else
            if(hasHydraulicPressure && ((FlightModelMain) (super.FM)).CT.AirBrakeControl != 0.0F)
                ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
        if(((FlightModelMain) (super.FM)).AP.way.curr().Action == 3)
            ((FlightModelMain) (super.FM)).CT.dropFuelTanks();
        if(((FlightModelMain) (super.FM)).AP.way.curr().Action == 3 && !((Maneuver)super.FM).hasBombs())
        {
            ((FlightModelMain) (super.FM)).AP.way.next();
            ((Maneuver)super.FM).target_ground = null;
            ((Maneuver)super.FM).Group.setGroupTask(1);
        }
    }

    private final void UpdateLightIntensity()
    {
        if(World.getTimeofDay() >= 6F && World.getTimeofDay() < 7F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        else
        if(World.getTimeofDay() >= 18F && World.getTimeofDay() < 19F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        else
        if(World.getTimeofDay() >= 7F && World.getTimeofDay() < 18F)
            lightTime = 0.1F;
        else
            lightTime = 1.0F;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
        } else
        if(k14Mode == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
        } else
        if(k14Mode == 2 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Off");
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
        if(k14WingspanType == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        if(k14WingspanType == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
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

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 30D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        if(f < 0.1F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.08F, 0.0F, 0.1F);
            Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.08F, 0.0F, 0.1F);
        } else
        {
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.17F, 0.99F, 0.1F, 0.45F);
            Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.08F, 0.0F, 0.1F);
        }
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -102F));
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -105F));
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -40F), Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -23F), Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -104F));
        hiermesh.chunkSetAngles("GearR2_D0", Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, 40F), Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, 23F), Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -104F));
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = ((FlightModelMain) (super.FM)).Gears.gWheelSinking[2];
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 0.19F, 0.0F, 0.19F);
        hierMesh().chunkSetLocate("GearC22_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(f, 0.0F, 19F, 0.0F, 30F);
        hierMesh().chunkSetAngles("GearC7_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("GearC8_D0", 0.0F, 2.0F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.19F, 0.0F, 0.19F);
        hierMesh().chunkSetLocate("GearL22_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.19F, 0.0F, -0.19F);
        hierMesh().chunkSetLocate("GearR22_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder_D0", 30F * f, 0.0F, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(((FlightModelMain) (super.FM)).CT.DiffBrakesType > 0)
        {
            if(((FlightModelMain) (super.FM)).CT.getGear() < 0.75F)
                return;
            hierMesh().chunkSetAngles("GearC21_D0", 0.0F, -30F * f, 0.0F);
        }
    }

    protected void moveElevator(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -20.5F * ((FlightModelMain) (super.FM)).CT.getElevator());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -20.5F * ((FlightModelMain) (super.FM)).CT.getElevator());
        } else
        if(f < 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -10F * ((FlightModelMain) (super.FM)).CT.getElevator());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -10F * ((FlightModelMain) (super.FM)).CT.getElevator());
        }
    }

    protected void moveAileron(float f)
    {
        if(super.FM.getSpeedKMH() > 570F)
        {
            float f1 = 1.5F * f;
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f1);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f1);
        } else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f);
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = 38F * f;
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0, 1F, 0.0F, 0.25F);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0, 1F, 0.0F, 0.25F);
        hierMesh().chunkSetAngles("Flap1_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetLocate("Flap1a_D0", Aircraft.xyz, Aircraft.ypr);
        //Aircraft.xyz[1] = Aircraft.cvt(f, 0, 1F, 0.0F, 1.1F);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0, 1F, 0.0F, -0.25F);
        hierMesh().chunkSetAngles("Flap2_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetLocate("Flap2a_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFan(float f)
    {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = part(s);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            	if(s.startsWith("xxcontrols"))
                {
                    debuggunnery("Controls: Hit..");
                    int j = s.charAt(10) - 48;
                    switch(j)
                    {
                    case 1: // '\001'
                    case 2: // '\002'
                        if(World.Rnd().nextFloat() < -10.5F && getEnergyPastArmor(1.1F, shot) > 200.0F)
                        {
                            debuggunnery("Controls: Ailerones Controls: Out..");
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;
                    case 3: // '\003'
                    case 4: // '\004'
                        if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200.0F && World.Rnd().nextFloat() < -10.25F)
                        {
                            debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                        }
                        if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200.00F && World.Rnd().nextFloat() <-10.25F)
                        {
                            debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
                    }
            } else
            	if(s.startsWith("xxengine1"))
                {
                    debuggunnery("Engine Module: Hit..");
                    if(s.endsWith("bloc"))
                        getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 20F)
                    {
                        ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                        debuggunnery("Engine Module: Engine Cams Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 44000F)
                        {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                            debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                        }
                        if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                        {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                            debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                        }
                    }
                    if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                        debuggunnery("Engine Module: Hit - Engine Fires..");
                    }
                    s.endsWith("exht");
                } else
                	if(s.startsWith("xxengine2"))
                    {
                        debuggunnery("Engine Module: Hit..");
                        if(s.endsWith("bloc"))
                            getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                        if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[1].getCylindersRatio() * 20F)
                        {
                            ((FlightModelMain) (super.FM)).EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                            debuggunnery("Engine Module: Engine Cams Hit, " + ((FlightModelMain) (super.FM)).EI.engines[1].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[1].getCylinders() + " Right..");
                            if(World.Rnd().nextFloat() < shot.power / 44000F)
                            {
                                ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 2);
                                debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                            }
                            if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                            {
                                ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 1);
                                debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                            }
                        }
                        if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                        {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 3);
                            debuggunnery("Engine Module: Hit - Engine Fires..");
                        }
                        s.endsWith("exht");
                    } else	
            if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: mnine Gun (" + k + ") Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, l, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            } else
            if(s.startsWith("xxhyd"))
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
            if(s.startsWith("xnose"))
                hitChunk("Nose", shot);
            else
            if(s.startsWith("xtail"))
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            if(s.startsWith("xkeel"))
            {
                if(chunkDamageVisible("Keel1") < 2)
                    hitChunk("Keel1", shot);
            } else
            if(s.startsWith("xrudder"))
                hitChunk("Rudder1", shot);
            else
            if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                    hitChunk("StabR", shot);
            } else
            if(s.startsWith("xvator"))
            {
                if(s.startsWith("xvatorl"))
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr"))
                    hitChunk("VatorR", shot);
            } else
            if(s.startsWith("xwing"))
            {
                if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                    hitChunk("WingLIn", shot);
                if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                    hitChunk("WingRIn", shot);
                if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                    hitChunk("WingRMid", shot);
                if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                    hitChunk("WingLOut", shot);
                if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                    hitChunk("WingROut", shot);
            } else
            if(s.startsWith("xarone"))
            {
                if(s.startsWith("xaronel"))
                    hitChunk("AroneL", shot);
                if(s.startsWith("xaroner"))
                    hitChunk("AroneR", shot);
            } else
            if(s.startsWith("xgear"))
            {
                if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
                }
            } else
            if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int i1;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    i1 = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    i1 = s.charAt(6) - 49;
                } else
                {
                    i1 = s.charAt(5) - 49;
                }
                hitFlesh(i1, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
label0:
        switch(i)
        {
        default:
            break;

        case 13: // '\r'
            ((FlightModelMain) (super.FM)).Gears.cgear = false;
            float f = World.Rnd().nextFloat();
            for(int k = 0; k < 2; k++)
            {
                if(f < 0.1F)
                {
                    ((FlightModelMain) (super.FM)).AS.hitEngine(this, k, 100);
                    if((double)World.Rnd().nextFloat() < 0.48999999999999999D)
                        ((FlightModelMain) (super.FM)).EI.engines[k].setEngineDies(actor);
                    break label0;
                }
                if((double)f > 0.55000000000000004D)
                    ((FlightModelMain) (super.FM)).EI.engines[k].setEngineDies(actor);
            }

            break;

        case 34: // '"'
            ((FlightModelMain) (super.FM)).Gears.lgear = false;
            break;

        case 37: // '%'
            ((FlightModelMain) (super.FM)).Gears.rgear = false;
            break;

        case 19: // '\023'
            for(int l = 0; l < 2; l++)
            {
                ((FlightModelMain) (super.FM)).CT.bHasAirBrakeControl = false;
                ((FlightModelMain) (super.FM)).EI.engines[l].setEngineDies(actor);
            }

            break;

        case 11: // '\013'
            ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
            ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
            ((FlightModelMain) (super.FM)).CT.bHasRudderTrim = false;
            ((FlightModelMain) (super.FM)).CT.bHasElevatorTrim = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (super.FM)).actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)((Interpolate) (super.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 800F)
                k14Distance = 800F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
        }
    }

    public float getAirPressure(float f)
    {
        float f1 = 1.0F - (0.0065F * f) / 288.15F;
        float f2 = 5.255781F;
        return 101325F * (float)Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f)
    {
        return getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f)
    {
        return (getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * f));
    }

    public float getAirDensityFactor(float f)
    {
        return getAirDensity(f) / 1.225F;
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float calculateMach()
    {
        return super.FM.getSpeedKMH() / getMachForAlt(super.FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(super.FM.getAltitude()) - super.FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = super.FM.getSpeedKMH() - getMachForAlt(super.FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if((double)calculateMach() <= 1.0D)
        {
            super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if((double)calculateMach() >= 1.0D)
        {
            super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(((FlightModelMain) (super.FM)).VmaxAllowed > 1500F)
            super.FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(super.FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if((double)calculateMach() > 1.01D || (double)calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster())
        {
            for(int i = 0; i < 2; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
                            ((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                ((FlightModelMain) (super.FM)).AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                ((FlightModelMain) (super.FM)).EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
                            ((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            {
                                if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                ((FlightModelMain) (super.FM)).EI.engines[i].setEngineStops(this);
                            } else
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                    }
                    oldthrl = curthrl;
                }

        }
    }
    
    private float flapsMovement(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if(f6 < f1)
        {
            f6 += f4 * f;
            if(f6 > f1)
                f6 = f1;
        } else
        if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
        return f6;
    }

    public void update(float f)
    {
        if((((FlightModelMain) (super.FM)).AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = false;
            bailout();
        }
        if(((FlightModelMain) (super.FM)).CT.getFlap() < ((FlightModelMain) (super.FM)).CT.FlapsControl)
            ((FlightModelMain) (super.FM)).CT.forceFlaps(flapsMovement(f, ((FlightModelMain) (super.FM)).CT.FlapsControl, ((FlightModelMain) (super.FM)).CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        else
            ((FlightModelMain) (super.FM)).CT.forceFlaps(flapsMovement(f, ((FlightModelMain) (super.FM)).CT.FlapsControl, ((FlightModelMain) (super.FM)).CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        float f2 = super.FM.getSpeedKMH() - 1000F;
        if(f2 < 0.0F)
            f2 = 0.0F;
        ((FlightModelMain) (super.FM)).CT.dvGear = 0.2F - f2 / 1000F;
        if(((FlightModelMain) (super.FM)).CT.dvGear < 0.0F)
            ((FlightModelMain) (super.FM)).CT.dvGear = 0.0F;
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
        {
            if(((FlightModelMain) (super.FM)).AP.way.isLanding() && ((FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() > 40F)
            {
                ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
                if(((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
                    ((FlightModelMain) (super.FM)).CT.DragChuteControl = 1.0F;
            }
            if(((FlightModelMain) (super.FM)).AP.way.isLanding() && ((FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() < 40F)
            {
                ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
                if(super.FM.getSpeed() < 20F)
                    ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
            }
        }
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
        {
            for(int i = 0; i < 2; i++)
            {
                if(((FlightModelMain) (super.FM)).EI.engines[i].getPowerOutput() > 0.25F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6)
                {
                    if(((FlightModelMain) (super.FM)).EI.engines[i].getPowerOutput() > 1.001F)
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, i, 5);
                    else
                    if(((FlightModelMain) (super.FM)).EI.engines[i].getPowerOutput() > 0.65F && ((FlightModelMain) (super.FM)).EI.engines[i].getPowerOutput() < 1.001F)
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, i, 3);
                    else
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, i, 2);
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, i, 0);
                }
                setExhaustFlame(Math.round(Aircraft.cvt(((FlightModelMain) (super.FM)).EI.engines[i].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            }

            if(super.FM instanceof RealFlightModel)
                umn();
        }
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        moveHydraulics(f);
        soundbarier();
        super.update(f);
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k]);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 2: // '\002'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5: // '\005'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 4: // '\004'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    protected void moveAirBrake(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("AirbrakeC1_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("AirbrakeC3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.75F, 10F, -22F));
        hierMesh().chunkSetAngles("AirbrakeC5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.75F, 10F, -22F));
        hierMesh().chunkSetAngles("AirbrakeL1_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("AirbrakeL3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.75F, 10F, -27F));
        hierMesh().chunkSetAngles("AirbrakeR1_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("AirbrakeR3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.75F, 10F, -27F));
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -0.14F);
        hierMesh().chunkSetLocate("AirbrakeC2_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -0.14F);
        hierMesh().chunkSetLocate("AirbrakeL2_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -0.14F);
        hierMesh().chunkSetLocate("AirbrakeR2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void setExhaustFlame(int i, int j)
    {
        if(j == 0)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            default:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            }
    }

    private void bailout()
    {
        if(overrideBailout)
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 0 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep < 2)
            {
                if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.5F)
                {
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 2;
                }
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 2 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 3)
            {
                switch(((FlightModelMain) (super.FM)).AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    doRemoveBlisters();
                    break;
                }
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate = ((FlightModelMain) (super.FM)).AS;
                aircraftstate.astateBailoutStep = (byte)(aircraftstate.astateBailoutStep + 1);
                if(((FlightModelMain) (super.FM)).AS.astateBailoutStep == 4)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 11 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 19)
            {
                byte byte0 = ((FlightModelMain) (super.FM)).AS.astateBailoutStep;
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate1 = ((FlightModelMain) (super.FM)).AS;
                aircraftstate1.astateBailoutStep = (byte)(aircraftstate1.astateBailoutStep + 1);
                if(byte0 == 11)
                {
                    super.FM.setTakenMortalDamage(true, null);
                    if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(((FlightModelMain) (super.FM)).AS.actor != World.getPlayerAircraft())
                            ((Maneuver)super.FM).set_maneuver(44);
                    }
                }
                if(((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11] < 99)
                {
                    doRemoveBodyFromPlane(byte0 - 10);
                    if(byte0 == 11)
                    {
                        doEjectCatapult();
                        super.FM.setTakenMortalDamage(true, null);
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) (super.FM)).Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(((FlightModelMain) (super.FM)).Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    private final void umn()
    {
        Vector3d vector3d = super.FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        Mig_19 mig_19 = this;
        float f = mn;
        World.cur().getClass();
        mig_19.mn = f / Atmosphere.sonicSpeed((float)((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z);
        if(mn >= lteb)
            ts = true;
        else
            ts = false;
    }

    public boolean ist()
    {
        return ts;
    }

    public float gmnr()
    {
        return mn;
    }

    public boolean inr()
    {
        return ictl;
    }
    
    public void typeRadarGainMinus() {
		// TODO Auto-generated method stub
    	
    	if(this.radarmode == 0)
    	{
    	lockrange -= 0.005F;
    	if(lockrange < 0.01F)
			lockrange = 0.01F;
    	} else
    	{
    		targetnum--;
        	if(targetnum<0)
        		targetnum = 0;
        	HUD.log(AircraftHotKeys.hudLogWeaponId, "Target number  " + targetnum);	
    	}
	}

	public void typeRadarGainPlus() {
		// TODO Auto-generated method stub
		if(this.radarmode == 0)
    	{
		lockrange += 0.005F;
		if(lockrange>0.035F)
			lockrange = 0.035F;
    	} else
    	{
    		targetnum++;
    		if(targetnum > 10)
        		targetnum = 0;
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "Target number  " + targetnum);
    	}
	}

	public void typeRadarRangeMinus() 
	{
    		
	}

	public void typeRadarRangePlus() 
	{

	}

	public void typeRadarReplicateFromNet(NetMsgInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void typeRadarReplicateToNet(NetMsgGuaranted arg0)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	public boolean typeRadarToggleMode() {
		radarmode++;
		if(radarmode>1)
			radarmode=0;		
		return false;
	}

    protected boolean bSlatsOff;
    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public float AirBrakeControl;
    public float DragChuteControl;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[];
    private static Loc lLightLoc1 = new Loc();
    private static Point3d lLightP1 = new Point3d();
    private static Point3d lLightP2 = new Point3d();
    private static Point3d lLightPL = new Point3d();
    private boolean ictl;
    private static float mteb = 1.0F;
    private float mn;
    private static float uteb = 1.25F;
    private static float lteb = 0.92F;
    private float actl;
    private float rctl;
    private float ectl;
    private boolean ts;
    private float H1;
    public static boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    public static int LockState = 0;
    static Actor hunted = null;
    private float engineSurgeDamage;
    private float gearTargetAngle;
    private float gearCurrentAngle;
    public boolean hasHydraulicPressure;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    private boolean bCanopyInitState;
    public int radarmode;
    public int targetnum;
    public float lockrange;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.Mig_19.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}