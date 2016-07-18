// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgInput;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.rts.Time;
import com.maddox.il2.ai.World;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.engine.HierMesh;

public class TA_152C1 extends FW_190 implements TypeFighterAceMaker
{
    public boolean bToFire;
    private long tX4Prev;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private float kangle;
    private float deltaAzimuth;
    private float deltaTangage;
    static /* synthetic */ Class class$com$maddox$il2$objects$air$CockpitTA_152C1;
    
    public TA_152C1() {
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200.0f;
        this.bToFire = false;
        this.kangle = 0.0f;
    }
    
    public static void moveGear(final HierMesh hierMesh, final float n) {
        hierMesh.chunkSetAngles("GearL2_D0", 0.0f, -77.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR2_D0", 0.0f, -77.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearL3_D0", 0.0f, -102.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR3_D0", 0.0f, -102.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearC2_D0", 20.0f * n, 0.0f, 0.0f);
        hierMesh.chunkSetAngles("GearC3_D0", 0.0f, 0.0f, 0.0f);
        final float max = Math.max(-n * 1500.0f, -94.0f);
        hierMesh.chunkSetAngles("GearL5_D0", 0.0f, max, 0.0f);
        hierMesh.chunkSetAngles("GearR5_D0", 0.0f, max, 0.0f);
    }
    
    protected void moveGear(final float n) {
        moveGear(this.hierMesh(), n);
    }
    
    public void moveSteering(final float n) {
        if (super.FM.CT.getGear() < 0.98f) {
            return;
        }
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0f, -n, 0.0f);
    }
    
    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.Gears.gWheelSinking[0], 0.0f, 0.44f, 0.0f, 0.44f);
        this.hierMesh().chunkSetLocate("GearL2a_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.Gears.gWheelSinking[1], 0.0f, 0.44f, 0.0f, 0.44f);
        this.hierMesh().chunkSetLocate("GearR2a_D0", Aircraft.xyz, Aircraft.ypr);
    }
    
    protected void moveFlap(final float n) {
        final float n2 = -50.0f * n;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0f, n2, 0.0f);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0f, -n2, 0.0f);
    }
    
    public void rareAction(final float n, final boolean b) {
        super.rareAction(n, b);
        if ((super.FM instanceof RealFlightModel && ((RealFlightModel)super.FM).isRealMode()) || !b || !(super.FM instanceof Pilot)) {
            return;
        }
        final Pilot pilot = (Pilot)super.FM;
        if (pilot.get_maneuver() == 63 && pilot.target != null) {
            final Point3d point3d = new Point3d(pilot.target.Loc);
            point3d.sub(super.FM.Loc);
            super.FM.Or.transformInv(point3d);
            if (((point3d.x > 4000.0 && point3d.x < 5500.0) || (point3d.x > 100.0 && point3d.x < 5000.0 && World.Rnd().nextFloat() < 0.33f)) && Time.current() > this.tX4Prev + 10000L) {
                this.bToFire = true;
                this.tX4Prev = Time.current();
            }
        }
    }
    
    public void update(final float n) {
        for (int i = 1; i < 15; ++i) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0f, -10.0f * this.kangle, 0.0f);
        }
        this.kangle = 0.95f * this.kangle + 0.05f * super.FM.EI.engines[0].getControlRadiator();
        super.update(n);
    }
    
    public boolean typeFighterAceMakerToggleAutomation() {
        ++this.k14Mode;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }
    
    public void typeFighterAceMakerAdjDistanceReset() {
    }
    
  //-------------------------------------------------------------------------------------------------------
    //TODO: skylla: gyro-gunsight distance HUD log (for details please see P_51D25NA.class):
    
    public void typeFighterAceMakerAdjDistancePlus() {
    	adjustK14AceMakerDistance(+10.0f);
    }
    
    public void typeFighterAceMakerAdjDistanceMinus() {
    	adjustK14AceMakerDistance(-10.0f);
    }
    
    private void adjustK14AceMakerDistance(float f) {
    	this.k14Distance += f;
    	if(this.k14Distance > 1000.0f) {
    		this.k14Distance = 1000.0f;
    	} else if(this.k14Distance < 160.0f) {
    		this.k14Distance = 160.0f;
    	}
    	HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Distance: " + (int)(this.k14Distance) + "m" );
    }
    /*
    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 10.0f;
        if (this.k14Distance > 800.0f) {
            this.k14Distance = 800.0f;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }
    
    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 10.0f;
        if (this.k14Distance < 200.0f) {
            this.k14Distance = 200.0f;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }
    */
    
  //Allied plane wingspans (approximately):    
    public void typeFighterAceMakerAdjSideslipPlus() {
    	try {
 			adjustAceMakerSideSlip(1);
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
    }
    	
    public void typeFighterAceMakerAdjSideslipMinus() {
    	try {
 			adjustAceMakerSideSlip(-1);
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
    }
    
    private void adjustAceMakerSideSlip(int i) throws IOException {
    	if(!(i == 1 || i ==-1))
    		throw new IOException("Wrong input value! Only +1 and -1 allowed!");
    	this.k14WingspanType += i;
    	String s = "Wingspan Selected: ";
    	String s1 = "Yak-3/Yak-9/La-5/P-39/MiG-3";
    	String s2 = "B-24";
    	
    	switch(this.k14WingspanType) {
    	//case 0: s += s1; break; //like Bf-109 
    	case 1: s += "P-51/P-47/P-80/Spitfire/Typhoon/Hurricane"; break; //like Fw-190
    	case 2: s += "P-38"; break; //like Ju-87
    	case 3: s += "Mosquito/IL-2/Beaufighter"; break; //like Me-210
    	case 4: s += "A-20/Pe-2"; break; //like Do-217
    	case 5: s += "20m"; break; //like Ju-88
    	case 6: s += "B-25/A-26"; break; //like Ju-188
    	case 7: s += "DC-3"; break; //like Ju-52
    	case 8: s += "B-17/Halifax/Lancaster"; break; //like He-177
    	case 9: s += s2; break; //like Fw-200
    	case 10: adjustAceMakerSideSlip(-1); s += s2; break;
    	case -1: adjustAceMakerSideSlip(1); s += s1; break;
    	default: this.k14WingspanType = 0; s += s1; break;
    	}
    	
    	HUD.log(AircraftHotKeys.hudLogWeaponId, s);   	
    }
    
    /*old code:
    public void typeFighterAceMakerAdjSideslipPlus() {
        --this.k14WingspanType;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + this.k14WingspanType);
    }
    
    public void typeFighterAceMakerAdjSideslipMinus() {
        ++this.k14WingspanType;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + this.k14WingspanType);
    }
    */
  //-------------------------------------------------------------------------------------------------------
    public void typeFighterAceMakerAdjSideslipReset() {
    }
    
    public void typeFighterAceMakerReplicateToNet(final NetMsgGuaranted netMsgGuaranted) throws IOException {
        netMsgGuaranted.writeByte(this.k14Mode);
        netMsgGuaranted.writeByte(this.k14WingspanType);
        netMsgGuaranted.writeFloat(this.k14Distance);
    }
    
    public void typeFighterAceMakerReplicateFromNet(final NetMsgInput netMsgInput) throws IOException {
        this.k14Mode = netMsgInput.readByte();
        this.k14WingspanType = netMsgInput.readByte();
        this.k14Distance = netMsgInput.readFloat();
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
        final Class this1 = CLASS.THIS();
        new SPAWN(this1);
        Property.set(this1, "iconFar_shortClassName", "Ta.152");
        Property.set(this1, "meshName", "3DO/Plane/Ta-152C1/hier.him");
        Property.set(this1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(this1, "yearService", 1944.6f);
        Property.set(this1, "yearExpired", 1948.0f);
        Property.set(this1, "FlightModel", "FlightModels/Ta-152C1.fmd");
        Property.set(this1, "cockpitClass", (Object)new Class[] { (TA_152C1.class$com$maddox$il2$objects$air$CockpitTA_152C1 == null) ? (TA_152C1.class$com$maddox$il2$objects$air$CockpitTA_152C1 = class$("com.maddox.il2.objects.air.CockpitTA_152C1")) : TA_152C1.class$com$maddox$il2$objects$air$CockpitTA_152C1 });
        Property.set(this1, "LOSElevation", 0.755f);
        Aircraft.weaponTriggersRegister(this1, new int[] { 0, 1, 1, 1, 1, 9, 9, 2, 2, 2, 2, 3, 3 });
        Aircraft.weaponHooksRegister(this1, new String[] { "_CANNON01", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalBomb02", "_ExternalBomb03" });
        Aircraft.weaponsRegister(this1, "default", new String[] { "MGunMK108k 90", "MGunMG15120s 175", "MGunMG15120s 175", "MGunMG15120s 175", "MGunMG15120s 175", null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(this1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
