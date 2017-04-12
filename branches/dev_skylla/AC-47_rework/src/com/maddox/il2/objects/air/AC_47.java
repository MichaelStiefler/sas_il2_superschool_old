// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.HierMesh;
import com.maddox.sas1946.il2.util.AircraftTools;

public class AC_47 extends Scheme2 implements TypeTransport
{
    static /* synthetic */ Class class$com$maddox$il2$objects$air$AC_47;
    static /* synthetic */ Class class$com$maddox$il2$objects$air$CockpitAC47;
    
    public static void moveGear(final HierMesh hierMesh, final float n) {
        hierMesh.chunkSetAngles("GearL2_D0", 0.0f, -45.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR2_D0", 0.0f, -45.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearL3_D0", 0.0f, 20.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR3_D0", 0.0f, 20.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearL4_D0", 0.0f, -120.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR4_D0", 0.0f, -120.0f * n, 0.0f);
    }
    
    protected void moveGear(final float n) {
        moveGear(this.hierMesh(), n);
    }
    
    public void msgShot(final Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLOut") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f && Math.abs(Aircraft.Pd.y) < 6.0) {
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("WingROut") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f && Math.abs(Aircraft.Pd.y) < 6.0) {
            this.FM.AS.hitTank(shot.initiator, 3, 1);
        }
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f && Math.abs(Aircraft.Pd.y) < 1.940000057220459) {
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f && Math.abs(Aircraft.Pd.y) < 1.940000057220459) {
            this.FM.AS.hitTank(shot.initiator, 2, 1);
        }
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0f, 1.0f) < 0.1f) {
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("Nose") && Aircraft.Pd.x > 4.900000095367432 && Aircraft.Pd.z > -0.09000000357627869 && World.Rnd().nextFloat() < 0.1f) {
            if (Aircraft.Pd.y > 0.0) {
                this.killPilot(shot.initiator, 0);
                this.FM.setCapableOfBMP(false, shot.initiator);
            }
            else {
                this.killPilot(shot.initiator, 1);
            }
        }
        if (this.FM.AS.astateEngineStates[0] > 2 && this.FM.AS.astateEngineStates[1] > 2 && World.Rnd().nextInt(0, 99) < 33) {
            this.FM.setCapableOfBMP(false, shot.initiator);
        }
        super.msgShot(shot);
    }
    
    public void doMurderPilot(final int n) {
        switch (n) {
            case 0: {
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
            }
            case 1: {
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
            }
        }
    }
    
    public void rareAction(final float f, final boolean bool) {
        super.rareAction(f, bool);
        for (int i = 1; i < 3; ++i) {
            if (this.FM.getAltitude() < 3000.0f) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            }
            else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }
    }
    
    protected boolean cutFM(final int n, final int n2, final Actor actor) {
        switch (n) {
            case 13: {
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                break;
            }
            case 35: {
                if (World.Rnd().nextFloat() < 0.25f) {
                    this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(2, 6));
                    break;
                }
                break;
            }
            case 38: {
                if (World.Rnd().nextFloat() < 0.25f) {
                    this.FM.AS.hitTank(this, 2, World.Rnd().nextInt(2, 6));
                    break;
                }
                break;
            }
        }
        return super.cutFM(n, n2, actor);
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
        final Class clazz = (AC_47.class$com$maddox$il2$objects$air$AC_47 == null) ? (AC_47.class$com$maddox$il2$objects$air$AC_47 = class$("com.maddox.il2.objects.air.AC_47")) : AC_47.class$com$maddox$il2$objects$air$AC_47;
        new SPAWN(clazz);
        Property.set(clazz, "iconFar_shortClassName", "Douglas");
        Property.set(clazz, "meshNameDemo", "3DO/Plane/AC-47/hier.him");
        Property.set(clazz, "meshName", "3DO/Plane/AC-47/hier.him");
        Property.set(clazz, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(clazz, "noseart", 1);
        Property.set(clazz, "yearService", 1939.0f);
        Property.set(clazz, "yearExpired", 2999.9f);
        Property.set(clazz, "FlightModel", "FlightModels/C-47B.fmd");
        Property.set(clazz, "cockpitClass", (Object)new Class[] { (AC_47.class$com$maddox$il2$objects$air$CockpitAC47 == null) ? (AC_47.class$com$maddox$il2$objects$air$CockpitAC47 = class$("com.maddox.il2.objects.air.CockpitAC47")) : AC_47.class$com$maddox$il2$objects$air$CockpitAC47 });
        Property.set(clazz, "LOSElevation", 0.725f);
        Aircraft.weaponTriggersRegister(clazz, new int[] { 3, 0, 1, 1 });
        Aircraft.weaponHooksRegister(clazz, new String[] { "_BombSpawn01", "_MGUN01", "_MGUN02", "_MGUN03" });
        //AircraftTools.weaponsRegister(clazz, "default", new String[] { "BombGunFlareLight 45", "MGunMiniGun6000 8000", "MGunMiniGun6000 8000", "MGunMiniGun6000 8000" });
        //AircraftTools.weaponsRegister(clazz, "3000rpm", new String[] { "BombGunFlareLight 45", "MGunMiniGun3000 8000", "MGunMiniGun3000 8000", "MGunMiniGun3000 8000" });
        //AircraftTools.weaponsRegister(clazz, "none", new String[] { null, null, null, null });
    }
}
