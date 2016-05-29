// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.rts.Property;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.ai.World;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Message;
import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Time;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.objects.weapons.Gun;

public class CockpitFW_190F8MSTL extends CockpitPilot
{
    private float tmp;
    private Gun[] gun;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private LightPointActor light1;
    private LightPointActor light2;
    private BulletEmitter[] bomb;
    private long t1;
    private float pictAiler;
    private float pictElev;
    public Vector3f w;
    private static final float[] speedometerScale;
    private static final float[] rpmScale;
    private static final float[] fuelScale;
    private static final float[] manPrsScale;
    private static final float[] oilfuelNeedleScale;
    private static final float[] vsiNeedleScale;
    private static final float[] oilTempScale;
    static /* synthetic */ Class class$com$maddox$il2$objects$air$CockpitFW_190F8MSTL;
    
    //------------------------------------------------------------------------
    /* skylla: Schusszaehler fix: for more info please see class CockpitFW_190A4
     * @author: SAS~Skylla
     * @params: int bullets: number of bullets still in the gun;
     * 			float offset: offset at which the ammocounter shows zero bullets left (-0.018f for outer, -0.045f for inner)
     * 			int counterrange: number of bullets the counter can display (100 for outer, 500 for inner)
     */
    private static int mg17lbullets = 0;
    private static int mg17rbullets = 0;
        
    private static float ammoCounter(int bullets, float offset, int counterrange) {
    	float f = (0.026f - offset) / counterrange;
    	bullets = Math.min(bullets, counterrange);
    	return (f * bullets + offset);
    } 
  //------------------------------------------------------------------------
    
    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30.0f);
    }
    
    public CockpitFW_190F8MSTL() {
        super("3DO/Cockpit/FW-190F-8Mistel/hier.him", "bf109");
        this.gun = new Gun[] { null, null, null, null, null, null };
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.bomb = new BulletEmitter[] { null, null, null, null, null };
        this.pictAiler = 0.0f;
        this.pictElev = 0.0f;
        this.w = new Vector3f();
        this.setNew.dimPosition = 1.0f;
        final HookNamed hookNamed = new HookNamed(this.mesh, "LIGHTHOOK_L");
        final Loc loc = new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f);
        hookNamed.computePos(this, new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126.0f, 232.0f, 245.0f);
        this.light1.light.setEmit(0.0f, 0.0f);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        final HookNamed hookNamed2 = new HookNamed(this.mesh, "LIGHTHOOK_R");
        final Loc loc2 = new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f);
        hookNamed2.computePos(this, new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f), loc2);
        this.light2 = new LightPointActor(new LightPoint(), loc2.getPoint());
        this.light2.light.setColor(126.0f, 232.0f, 245.0f);
        this.light2.light.setEmit(0.0f, 0.0f);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = new String[] { "D9GP1", "A8GP2", "D9GP3", "F8SWGP4", "F8SWGP5", "A4GP6", "A5GP3Km", "DA8GP1", "DA8GP2", "DA8GP3", "DF8SWGP4" };
        this.setNightMats(false);
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }
    
    public void reflectWorldToInstruments(final float f_54_) {
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft)this.fm.actor).getGunByHookName("_MGUN01");
            this.gun[1] = ((Aircraft)this.fm.actor).getGunByHookName("_MGUN02");
            this.gun[2] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON03");
            this.gun[3] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON04");
            this.gun[4] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[5] = ((Aircraft)this.fm.actor).getGunByHookName("_CANNON02");
        }
        if (this.bomb[0] == null) {
            for (int i = 0; i < this.bomb.length; ++i) {
                this.bomb[i] = ((Aircraft)this.fm.actor).getBulletEmitterByHookName("_ExternalBomb0" + (i + 1));
            }
            this.t1 = Time.current();
        }
        this.mesh.chunkSetAngles("NeedleALT", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f_54_), 0.0f, 10000.0f, 0.0f, 3600.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleALTKm", 0.0f, 0.0f, this.cvt(this.setNew.altimeter, 0.0f, 10000.0f, 0.0f, -180.0f));
        this.mesh.chunkSetAngles("NeedleManPress", -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6f, 1.8f, 0.0f, 336.0f), 0.0f, 0.0f);
        if (this.fm.EI.getNum() > 1) {
            this.mesh.chunkSetAngles("NeedleMP88_L", -this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6f, 1.8f, 0.0f, 336.0f), 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleMP88_R", -this.cvt(this.fm.EI.engines[2].getManifoldPressure(), 0.6f, 1.8f, 0.0f, 336.0f), 0.0f, 0.0f);
        }
        this.mesh.chunkSetAngles("NeedleKMH", -this.floatindex(this.cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0f, 900.0f, 0.0f, 9.0f), CockpitFW_190F8MSTL.speedometerScale), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleRPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0f, 4000.0f, 0.0f, 8.0f), CockpitFW_190F8MSTL.rpmScale), 0.0f, 0.0f);
        if (this.fm.EI.getNum() > 1) {
            this.mesh.chunkSetAngles("NeedleRPM88_L", -this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0f, 4000.0f, 0.0f, 8.0f), CockpitFW_190F8MSTL.rpmScale), 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleRPM88_R", -this.floatindex(this.cvt(this.fm.EI.engines[2].getRPM(), 0.0f, 4000.0f, 0.0f, 8.0f), CockpitFW_190F8MSTL.rpmScale), 0.0f, 0.0f);
        }
        this.mesh.chunkSetAngles("NeedleFuel", this.floatindex(this.cvt(this.fm.M.fuel / 0.72f, 0.0f, 400.0f, 0.0f, 4.0f), CockpitFW_190F8MSTL.fuelScale), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleOilTemp", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0f, 120.0f, 0.0f, 3.0f), CockpitFW_190F8MSTL.oilTempScale), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleFuelPress", this.cvt((this.fm.M.fuel > 1.0f) ? 0.26f : 0.0f, 0.0f, 3.0f, 0.0f, 135.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0f + 0.05f * this.fm.EI.engines[0].tOilOut, 0.0f, 15.0f, 0.0f, 135.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleAHTurn", 0.0f, this.cvt(this.setNew.turn, -0.23562f, 0.23562f, 25.0f, -25.0f), 0.0f);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0x0) {
            this.mesh.chunkSetAngles("NeedleAHBank", 0.0f, this.cvt(this.getBall(7.0), -7.0f, 7.0f, -11.0f, 11.0f), 0.0f);
        }
        this.mesh.chunkSetAngles("NeedleAHCyl", 0.0f, 0.0f, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("NeedleAHBar", 0.0f, 0.0f, this.cvt(this.fm.Or.getTangage(), -45.0f, 45.0f, 12.0f, -12.0f));
        this.mesh.chunkSetAngles("NeedleCD", (this.setNew.vspeed >= 0.0f) ? (-this.floatindex(this.cvt(this.setNew.vspeed, 0.0f, 30.0f, 0.0f, 6.0f), CockpitFW_190F8MSTL.vsiNeedleScale)) : this.floatindex(this.cvt(-this.setNew.vspeed, 0.0f, 30.0f, 0.0f, 6.0f), CockpitFW_190F8MSTL.vsiNeedleScale), 0.0f, 0.0f);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.azimuth.getDeg(f_54_) + this.setNew.waypointAzimuth.getDeg(f_54_), 0.0f, 0.0f);
            this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.waypointAzimuth.getDeg(f_54_), 0.0f, 0.0f);
        }
        else {
            this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.azimuth.getDeg(f_54_), 0.0f, 0.0f);
            this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.waypointAzimuth.getDeg(f_54_ * 0.1f), 0.0f, 0.0f);
        }
        this.mesh.chunkSetAngles("NeedleHBSmall", -105.0f + (float)Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5.0f, 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleHBLarge", -270.0f + (float)Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60.0f, 0.0f, 0.0f);
        if (this.fm.EI.getNum() > 1) {
            this.mesh.chunkSetAngles("NeedleHBS88_L", -105.0f + (float)Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin()) * 5.0f, 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleHBL88_L", -270.0f + (float)Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin()) * 60.0f, 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleHBS88_R", -105.0f + (float)Math.toDegrees(this.fm.EI.engines[2].getPropPhi() - this.fm.EI.engines[2].getPropPhiMin()) * 5.0f, 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleHBL88_R", -270.0f + (float)Math.toDegrees(this.fm.EI.engines[2].getPropPhi() - this.fm.EI.engines[2].getPropPhiMin()) * 60.0f, 0.0f, 0.0f);
        }
        if ((this.fm.AS.astateCockpitState & 0x2) == 0x0 && (this.fm.AS.astateCockpitState & 0x1) == 0x0 && (this.fm.AS.astateCockpitState & 0x4) == 0x0 && (this.fm.AS.astateCockpitState & 0x10) == 0x0) {
            this.mesh.chunkSetAngles("NeedleTrimmung", this.fm.CT.getTrimElevatorControl() * 25.0f, 0.0f, 0.0f);
        }
        if ((this.fm.AS.astateCockpitState & 0x8) == 0x0 && (this.fm.AS.astateCockpitState & 0x20) == 0x0) {
            this.mesh.chunkSetAngles("NeedleHClock", -this.cvt(World.getTimeofDay(), 0.0f, 24.0f, 0.0f, 720.0f), 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleMClock", -this.cvt(World.getTimeofDay() % 1.0f, 0.0f, 1.0f, 0.0f, 360.0f), 0.0f, 0.0f);
            this.mesh.chunkSetAngles("NeedleSClock", -this.cvt(World.getTimeofDay() % 1.0f * 60.0f % 1.0f, 0.0f, 1.0f, 0.0f, 360.0f), 0.0f, 0.0f);
        }
        this.resetYPRmodifier();
        
      //------------------------------------------------------------------------
        //skylla: Schusszaehler fix; for more info please see class CockpitFW_190A4
        if (this.gun[0] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[0].countBullets(), 0.0f, 500.0f, -0.044f, 0.0f);
            this.mesh.chunkVisible("XLampMG17_1", !this.gun[0].haveBullets());
            if(this.gun[0].haveBullets() && this.gun[0].isShots() && this.gun[0].countBullets() != mg17lbullets) {
            	this.mesh.chunkVisible("XLampMG17_1", true);
            	mg17lbullets = this.gun[0].countBullets();
            }
        }
        if (this.gun[1] != null) {
            this.mesh.chunkVisible("XLampMG17_2", !this.gun[1].haveBullets());
            if(this.gun[1].haveBullets() && this.gun[1].isShots() && this.gun[1].countBullets() != mg17rbullets) {
            	this.mesh.chunkVisible("XLampMG17_2", true);
            	mg17rbullets = this.gun[1].countBullets();
            }
        }
        if (this.gun[4] != null) {
        	Cockpit.xyz[0] = this.cvt(this.gun[4].countBullets(), 0.0f, 500.0f, -0.045f, 0.0f); 
        	//Cockpit.xyz[0] = ammoCounter(this.gun[4].countBullets(), -0.045f, 500); 
            this.mesh.chunkSetLocate("RC_MG17_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[5] != null) {
        	Cockpit.xyz[0] = this.cvt(this.gun[5].countBullets(), 0.0f, 500.0f, -0.045f, 0.0f); 
        	//Cockpit.xyz[0] = ammoCounter(this.gun[5].countBullets(), -0.045f, 500);
            this.mesh.chunkSetLocate("RC_MG17_R", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[2] != null) {
        	Cockpit.xyz[0] = ammoCounter(this.gun[2].countBullets(), -0.018f, 500); // <- new code with CockpitFW_190A4.ammoCounter(); see Cockpit class of FW-190A4 for more details
            this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[3] != null) {
        	Cockpit.xyz[0] = ammoCounter(this.gun[3].countBullets(), -0.018f, 500); // <- new code with CockpitFW_190A4.ammoCounter(); see Cockpit class of FW-190A4 for more details
            this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
        }          
        /*
        //old code:
        if (this.gun[0] != null) {
            CockpitFW_190F8MSTL.xyz[0] = this.cvt(this.gun[0].countBullets(), 0.0f, 500.0f, -0.044f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG17_L", CockpitFW_190F8MSTL.xyz, CockpitFW_190F8MSTL.ypr);
        }
        if (this.gun[1] != null) {
            CockpitFW_190F8MSTL.xyz[0] = this.cvt(this.gun[1].countBullets(), 0.0f, 500.0f, -0.044f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG17_R", CockpitFW_190F8MSTL.xyz, CockpitFW_190F8MSTL.ypr);
        }
        if (this.gun[4] != null) {
            CockpitFW_190F8MSTL.xyz[0] = this.cvt(this.gun[4].countBullets(), 0.0f, 200.0f, -0.017f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_L", CockpitFW_190F8MSTL.xyz, CockpitFW_190F8MSTL.ypr);
        }
        if (this.gun[5] != null) {
            CockpitFW_190F8MSTL.xyz[0] = this.cvt(this.gun[5].countBullets(), 0.0f, 200.0f, -0.017f, 0.0f);
            this.mesh.chunkSetLocate("RC_MG151_R", CockpitFW_190F8MSTL.xyz, CockpitFW_190F8MSTL.ypr);
        }         
        */
      //------------------------------------------------------------------------
        this.mesh.chunkVisible("XLampDrop", ((TypeDockable)this.aircraft()).typeDockableIsDocked());
        this.mesh.chunkSetAngles("IgnitionSwitch", 24.0f * this.fm.EI.engines[0].getControlMagnetos(), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("Revi16Tinter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f_54_), 0.0f, 1.0f, 0.0f, -45.0f), 0.0f, 0.0f);
        final HierMesh mesh = this.mesh;
        final String s = "Stick";
        final float n = 0.0f;
        final float pictAiler = 0.85f * this.pictAiler + 0.15f * this.fm.CT.AileronControl;
        this.pictAiler = pictAiler;
        mesh.chunkSetAngles(s, n, pictAiler * 20.0f, (this.pictElev = 0.85f * this.pictElev + 0.15f * this.fm.CT.ElevatorControl) * 20.0f);
        this.mesh.chunkSetAngles("SteeringHatUD", 30.0f * this.pictElev, 0.0f, 0.0f);
        this.mesh.chunkSetAngles("SteeringHatLR", 30.0f * this.pictAiler, 0.0f, 0.0f);
        this.resetYPRmodifier();
        CockpitFW_190F8MSTL.xyz[2] = (this.fm.CT.WeaponControl[1] ? -0.004f : 0.0f);
        this.mesh.chunkSetLocate("SecTrigger", CockpitFW_190F8MSTL.xyz, CockpitFW_190F8MSTL.ypr);
        this.resetYPRmodifier();
        CockpitFW_190F8MSTL.ypr[0] = this.interp(this.setNew.throttle0, this.setOld.throttle0, f_54_) * 34.0f * 0.91f;
        this.mesh.chunkSetLocate("ThrottleQuad", CockpitFW_190F8MSTL.xyz, CockpitFW_190F8MSTL.ypr);
        if (this.fm.EI.getNum() > 1) {
            this.mesh.chunkSetAngles("Ju88TQ_L", 0.0f, -30.0f * this.interp(this.setNew.throttle1, this.setOld.throttle1, f_54_), 0.0f);
            this.mesh.chunkSetAngles("Ju88TQ_R", 0.0f, -30.0f * this.interp(this.setNew.throttle2, this.setOld.throttle2, f_54_), 0.0f);
        }
        this.mesh.chunkSetAngles("RPedalBase", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f);
        this.mesh.chunkSetAngles("RPedalStrut", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f);
        this.mesh.chunkSetAngles("RPedal", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f - this.fm.CT.getBrake() * 15.0f);
        this.mesh.chunkSetAngles("LPedalBase", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f);
        this.mesh.chunkSetAngles("LPedalStrut", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f);
        this.mesh.chunkSetAngles("LPedal", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f - this.fm.CT.getBrake() * 15.0f);
        this.mesh.chunkVisible("XLampTankSwitch", this.fm.M.fuel > 144.0f);
        this.mesh.chunkVisible("XLampFuelLow", this.fm.M.fuel < 43.2f);
        this.mesh.chunkVisible("XLampGearL_1", this.fm.CT.getGear() < 0.05f);
        this.mesh.chunkVisible("XLampGearL_2", this.fm.CT.getGear() > 0.95f && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearR_1", this.fm.CT.getGear() < 0.05f);
        this.mesh.chunkVisible("XLampGearR_2", this.fm.CT.getGear() > 0.95f && this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("NeedleNahe1", this.cvt(this.setNew.beaconDirection, -45.0f, 45.0f, 20.0f, -20.0f), 0.0f, 0.0f);
        this.mesh.chunkSetAngles("NeedleNahe2", this.cvt(this.setNew.beaconRange, 0.0f, 1.0f, -20.0f, 20.0f), 0.0f, 0.0f);
        this.mesh.chunkVisible("AFN2_RED", this.isOnBlindLandingMarker());
    }
    
    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }
    
    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0012f, 0.75f);
            this.light2.light.setEmit(0.0012f, 0.75f);
            this.setNightMats(true);
        }
        else {
            this.light1.light.setEmit(0.0f, 0.0f);
            this.light2.light.setEmit(0.0f, 0.0f);
            this.setNightMats(false);
        }
    }
    
    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        }
        else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }
    
    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x2) != 0x0 || (this.fm.AS.astateCockpitState & 0x1) != 0x0 || (this.fm.AS.astateCockpitState & 0x4) != 0x0 || (this.fm.AS.astateCockpitState & 0x10) != 0x0) {
            this.mesh.materialReplace("D9GP1", "DA8GP1");
            this.mesh.materialReplace("D9GP1_night", "DA8GP1_night");
            this.mesh.materialReplace("A8GP4", "DA8GP4");
            this.mesh.chunkVisible("NeedleManPress", false);
            this.mesh.chunkVisible("NeedleRPM", false);
            this.mesh.chunkVisible("RepeaterOuter", false);
            this.mesh.chunkVisible("RepeaterPlane", false);
            this.mesh.chunkVisible("NeedleHBLarge", false);
            this.mesh.chunkVisible("NeedleHBSmall", false);
            this.mesh.chunkVisible("NeedleFuel", false);
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0x0) {
            this.mesh.materialReplace("A8GP2", "DA8GP2");
            this.mesh.materialReplace("A8GP2_night", "DA8GP2_night");
            this.resetYPRmodifier();
            CockpitFW_190F8MSTL.xyz[0] = 0.0f;
            CockpitFW_190F8MSTL.xyz[1] = 0.003f;
            CockpitFW_190F8MSTL.xyz[2] = 0.012f;
            CockpitFW_190F8MSTL.ypr[0] = -3.0f;
            CockpitFW_190F8MSTL.ypr[1] = -3.0f;
            CockpitFW_190F8MSTL.ypr[2] = 9.0f;
            this.mesh.chunkSetLocate("IPCentral", CockpitFW_190F8MSTL.xyz, CockpitFW_190F8MSTL.ypr);
            this.mesh.chunkVisible("NeedleAHCyl", false);
            this.mesh.chunkVisible("NeedleAHBar", false);
            this.mesh.chunkVisible("NeedleAHTurn", false);
            this.mesh.chunkVisible("NeedleFuelPress", false);
            this.mesh.chunkVisible("NeedleOilPress", false);
            this.mesh.chunkVisible("NeedleOilTemp", false);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x8) != 0x0 || (this.fm.AS.astateCockpitState & 0x20) != 0x0) {
            this.mesh.materialReplace("D9GP3", "DA8GP3");
            this.mesh.materialReplace("D9GP3_night", "DA8GP3_night");
            this.mesh.chunkVisible("NeedleKMH", false);
            this.mesh.chunkVisible("NeedleCD", false);
            this.mesh.chunkVisible("NeedleAlt", false);
            this.mesh.chunkVisible("NeedleAltKM Kill", false);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        this.retoggleLight();
    }
    
    static /* synthetic */ Class class$(final String s) {
        try {
            return Class.forName(s);
        }
        catch (ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    static {
        speedometerScale = new float[] { 0.0f, 18.5f, 67.0f, 117.0f, 164.0f, 215.0f, 267.0f, 320.0f, 379.0f, 427.0f, 428.0f };
        rpmScale = new float[] { 0.0f, 11.25f, 53.0f, 108.0f, 170.0f, 229.0f, 282.0f, 334.0f, 342.5f, 342.5f };
        fuelScale = new float[] { 0.0f, 16.0f, 35.0f, 52.5f, 72.0f, 72.0f };
        manPrsScale = new float[] { 0.0f, 0.0f, 0.0f, 15.5f, 71.0f, 125.0f, 180.0f, 235.0f, 290.0f, 245.0f, 247.0f, 247.0f };
        oilfuelNeedleScale = new float[] { 0.0f, 38.0f, 84.0f, 135.5f, 135.0f };
        vsiNeedleScale = new float[] { 0.0f, 48.0f, 82.0f, 96.5f, 111.0f, 120.5f, 130.0f, 130.0f };
        oilTempScale = new float[] { 0.0f, 23.0f, 52.0f, 81.0f, 81.0f };
        Property.set((CockpitFW_190F8MSTL.class$com$maddox$il2$objects$air$CockpitFW_190F8MSTL == null) ? (CockpitFW_190F8MSTL.class$com$maddox$il2$objects$air$CockpitFW_190F8MSTL = class$("com.maddox.il2.objects.air.CockpitFW_190F8MSTL")) : CockpitFW_190F8MSTL.class$com$maddox$il2$objects$air$CockpitFW_190F8MSTL, "normZN", 0.72f);
        Property.set((CockpitFW_190F8MSTL.class$com$maddox$il2$objects$air$CockpitFW_190F8MSTL == null) ? (CockpitFW_190F8MSTL.class$com$maddox$il2$objects$air$CockpitFW_190F8MSTL = class$("com.maddox.il2.objects.air.CockpitFW_190F8MSTL")) : CockpitFW_190F8MSTL.class$com$maddox$il2$objects$air$CockpitFW_190F8MSTL, "gsZN", 0.66f);
    }
    
    private class Variables
    {
        float altimeter;
        float throttle0;
        float throttle1;
        float throttle2;
        float dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float turn;
        float beaconDirection;
        float beaconRange;
        float vspeed;
        
        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }
    
    class Interpolater extends InterpolateRef
    {
        public boolean tick() {
            if (CockpitFW_190F8MSTL.this.fm != null) {
                CockpitFW_190F8MSTL.this.setTmp = CockpitFW_190F8MSTL.this.setOld;
                CockpitFW_190F8MSTL.this.setOld = CockpitFW_190F8MSTL.this.setNew;
                CockpitFW_190F8MSTL.this.setNew = CockpitFW_190F8MSTL.this.setTmp;
                CockpitFW_190F8MSTL.this.setNew.altimeter = CockpitFW_190F8MSTL.this.fm.getAltitude();
                if (CockpitFW_190F8MSTL.this.cockpitDimControl) {
                    if (CockpitFW_190F8MSTL.this.setNew.dimPosition > 0.0f) {
                        CockpitFW_190F8MSTL.this.setNew.dimPosition = CockpitFW_190F8MSTL.this.setOld.dimPosition - 0.05f;
                    }
                }
                else if (CockpitFW_190F8MSTL.this.setNew.dimPosition < 1.0f) {
                    CockpitFW_190F8MSTL.this.setNew.dimPosition = CockpitFW_190F8MSTL.this.setOld.dimPosition + 0.05f;
                }
                CockpitFW_190F8MSTL.this.setNew.throttle0 = 0.9f * CockpitFW_190F8MSTL.this.setOld.throttle0 + 0.1f * CockpitFW_190F8MSTL.this.fm.EI.engines[0].getControlThrottle();
                if (CockpitFW_190F8MSTL.this.fm.EI.getNum() > 1) {
                    CockpitFW_190F8MSTL.this.setNew.throttle1 = 0.9f * CockpitFW_190F8MSTL.this.setOld.throttle1 + 0.1f * CockpitFW_190F8MSTL.this.fm.EI.engines[1].getControlThrottle();
                    CockpitFW_190F8MSTL.this.setNew.throttle2 = 0.9f * CockpitFW_190F8MSTL.this.setOld.throttle2 + 0.1f * CockpitFW_190F8MSTL.this.fm.EI.engines[2].getControlThrottle();
                }
                CockpitFW_190F8MSTL.this.setNew.vspeed = (499.0f * CockpitFW_190F8MSTL.this.setOld.vspeed + CockpitFW_190F8MSTL.this.fm.getVertSpeed()) / 500.0f;
                final float waypointAzimuth = CockpitFW_190F8MSTL.this.waypointAzimuth();
                if (CockpitFW_190F8MSTL.this.useRealisticNavigationInstruments()) {
                    CockpitFW_190F8MSTL.this.setNew.waypointAzimuth.setDeg(waypointAzimuth - 90.0f);
                    CockpitFW_190F8MSTL.this.setOld.waypointAzimuth.setDeg(waypointAzimuth - 90.0f);
                }
                else {
                    CockpitFW_190F8MSTL.this.setNew.waypointAzimuth.setDeg(CockpitFW_190F8MSTL.this.setOld.waypointAzimuth.getDeg(0.1f), waypointAzimuth - CockpitFW_190F8MSTL.this.setOld.azimuth.getDeg(1.0f));
                }
                CockpitFW_190F8MSTL.this.setNew.azimuth.setDeg(CockpitFW_190F8MSTL.this.setOld.azimuth.getDeg(1.0f), CockpitFW_190F8MSTL.this.fm.Or.azimut());
                CockpitFW_190F8MSTL.this.w.set(CockpitFW_190F8MSTL.this.fm.getW());
                CockpitFW_190F8MSTL.this.fm.Or.transform(CockpitFW_190F8MSTL.this.w);
                CockpitFW_190F8MSTL.this.setNew.turn = (12.0f * CockpitFW_190F8MSTL.this.setOld.turn + CockpitFW_190F8MSTL.this.w.z) / 13.0f;
                CockpitFW_190F8MSTL.this.setNew.beaconDirection = (10.0f * CockpitFW_190F8MSTL.this.setOld.beaconDirection + CockpitFW_190F8MSTL.this.getBeaconDirection()) / 11.0f;
                CockpitFW_190F8MSTL.this.setNew.beaconRange = (10.0f * CockpitFW_190F8MSTL.this.setOld.beaconRange + CockpitFW_190F8MSTL.this.getBeaconRange()) / 11.0f;
            }
            return true;
        }
    }
}
