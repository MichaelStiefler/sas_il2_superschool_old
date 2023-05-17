// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode
// Source File Name: CockpitBF_109Z.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
// CockpitPilot, Aircraft, AircraftLH

public class CockpitBF_109Z extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitBF_109Z.this.setTmp = CockpitBF_109Z.this.setOld;
            CockpitBF_109Z.this.setOld = CockpitBF_109Z.this.setNew;
            CockpitBF_109Z.this.setNew = CockpitBF_109Z.this.setTmp;
            CockpitBF_109Z.this.setNew.altimeter = CockpitBF_109Z.this.fm.getAltitude();
            if (CockpitBF_109Z.this.cockpitDimControl) {
                if (CockpitBF_109Z.this.setNew.dimPosition > 0.0F) {
                    CockpitBF_109Z.this.setNew.dimPosition = CockpitBF_109Z.this.setOld.dimPosition - 0.05F;
                }
            } else if (CockpitBF_109Z.this.setNew.dimPosition < 1.0F) {
                CockpitBF_109Z.this.setNew.dimPosition = CockpitBF_109Z.this.setOld.dimPosition + 0.05F;
            }
            CockpitBF_109Z.this.setNew.throttle1 = (0.91F * CockpitBF_109Z.this.setOld.throttle1) + (0.09F * CockpitBF_109Z.this.fm.EI.engines[0].getControlThrottle());
            CockpitBF_109Z.this.setNew.throttle2 = (0.91F * CockpitBF_109Z.this.setOld.throttle2) + (0.09F * CockpitBF_109Z.this.fm.EI.engines[1].getControlThrottle());
            CockpitBF_109Z.this.setNew.mix1 = (0.88F * CockpitBF_109Z.this.setOld.mix1) + (0.12F * CockpitBF_109Z.this.fm.EI.engines[0].getControlMix());
            CockpitBF_109Z.this.setNew.mix2 = (0.88F * CockpitBF_109Z.this.setOld.mix2) + (0.12F * CockpitBF_109Z.this.fm.EI.engines[1].getControlMix());
            float f = CockpitBF_109Z.this.waypointAzimuth();
            if (CockpitBF_109Z.this.useRealisticNavigationInstruments()) {
                CockpitBF_109Z.this.setNew.waypointAzimuth.setDeg(f - 90F);
                CockpitBF_109Z.this.setOld.waypointAzimuth.setDeg(f - 90F);
            } else {
                CockpitBF_109Z.this.setNew.waypointAzimuth.setDeg(CockpitBF_109Z.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitBF_109Z.this.setOld.azimuth.getDeg(1.0F));
            }
            CockpitBF_109Z.this.setNew.azimuth.setDeg(CockpitBF_109Z.this.setOld.azimuth.getDeg(1.0F), CockpitBF_109Z.this.fm.Or.azimut());
            CockpitBF_109Z.this.w.set(CockpitBF_109Z.this.fm.getW());
            CockpitBF_109Z.this.fm.Or.transform(CockpitBF_109Z.this.w);
            CockpitBF_109Z.this.setNew.turn = ((12F * CockpitBF_109Z.this.setOld.turn) + CockpitBF_109Z.this.w.z) / 13F;
            CockpitBF_109Z.this.buzzerFX((CockpitBF_109Z.this.fm.CT.getGear() < 0.999999F) && (CockpitBF_109Z.this.fm.CT.getFlap() > 0.0F));
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        float      throttle1;
        float      throttle2;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      turn;
        float      mix1;
        float      mix2;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitBF_109Z() {
        super("3DO/Cockpit/Bf-109Z/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManifold1 = 0.0F;
        this.pictManifold2 = 0.0F;
        this.w = new Vector3f();
        this.setNew.dimPosition = 1.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "ZClocks1", "ZClocks1DMG", "ZClocks2", "ZClocks3", "FW190A4Compass" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.loadBuzzerFX();
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON05");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
        }
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReviTinter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReviTint", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ATA1", this.cvt(this.pictManifold1 = (0.75F * this.pictManifold1) + (0.25F * this.fm.EI.engines[0].getManifoldPressure()), 0.6F, 1.8F, 0.0F, 329F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ATA2", this.cvt(this.pictManifold2 = (0.75F * this.pictManifold2) + (0.25F * this.fm.EI.engines[1].getManifoldPressure()), 0.6F, 1.8F, 0.0F, 329F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitBF_109Z.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitBF_109Z.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitBF_109Z.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelQuantity1", -44.5F + this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 8F), CockpitBF_109Z.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp2", this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress2", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_Azimuth1", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.waypointAzimuth.getDeg(f), 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("Z_Azimuth1", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_PropPitch1", 270F - ((float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch2", 105F - ((float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch3", 270F - ((float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin()) * 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch4", 105F - ((float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin()) * 5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(6D), -6F, 6F, -4.5F, 4.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Horizon1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("Z_Horizon2", this.cvt(this.fm.Or.getTangage(), -45F, 45F, -13F, 13F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_FuelWarning1", this.fm.M.fuel < 36F);
        this.mesh.chunkVisible("Z_GearLRed1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_GearRRed1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_GearLGreen1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_GearRGreen1", this.fm.CT.getGear() == 1.0F);
        if (this.gun[0] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter1", this.cvt(this.gun[0].countBullets(), 0.0F, 250F, 15F, 0.0F), 0.0F, 0.0F);
        }
        if (this.gun[1] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter2", this.cvt(this.gun[1].countBullets(), 0.0F, 250F, 15F, 0.0F), 0.0F, 0.0F);
        }
        if (this.gun[2] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter3", this.cvt(this.gun[2].countBullets(), 0.0F, 250F, 15F, 0.0F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Z_PedalStrut", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", this.fm.CT.getAileron() * 15F, 0.0F, this.fm.CT.getElevator() * 10F);
        if ((this.fm.AS.astateCockpitState & 8) == 0) {
            this.mesh.chunkSetAngles("Z_Throttle", this.interp(this.setNew.throttle1, this.setOld.throttle1, f) * 68.18182F, 0.0F, 0.0F);
        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {
            this.mesh.chunkSetAngles("Z_Throttle2", this.interp(this.setNew.throttle2, this.setOld.throttle2, f) * 68.18182F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Mix", this.interp(this.setNew.mix1, this.setOld.mix2, f) * 62.5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix2", this.interp(this.setNew.mix1, this.setOld.mix2, f) * 62.5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", -45F + (28.333F * this.fm.EI.engines[0].getControlMagnetos()), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwit1", -45F + (28.333F * this.fm.EI.engines[1].getControlMagnetos()), 0.0F, 0.0F);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.005F, 0.5F);
            this.light2.light.setEmit(0.005F, 0.5F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes3_D1", true);
            this.mesh.chunkVisible("Revi_D0", false);
            this.mesh.chunkVisible("Z_ReviTint", false);
            this.mesh.chunkVisible("Z_ReviTinter", false);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Revi_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("PoppedPanel_D0", false);
            this.mesh.chunkVisible("Z_Repeater1", false);
            this.mesh.chunkVisible("Z_Azimuth1", false);
            this.mesh.chunkVisible("Z_Compass1", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("PoppedPanel_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
            this.mesh.chunkVisible("Radio_D0", false);
            this.mesh.chunkVisible("Radio_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) == 0) {
            ;
        }
    }

    private Gun                gun[]              = { null, null, null };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManifold1;
    private float              pictManifold2;
    public Vector3f            w;
    private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]         = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]        = { 0.0F, 9F, 21F, 29.5F, 37F, 48F, 61.5F, 75.5F, 92F, 92F };
}
