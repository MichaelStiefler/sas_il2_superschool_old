package com.maddox.il2.objects.air;

import java.io.IOException;
import java.security.SecureRandom;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class A_37 extends Scheme2 implements TypeFighter, TypeBNZFighter, TypeFighterAceMaker{

	/**
	 * @author SAS~Skylla
	 * @see A_37A, CockpitA_37 
	**/
	
    private float oldthrl;
    private float curthrl;
    private float engineSurgeDamage;
    private boolean overrideBailout;
    private boolean ejectComplete;
    public static boolean bChangedPit = false;
    private float arrestor2;
    public float AirBrakeControl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    
    private float [] rndgear = {0.0F, 0.0F, 0.0F};
    private static float [] rndgearnull = {0.0F, 0.0F, 0.0F};

    static  {
        Class class1 = A_37.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
	
    public A_37() {
        oldthrl = -1F;
        curthrl = -1F;
        arrestor2 = 0.0F;
        AirBrakeControl = 0.0F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        overrideBailout = false;
        ejectComplete = false;
        SecureRandom secRandom = new SecureRandom();
	    secRandom.setSeed(System.currentTimeMillis());
	    RangeRandom rr = new RangeRandom(secRandom.nextLong());
	    for (int i=0; i<rndgear.length; i++) {
	        rndgear[i] = rr.nextFloat(0.0F, 0.25F);
	    }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if(this.FM.Gears.nearGround() || this.FM.Gears.onGround() && this.FM.CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
            if(this.FM.AP.way.isLanding() && (double)super.FM.getSpeed() > (double)(this.FM.VmaxFLAPS * 2D)) {
                FM.CT.AirBrakeControl = 1.0F;
            } else if(FM.AP.way.isLanding() && (double)super.FM.getSpeed() < (double)(FM.VmaxFLAPS * 1.5D)) {
                FM.CT.AirBrakeControl = 0.0F;
            }
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    	
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    	
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void doMurderPilot(int i) {
        switch(i) {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }
    
 // FIXME --------------------------------------------------------------------------------------------------------------------
    
    protected void moveAirBrake(float f) {
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, 0.0F, 60F * f);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, 0.0F, 60F * f);
    }

    public void moveCockpitDoor(float f) {
        myResetYPRmodifier();   
        Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 35.0F);
        //Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 1.0F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER()) {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }
    
    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -70F), 0.0F);
    }
    
 // Wing Code: --------------------------------------------------------------------------------------------------------------------

    public void moveWingFold(float f) {
        if(f < 0.001F) {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else {
            setGunPodsOn(false);
            FM.CT.WeaponControl[0] = false;
            hideWingWeapons(true);
        }
        moveWingFold(this.hierMesh(), f);
    }
    
    public void moveArrestorHook(float f) {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 12.2F * f, 0.0F);
        hierMesh().chunkSetAngles("Hook2_D0", 0.0F, 0.0F, 0.0F);
    }

  // Gear code  ----------------------------------------------------------------------------------------------------------------------
    
    public static void moveGear(HierMesh hiermesh, float l, float r, float c, float [] rnd) {
    	//hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F); Rad
    	myResetYPRmodifier();
    	Aircraft.ypr[1] = Aircraft.cvt(c, 0.18F+rnd[0], 0.74F+rnd[0], 0.0F, -106F);
    	Aircraft.xyz[0] = Aircraft.cvt(c, 0.18F+rnd[0], 0.74F+rnd[0], 0.5F, 0.0F);
    	hiermesh.chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
        //hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(c, 0.18F+rnd[0], 0.74F+rnd[0], 0.0F, -100F), 0.0F);         
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, Aircraft.cvt(c, 0.01F+rnd[0], 0.26F+rnd[0], 0.0F, 80F));
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, Aircraft.cvt(c, 0.01F+rnd[0], 0.26F+rnd[0], 0.0F, -80F));
        
        myResetYPRmodifier();
        Aircraft.ypr[1] = Aircraft.cvt(l, 0.15F+rnd[1], 0.70F+rnd[1], 0.0F, 90F);
        Aircraft.xyz[0] = Aircraft.cvt(l, 0.15F+rnd[1], 0.70F+rnd[1], -0.22F, 0.0F);
        hiermesh.chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(l, 0.15F+rnd[1], 0.70F+rnd[1], 0.0F, 95F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(l, 0.01F+rnd[1], 0.29F+rnd[1], 0.0F, -93F), 0.0F);
        
        myResetYPRmodifier();
        Aircraft.ypr[1] = Aircraft.cvt(r, 0.15F+rnd[2], 0.70F+rnd[2], 0.0F, -90F);
        Aircraft.xyz[0] = Aircraft.cvt(r, 0.15F+rnd[2], 0.70F+rnd[2], 0.22F, 0.0F);
        Aircraft.xyz[2] = Aircraft.cvt(r, 0.15F+rnd[2], 0.20F+rnd[2], 0.01F, -0.02F);
        hiermesh.chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(r, 0.15F+rnd[2], 0.70F+rnd[2], 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(r, 0.01F+rnd[2], 0.29F+rnd[2], 0.0F, 93.0F), 0.0F);
    }

	private static void myResetYPRmodifier() {
	    Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
	}
    
    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos) {
        moveGear(hiermesh, leftGearPos, rightGearPos, tailWheelPos, rndgearnull);
    }
    
	public static void moveGear(final HierMesh h, final float n, float [] rnd) {
		moveGear(h,n,n,n,rnd);
    }
    
    public static void moveGear(final HierMesh h, final float n) {
    	moveGear(h,n,rndgearnull);
    }
    
    protected void moveGear(float l, float r, float t) {
        moveGear(this.hierMesh(), l, r, t, rndgear);
    }
    
    protected void moveGear(final float n) {
        moveGear(this.hierMesh(), n, rndgear);
    }

    public void moveWheelSink() {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }
    
  //--------------------------------------------------------------------------------------------------------------------------------
    
    protected void moveRudder(float f) {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f) {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * f);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveAileron(float f) {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveFlap(float f) {
        float f1 = 55F * f;
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, 0.0F, f1);
    }

    protected void moveFan(float f) {
    	
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if(s.startsWith("xx")) {
            if(s.startsWith("xxarmor")) {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1")) {
                    getEnergyPastArmor(13.35 / Math.abs(A_37.v1.x), shot);
                    if(shot.power <= 0.0F) {
                        doRicochetBack(shot);
                    }
                } else if(s.endsWith("p2")) {
                    getEnergyPastArmor(8.77F, shot);
                } else if(s.endsWith("g1")) {
                    getEnergyPastArmor(World.Rnd().nextFloat(40F, 60F) / Math.abs(A_37.v1.x), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F) {
                        doRicochetBack(shot);
                    }
                }
            } else if(s.startsWith("xxcontrols")) {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i) {
                case 1:
                case 2: 
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F) {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;
                    
                case 3:
                case 4:
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else if(s.startsWith("xxeng1")) {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc")) {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / Math.abs(A_37.v1.x), shot);
                }
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F) {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if(!s.endsWith("exht"));
            } else if(s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    if(this.FM.AS.astateTankStates[j] == 0) {
                        debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F) {
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                        debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
            } else if(s.startsWith("xxspar")) {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            } else if(s.startsWith("xxhyd")) {
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            } else if(s.startsWith("xxpnm")) {
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            }
        } else {
            if(s.startsWith("xcockpit")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xxhispa1") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                this.FM.AS.setJamBullets(1, 0);
            }
            if(s.startsWith("xxhispa2") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                this.FM.AS.setJamBullets(1, 1);
            }
            if(s.startsWith("xxhispa3") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                this.FM.AS.setJamBullets(1, 2);
            }
            if(s.startsWith("xxhispa4") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                this.FM.AS.setJamBullets(1, 3);
            }
            if(s.startsWith("xcf")) {
                hitChunk("CF", shot);
            } else if(s.startsWith("xnose")) {
                hitChunk("Nose", shot);
            } else if(s.startsWith("xtail")) {
                if(chunkDamageVisible("Tail1") < 3) {
                    hitChunk("Tail1", shot);
                }
            } else if(s.startsWith("xkeel")) {
                if(chunkDamageVisible("Keel1") < 2) {
                    hitChunk("Keel1", shot);
                }
            } else if(s.startsWith("xrudder")) {
                hitChunk("Rudder1", shot);
            } else if(s.startsWith("xstab")) {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2) {
                    hitChunk("StabL", shot);
                }
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1) {
                    hitChunk("StabR", shot);
                }
            } else if(s.startsWith("xvator")) {
                if(s.startsWith("xvatorl")) {
                    hitChunk("VatorL", shot);
                }
                if(s.startsWith("xvatorr")) {
                    hitChunk("VatorR", shot);
                }
            } else if(s.startsWith("xwing")) {
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
            } else if(s.startsWith("xarone")) {
                if(s.startsWith("xaronel"))
                    hitChunk("AroneL", shot);
                if(s.startsWith("xaroner"))
                    hitChunk("AroneR", shot);
            } else if(s.startsWith("xgear")) {
                if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                    debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
                    debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else if(s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte0 = 0;
                int k;
                if(s.endsWith("a")) {
                    byte0 = 1;
                    k = s.charAt(6) - 49;
                } else if(s.endsWith("b")) {
                    byte0 = 2;
                    k = s.charAt(6) - 49;
                } else {
                    k = s.charAt(5) - 49;
                }
                hitFlesh(k, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch(i) {
        case 19: // '\023'
            this.FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void engineSurge(float f) {
        if(this.FM.AS.isMaster())
            if(curthrl == -1F) {
                curthrl = oldthrl = this.FM.EI.engines[0].getControlThrottle();
            } else {
                curthrl = this.FM.EI.engines[0].getControlThrottle();
                if(curthrl < 1.05F) {
                    if((curthrl - oldthrl) / f > 20F && this.FM.EI.engines[0].getRPM() < 3200F && this.FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F) {
                        if(this.FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.01D * (double)(this.FM.EI.engines[0].getRPM() / 1000F);
                        this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            this.FM.AS.hitEngine(this, 0, 100);
                        if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            this.FM.EI.engines[0].setEngineDies(this);
                    }
                    if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && this.FM.EI.engines[0].getRPM() < 3200F && this.FM.EI.engines[0].getStage() == 6) {
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.001D * (double)(this.FM.EI.engines[0].getRPM() / 1000F);
                        this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode()) {
                            if(this.FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                            this.FM.EI.engines[0].setEngineStops(this);
                        } else if(this.FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                    }
                }
                oldthrl = curthrl;
            }
    }

    public void update(float f) {
        if((this.FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F) {
            overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            bailout();
        }
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
            if(this.FM.EI.engines[0].getPowerOutput() > 0.5F && this.FM.EI.engines[0].getStage() == 6) {
                if(this.FM.EI.engines[0].getPowerOutput() > 0.75F)
                    this.FM.AS.setSootState(this, 0, 3);
                else
                    this.FM.AS.setSootState(this, 0, 2);
            } else {
                this.FM.AS.setSootState(this, 0, 0);
            }
        if(this.FM.CT.getArrestor() > 0.9F)
            if(this.FM.Gears.arrestorVAngle != 0.0F) {
                arrestor2 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -65F, 3F, 45F, -23F);
                hierMesh().chunkSetAngles("Hook2_D0", 0.0F, arrestor2, 0.0F);
                if(this.FM.Gears.arrestorVAngle < -35F);
            } else {
                float f1 = -41F * this.FM.Gears.arrestorVSink;
                if(f1 < 0.0F && super.FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if(f1 > 0.0F && this.FM.CT.getArrestor() < 0.9F)
                    f1 = 0.0F;
                if(f1 > 6.2F)
                    f1 = 6.2F;
                arrestor2 += f1;
                if(arrestor2 < -23F)
                    arrestor2 = -23F;
                else
                if(arrestor2 > 45F)
                    arrestor2 = 45F;
                hierMesh().chunkSetAngles("Hook2_D0", 0.0F, arrestor2, 0.0F);
            }
        engineSurge(f);
        super.update(f);
    }

    private void bailout() {
        if(overrideBailout)
            if(this.FM.AS.astateBailoutStep >= 0 && this.FM.AS.astateBailoutStep < 2) {
                if(this.FM.CT.cockpitDoorControl > 0.5F && this.FM.CT.getCockpitDoor() > 0.5F) {
                    this.FM.AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else
            if(this.FM.AS.astateBailoutStep >= 2 && this.FM.AS.astateBailoutStep <= 3) {
                switch(this.FM.AS.astateBailoutStep) {
                case 2:
                    if(this.FM.CT.cockpitDoorControl < 0.5F) {
                    
                    }
                    break;

                case 3: 
                    doRemoveBlisters();
                    break;
                }
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate = this.FM.AS;
                aircraftstate.astateBailoutStep = (byte)(aircraftstate.astateBailoutStep + 1);
                if(this.FM.AS.astateBailoutStep == 4)
                    this.FM.AS.astateBailoutStep = 11;
            } else if(this.FM.AS.astateBailoutStep >= 11 && this.FM.AS.astateBailoutStep <= 19) {
                byte byte0 = this.FM.AS.astateBailoutStep;
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate1 = this.FM.AS;
                aircraftstate1.astateBailoutStep = (byte)(aircraftstate1.astateBailoutStep + 1);
                if(byte0 == 11) {
                    super.FM.setTakenMortalDamage(true, null);
                    if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44) {
                        World.cur();
                        if(this.FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)super.FM).set_maneuver(44);
                    }
                }
                if(this.FM.AS.astatePilotStates[byte0 - 11] < 99) {
                    doRemoveBodyFromPlane(byte0 - 10);
                    if(byte0 == 11) {
                        doEjectCatapult();
                        super.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                        if(byte0 > 10 && byte0 <= 19)
                            EventLog.onBailedOut(this, byte0 - 11);
                    }
                }
            }
    }


    public void doEjectCatapult() {
        new MsgAction(false, this) {
            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if(Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
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

        };
        hierMesh().chunkVisible("Seat_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    private final void doRemoveBlisters() {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && this.FM.AS.getPilotHealth(i - 1) > 0.0F) {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
        
    }
}