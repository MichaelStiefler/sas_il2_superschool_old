package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class ME_262V3 extends ME_262 {
    public void onAircraftLoaded() {
        this.getSquares();
    }

    public void moveSteering(float f) {
        if (f > 77.5F) {
            f = 77.5F;
            this.FM.Gears.steerAngle = f;
        }
        if (f < -77.5F) {
            f = -77.5F;
            this.FM.Gears.steerAngle = f;
        }
        this.hierMesh().chunkSetAngles("GearC2rear_D0", 0.0F, -f, 0.0F);
    }

    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        if (super.FM.isPlayers())
            this.FM.CT.setTrimElevatorControl(0.5F);
    }

    public void update(float f) {
        this.FM.canChangeBrakeShoe = true;
//        HUD.training("RPM: " + this.FM.EI.engines[0].getRPM());
        super.update(f);
        this.FM.canChangeBrakeShoe = true;
        if (this.FM.isPlayers()) {
            if (this.startModToStd != -1L)
                this.setSquaresStdSmoooth();
            if (this.FM.Gears.nOfGearsOnGr > 1 && this.FM.getAOA() > 15F) {
                if (!this.isOnGround) {
                    this.getSquares();
                    this.setSquaresMod();
                    this.startModToStd = -1L;
                    this.isOnGround = true;
                }
            } else {
                if (this.isOnGround && this.FM.CT.BrakeControl > 0.5F && this.FM.getSpeedKMH() > 160F) {
//                    this.setSquaresStd();
                    this.isOnGround = false;
                    this.startModToStd = Time.current();
                }
            }
            if (this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 10D && this.isOnGround) {
//                this.setSquaresStd();
                this.isOnGround = false;
                this.startModToStd = Time.current();
            }
        }
    }

    public void engineSurge(float f) {
        if (DEBUG)
            return;
        if (!this.FM.isPlayers()) {
            super.engineSurge(f);
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (this.curthrl[i] == -1F) {
                this.curthrl[i] = this.oldthrl[i] = this.FM.EI.engines[i].getControlThrottle();
            } else {
                this.curthrl[i] = this.FM.EI.engines[i].getControlThrottle();
                if ((this.curthrl[i] - this.oldthrl[i]) / f > 1.7F && this.FM.EI.engines[i].getRPM() < RPM_CRIT && this.FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.5F) {
                    if (this.FM.actor == World.getPlayerAircraft())
                        HUD.log("Compressor Stall!");
                    super.playSound("weapon.MGunMk108s", true);
                    this.engineSurgeDamage[i] += 0.01D * (this.FM.EI.engines[i].getRPM() / 1000F);
                    this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - this.engineSurgeDamage[i]);
                    if (World.Rnd().nextFloat() < 0.2F && (super.FM instanceof RealFlightModel) && ((RealFlightModel) super.FM).isRealMode())
                        this.FM.AS.hitEngine(this, i, 100);
                    if (World.Rnd().nextFloat() < 0.2F && (super.FM instanceof RealFlightModel) && ((RealFlightModel) super.FM).isRealMode())
                        this.FM.EI.engines[i].setEngineDies(this);
                }
                if ((this.curthrl[i] - this.oldthrl[i]) / f < -1.7F && (this.curthrl[i] - this.oldthrl[i]) / f > -100F && this.FM.EI.engines[i].getRPM() < RPM_CRIT && this.FM.EI.engines[i].getStage() == 6) {
                    super.playSound("weapon.MGunMk108s", true);
                    this.engineSurgeDamage[i] += 0.001D * (this.FM.EI.engines[i].getRPM() / 1000F);
                    this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - this.engineSurgeDamage[i]);
                    if (World.Rnd().nextFloat() < 0.5F && (super.FM instanceof RealFlightModel) && ((RealFlightModel) super.FM).isRealMode()) {
                        if (this.FM.actor == World.getPlayerAircraft())
                            HUD.log("Engine Flameout!");
                        this.FM.EI.engines[i].setEngineStops(this);
                    } else {
                        if (this.FM.actor == World.getPlayerAircraft())
                            HUD.log("Compressor Stall!");
                        this.FM.EI.engines[i].setKillCompressor(this);
                    }
                }
            }
        }
        if (this.lastThrottleCheck == -1L || Time.current() - this.lastThrottleCheck > TIME_PER_5_PERCENT) {
            for (int i = 0; i < 2; i++)
                this.oldthrl[i] = this.curthrl[i];
            this.lastThrottleCheck = Time.current();
        }
    }

    private void getSquares() {
        this.squareWing = this.FM.Sq.squareWing;
        this.squareAilerons = this.FM.Sq.squareAilerons;
        this.squareElevators = this.FM.Sq.squareElevators;
        this.squareRudders = this.FM.Sq.squareRudders;
        this.squareFlaps = this.FM.Sq.squareFlaps;
        this.liftWingLIn = this.FM.Sq.liftWingLIn;
        this.liftWingLMid = this.FM.Sq.liftWingLMid;
        this.liftWingLOut = this.FM.Sq.liftWingLOut;
        this.liftWingRIn = this.FM.Sq.liftWingRIn;
        this.liftWingRMid = this.FM.Sq.liftWingRMid;
        this.liftWingROut = this.FM.Sq.liftWingROut;
        this.liftStab = this.FM.Sq.liftStab;
        this.liftKeel = this.FM.Sq.liftKeel;
    }

    private void setSquaresStd() {
        this.FM.Sq.squareWing = this.squareWing;
        this.FM.Sq.squareAilerons = this.squareAilerons;
        this.FM.Sq.squareElevators = this.squareElevators;
        this.FM.Sq.squareRudders = this.squareRudders;
        this.FM.Sq.squareFlaps = this.squareFlaps;
        this.FM.Sq.liftWingLIn = this.liftWingLIn;
        this.FM.Sq.liftWingLMid = this.liftWingLMid;
        this.FM.Sq.liftWingLOut = this.liftWingLOut;
        this.FM.Sq.liftWingRIn = this.liftWingRIn;
        this.FM.Sq.liftWingRMid = this.liftWingRMid;
        this.FM.Sq.liftWingROut = this.liftWingROut;
        this.FM.Sq.liftStab = this.liftStab;
        this.FM.Sq.liftKeel = this.liftKeel;
    }

    private void setSquaresStdSmoooth() {
        if (Time.current() > this.startModToStd + TIME_MOD_TO_STD) {
            this.startModToStd = -1L;
            this.setSquaresStd();
            return;
        }
        float fStepCur = (float)(Time.current() - this.startModToStd);
        float squareWingSmooth = smoothCvt(fStepCur, 0.0F, (float)TIME_MOD_TO_STD, MOD_MULTIPLIER_SQUARE_WING, 1.0F);
        float squareAileronsSmooth = smoothCvt(fStepCur, 0.0F, (float)TIME_MOD_TO_STD, MOD_MULTIPLIER_SQUARE_AILERONS, 1.0F);
        float squareElevatorsSmooth = smoothCvt(fStepCur, 0.0F, (float)TIME_MOD_TO_STD, MOD_MULTIPLIER_SQUARE_ELEVATORS, 1.0F);
        float squareRuddersSmooth = smoothCvt(fStepCur, 0.0F, (float)TIME_MOD_TO_STD, MOD_MULTIPLIER_SQUARE_RUDDERS, 1.0F);
        float squareFlapsSmooth = smoothCvt(fStepCur, 0.0F, (float)TIME_MOD_TO_STD, MOD_MULTIPLIER_SQUARE_FLAPS, 1.0F);
        float liftWingSmooth = smoothCvt(fStepCur, 0.0F, (float)TIME_MOD_TO_STD, MOD_MULTIPLIER_LIFT_WING, 1.0F);
        float liftStabSmooth = smoothCvt(fStepCur, 0.0F, (float)TIME_MOD_TO_STD, MOD_MULTIPLIER_LIFT_STAB, 1.0F);
        float liftKeelSmooth = smoothCvt(fStepCur, 0.0F, (float)TIME_MOD_TO_STD, MOD_MULTIPLIER_LIFT_KEEL, 1.0F);
        this.FM.Sq.squareWing = this.squareWing * squareWingSmooth;
        this.FM.Sq.squareAilerons = this.squareAilerons * squareAileronsSmooth;
        this.FM.Sq.squareElevators = this.squareElevators * squareElevatorsSmooth;
        this.FM.Sq.squareRudders = this.squareRudders * squareRuddersSmooth;
        this.FM.Sq.squareFlaps = this.squareFlaps * squareFlapsSmooth;
        this.FM.Sq.liftWingLIn = this.liftWingLIn * liftWingSmooth;
        this.FM.Sq.liftWingLMid = this.liftWingLMid * liftWingSmooth;
        this.FM.Sq.liftWingLOut = this.liftWingLOut * liftWingSmooth;
        this.FM.Sq.liftWingRIn = this.liftWingRIn * liftWingSmooth;
        this.FM.Sq.liftWingRMid = this.liftWingRMid * liftWingSmooth;
        this.FM.Sq.liftWingROut = this.liftWingROut * liftWingSmooth;
        this.FM.Sq.liftStab = this.liftStab * liftStabSmooth;
        this.FM.Sq.liftKeel = this.liftKeel * liftKeelSmooth;
    }
    
    private void setSquaresMod() {
        this.FM.Sq.squareWing = this.squareWing * MOD_MULTIPLIER_SQUARE_WING;
        this.FM.Sq.squareAilerons = this.squareAilerons * MOD_MULTIPLIER_SQUARE_AILERONS;
        this.FM.Sq.squareElevators = this.squareElevators * MOD_MULTIPLIER_SQUARE_ELEVATORS;
        this.FM.Sq.squareRudders = this.squareRudders * MOD_MULTIPLIER_SQUARE_RUDDERS;
        this.FM.Sq.squareFlaps = this.squareFlaps * MOD_MULTIPLIER_SQUARE_FLAPS;
        this.FM.Sq.liftWingLIn = this.liftWingLIn * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingLMid = this.liftWingLMid * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingLOut = this.liftWingLOut * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingRIn = this.liftWingRIn * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingRMid = this.liftWingRMid * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingROut = this.liftWingROut * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftStab = this.liftStab * MOD_MULTIPLIER_LIFT_STAB;
        this.FM.Sq.liftKeel = this.liftKeel * MOD_MULTIPLIER_LIFT_KEEL;
    }
    
    private static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + (outMax - outMin) * (-0.5F * (float) Math.cos((inputValue - inMin) / (inMax - inMin) * Math.PI) + 0.5F);
    }

    private float                oldthrl[]                       = { -1F, -1F };
    private float                curthrl[]                       = { -1F, -1F };
    private float                engineSurgeDamage[]             = { 0.0F, 0.0F };
    private boolean              isOnGround                      = false;
    private float                squareWing;
    private float                squareAilerons;
    private float                squareElevators;
    private float                squareRudders;
    private float                squareFlaps;
    private float                liftWingLIn;
    private float                liftWingLMid;
    private float                liftWingLOut;
    private float                liftWingRIn;
    private float                liftWingRMid;
    private float                liftWingROut;
    private float                liftStab;
    private float                liftKeel;
    private long                 lastThrottleCheck               = -1L;
    private long                 startModToStd                   = -1L;
    private static final long    TIME_PER_5_PERCENT              = 1000L;
    private static final float   RPM_CRIT                        = 2850F;         // was 3200F before
    private static final boolean DEBUG                           = false;
    private static final float   MOD_MULTIPLIER_SQUARE_WING      = 1.0F;
    private static final float   MOD_MULTIPLIER_SQUARE_AILERONS  = 0.0F;
    private static final float   MOD_MULTIPLIER_SQUARE_ELEVATORS = 0.0F;
    private static final float   MOD_MULTIPLIER_SQUARE_RUDDERS   = 1.0F;
    private static final float   MOD_MULTIPLIER_SQUARE_FLAPS     = 0.0F;
    private static final float   MOD_MULTIPLIER_LIFT_WING        = 0.5F;
    private static final float   MOD_MULTIPLIER_LIFT_STAB        = -1.0F;
    private static final float   MOD_MULTIPLIER_LIFT_KEEL        = -1.0F;
    private static final long    TIME_MOD_TO_STD                 = 1500L;

    static {
        Class class1 = ME_262V3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me 262");
        Property.set(class1, "meshName", "3DO/Plane/Me-262V-3/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1943.1F);
        Property.set(class1, "yearExpired", 1943.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262V3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262.class });
        Property.set(class1, "LOSElevation", 0.74615F);
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
        Aircraft.weaponsRegister(class1, "default", new String[] { null, null });
        Aircraft.weaponsRegister(class1, "none", new String[] { null, null });
    }
}
