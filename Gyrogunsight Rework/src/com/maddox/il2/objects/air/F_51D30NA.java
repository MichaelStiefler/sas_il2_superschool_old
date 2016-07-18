// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;
import com.maddox.rts.NetMsgInput;
import java.io.IOException;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.engine.Config;

public class F_51D30NA extends P_51Mustang implements TypeFighterAceMaker
{
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public boolean bHasBlister;
    private float fMaxKMHSpeedForOpenCanopy;
    static /* synthetic */ Class class$com$maddox$il2$objects$air$F_51D30NA;
    static /* synthetic */ Class class$com$maddox$il2$objects$air$CockpitF_51D30K14;
    
    public F_51D30NA() {
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200.0f;
        this.bHasBlister = true;
        this.fMaxKMHSpeedForOpenCanopy = 150.0f;
    }
    
    public void moveCockpitDoor(final float n) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(n, 0.01f, 0.99f, 0.0f, 0.55f);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        final float n2 = (float)Math.sin(Aircraft.cvt(n, 0.01f, 0.99f, 0.0f, 3.141593f));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0f, 0.0f, 9.0f * n2);
        this.hierMesh().chunkSetAngles("Head1_D0", 12.0f * n2, 0.0f, 0.0f);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) {
                Main3D.cur3D().cockpits[0].onDoorMoved(n);
            }
            this.setDoorSnd(n);
        }
    }
    
    public void update(final float n) {
        super.update(n);
        if (this.FM.CT.getCockpitDoor() > 0.2 && this.bHasBlister && this.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy && this.hierMesh().chunkFindCheck("Blister1_D0") != -1) {
            try {
                if (this == World.getPlayerAircraft()) {
                    ((CockpitF_51D30K14)Main3D.cur3D().cockpitCur).removeCanopy();
                }
            }
            catch (Exception ex) {}
            this.hierMesh().hideSubTrees("Blister1_D0");
            final Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            final Vector3d speed = new Vector3d();
            speed.set(this.FM.Vwld);
            wreckage.setSpeed(speed);
            this.bHasBlister = false;
            this.FM.CT.bHasCockpitDoorControl = false;
            this.FM.setGCenter(-0.3f);
        }
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
    	if(this.k14Distance > 730.0f) {
    		this.k14Distance = 730.0f;
    	} else if(this.k14Distance < 180.0f) {
    		this.k14Distance = 180.0f;
    	}
    	HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Distance: " + (int)(this.k14Distance) + "m" );
    }
    
    /* old code:
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
  //-------------------------------------------------------------------------------------------------------
    
    public void typeFighterAceMakerAdjSideslipReset() {
    }
    
    public void typeFighterAceMakerAdjSideslipPlus() {
        --this.k14WingspanType;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }
    
    public void typeFighterAceMakerAdjSideslipMinus() {
        ++this.k14WingspanType;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
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
        final Class var_class = (F_51D30NA.class$com$maddox$il2$objects$air$F_51D30NA == null) ? (F_51D30NA.class$com$maddox$il2$objects$air$F_51D30NA = class$("com.maddox.il2.objects.air.F_51D30NA")) : F_51D30NA.class$com$maddox$il2$objects$air$F_51D30NA;
        new SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "F-51");
        Property.set(var_class, "meshName", "3DO/Plane/F-51D-30NA(Multi1)/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(var_class, "meshName_us", "3DO/Plane/F-51D-30NA(USA)/hier.him");
        Property.set(var_class, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(var_class, "noseart", 1);
        Property.set(var_class, "yearService", 1947.0f);
        Property.set(var_class, "yearExpired", 1957.5f);
        Property.set(var_class, "FlightModel", "FlightModels/P-51D-30.fmd");
        Property.set(var_class, "cockpitClass", (Object)new Class[] { (F_51D30NA.class$com$maddox$il2$objects$air$CockpitF_51D30K14 == null) ? (F_51D30NA.class$com$maddox$il2$objects$air$CockpitF_51D30K14 = class$("com.maddox.il2.objects.air.CockpitF_51D30K14")) : F_51D30NA.class$com$maddox$il2$objects$air$CockpitF_51D30K14 });
        Property.set(var_class, "LOSElevation", 1.1188f);
        Aircraft.weaponTriggersRegister(var_class, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16" });
        Aircraft.weaponsRegister(var_class, "default", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x250", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x500", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x1000", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun1000lbs 1", "BombGun1000lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x75Nap", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun75GalNapalm 1", "BombGun75GalNapalm 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x110Nap", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun110GalNapalm 1", "BombGun110GalNapalm 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x250_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x500_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x75Nap_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun75GalNapalm 1", "BombGun75GalNapalm 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x110Nap_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun110GalNapalm 1", "BombGun110GalNapalm 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x75Tank_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank75gal2New 1", "FuelTankGun_Tank75gal2New 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x108Tank_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank108gal2 1", "FuelTankGun_Tank108gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x110Tank_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank110gal2 1", "FuelTankGun_Tank110gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x165Tank_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank165gal2 1", "FuelTankGun_Tank165gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_10xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x75Tank", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank75gal2New 1", "FuelTankGun_Tank75gal2New 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x108Tank", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank108gal2 1", "FuelTankGun_Tank108gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x110Tank", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank110gal2 1", "FuelTankGun_Tank110gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "6xM2_APIT_2x165Tank", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank165gal2 1", "FuelTankGun_Tank165gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x250", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x500", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x75Nap", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun75GalNapalm 1", "BombGun75GalNapalm 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x110Nap", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun110GalNapalm 1", "BombGun110GalNapalm 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x250_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x500_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x75Nap_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun75GalNapalm 1", "BombGun75GalNapalm 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x110Nap_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun110GalNapalm 1", "BombGun110GalNapalm 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x75Tank_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank75gal2New 1", "FuelTankGun_Tank75gal2New 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x108Tank_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank108gal2 1", "FuelTankGun_Tank108gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x110Tank_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank110gal2 1", "FuelTankGun_Tank110gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x165Tank_6xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank165gal2 1", "FuelTankGun_Tank165gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", null, null, "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_10xHVAR", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", null, null, "Pylon51Late 1", "Pylon51Late 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "PylonP51RAIL 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x75Tank", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank75gal2New 1", "FuelTankGun_Tank75gal2New 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x108Tank", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank108gal2 1", "FuelTankGun_Tank108gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x110Tank", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank110gal2 1", "FuelTankGun_Tank110gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "4xM2_APIT_2x165Tank", new String[] { "MGunBrowning50kAPIT 400", "MGunBrowning50kAPIT 400", null, null, "MGunBrowning50kAPIT 270", "MGunBrowning50kAPIT 270", "FuelTankGun_Tank165gal2 1", "FuelTankGun_Tank165gal2 1", "Pylon51Late 1", "Pylon51Late 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        Aircraft.weaponsRegister(var_class, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
