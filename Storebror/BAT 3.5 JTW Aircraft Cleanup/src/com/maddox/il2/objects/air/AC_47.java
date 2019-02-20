package com.maddox.il2.objects.air;

import java.io.IOException;
import java.security.SecureRandom;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.weapons.MGunAdjustableMiniGun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class AC_47 extends Scheme2
    implements TypeTransport, TypeBomber
{

    public AC_47()
    {
        APmode1 = false;
        APmode2 = false;
        gunsActive = 0;
        gunsRPM = 0;
        SecureRandom securerandom = new SecureRandom();
        securerandom.setSeed(System.currentTimeMillis());
        RangeRandom rangerandom = new RangeRandom(securerandom.nextLong());
        for(int i = 0; i < rndgear.length; i++)
            rndgear[i] = rangerandom.nextFloat(0.0F, 0.25F);

    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, float af[])
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.01F + af[0], 0.74F + af[0], 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.01F + af[1], 0.74F + af[1], 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(f, 0.01F + af[0], 0.74F + af[0], 0.0F, 20F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(f1, 0.01F + af[1], 0.74F + af[1], 0.0F, 20F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F + af[0], 0.74F + af[0], 0.0F, -120F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.01F + af[1], 0.74F + af[1], 0.0F, -120F), 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        moveGear(hiermesh, f, f1, f2, rndgearnull);
    }

    public static void moveGear(HierMesh hiermesh, float f, float af[])
    {
        moveGear(hiermesh, f, f, f, af);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, rndgearnull);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2, rndgear);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f, rndgear);
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("WingLOut") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 6D)
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("WingROut") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 6D)
            this.FM.AS.hitTank(shot.initiator, 3, 1);
        if(shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 1.9399999999999999D)
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 1.9399999999999999D)
            this.FM.AS.hitTank(shot.initiator, 2, 1);
        if(shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("Nose") && ((Tuple3d) (Aircraft.Pd)).x > 4.9000000000000004D && ((Tuple3d) (Aircraft.Pd)).z > -0.089999999999999997D && World.Rnd().nextFloat() < 0.1F)
            if(((Tuple3d) (Aircraft.Pd)).y > 0.0D)
            {
                killPilot(shot.initiator, 0);
                this.FM.setCapableOfBMP(false, shot.initiator);
            } else
            {
                killPilot(shot.initiator, 1);
            }
        if(this.FM.AS.astateEngineStates[0] > 2 && this.FM.AS.astateEngineStates[1] > 2 && World.Rnd().nextInt(0, 99) < 33)
            this.FM.setCapableOfBMP(false, shot.initiator);
        super.msgShot(shot);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

        replicateGunStateToNet();
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 13:
            killPilot(this, 0);
            killPilot(this, 1);
            break;

        case 35:
            if(World.Rnd().nextFloat() < 0.25F)
                this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(2, 6));
            break;

        case 38:
            if(World.Rnd().nextFloat() < 0.25F)
                this.FM.AS.hitTank(this, 2, World.Rnd().nextInt(2, 6));
            break;
        }
        return super.cutFM(i, j, actor);
    }

    private void setGunsRPM(int i)
    {
        float f = -1F;
        for(int j = 0; j < 3; j++)
        {
            BulletEmitter bulletemitter = getBulletEmitterByHookName("_MGUN0" + (j + 1));
            if(bulletemitter instanceof MGunAdjustableMiniGun)
            {
                MGunAdjustableMiniGun mgunadjustableminigun = (MGunAdjustableMiniGun)bulletemitter;
                switch(i)
                {
                case -1: 
                    f = mgunadjustableminigun.decRPM();
                    break;

                case 0:
                    mgunadjustableminigun.resRPM();
                    break;

                case 1:
                    f = mgunadjustableminigun.incRPM();
                    break;

                default:
                    return;
                }
            } else
            {
                return;
            }
        }

        if(f > 0.0F)
        {
            if(this == World.getPlayerAircraft())
                HUD.log("Current Rate of Fire: " + (int)f + "rpm");
            gunsRPM = (int)f;
        }
    }

    public void typeBomberAdjSpeedMinus()
    {
        setGunsRPM(-1);
        replicateGunStateToNet();
    }

    public void typeBomberAdjSpeedPlus()
    {
        setGunsRPM(1);
        replicateGunStateToNet();
    }

    public void typeBomberAdjSpeedReset()
    {
        setGunsRPM(0);
        replicateGunStateToNet();
    }

    private void setGunsActive(int i)
    {
        BulletEmitter abulletemitter[] = new BulletEmitter[3];
        int j = 0;
        for(int k = 0; k < abulletemitter.length; k++)
        {
            abulletemitter[k] = getBulletEmitterByHookName("_MGUN0" + (k + 1));
            if(abulletemitter[k] != null)
            {
                if(!abulletemitter[k].isPause())
                    j++;
            } else
            {
                return;
            }
        }

        if(j + i > 0 && j + i < 4)
            j += i;
        switch(j)
        {
        case 1:
            abulletemitter[0].setPause(false);
            abulletemitter[1].setPause(true);
            abulletemitter[2].setPause(true);
            break;

        case 2:
            abulletemitter[0].setPause(false);
            abulletemitter[1].setPause(false);
            abulletemitter[2].setPause(true);
            break;

        default:
            abulletemitter[0].setPause(false);
            abulletemitter[1].setPause(false);
            abulletemitter[2].setPause(false);
            break;
        }
        if(this == World.getPlayerAircraft())
            HUD.log("Active Guns: " + j);
        gunsActive = j;
    }

    public void typeBomberAdjAltitudeMinus()
    {
        setGunsActive(-1);
        replicateGunStateToNet();
    }

    public void typeBomberAdjAltitudePlus()
    {
        setGunsActive(1);
        replicateGunStateToNet();
    }

    public void update(float f)
    {
        super.update(f);
        replicateGunStateFromNet();
    }

    private void replicateGunStateFromNet()
    {
        if(this == World.getPlayerAircraft() && !NetMissionTrack.isPlaying())
            return;
        int i = this.FM.AS.torpedoGyroAngle;
        int j = (i / 10) * 10;
        i -= j;
        int k = i;
        for(int l = 0; gunsRPM != j && l < 100; l++)
            if(gunsRPM < j)
                setGunsRPM(1);
            else
            if(gunsRPM > j)
                setGunsRPM(-1);

        for(int i1 = 0; gunsActive != k && i1 < 4; i1++)
            if(gunsActive < k)
                setGunsActive(1);
            else
            if(gunsActive > k)
                setGunsActive(-1);

    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
                this.FM.AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
                this.FM.AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
                this.FM.AP.setStabDirection(true);
                this.FM.CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
                this.FM.AP.setStabDirection(false);
                this.FM.CT.bHasRudderControl = true;
            }
    }

    private void replicateGunStateToNet()
    {
        if(!this.FM.AS.isMaster() || NetMissionTrack.isPlaying())
            return;
        int i = 0;
        if(gunsActive < 4 && gunsActive > 1)
            i += gunsActive;
        if(gunsRPM > 10 && gunsRPM < 60000)
            i += gunsRPM;
        int j = (i & 0xff00) >> 8;
        int k = i & 0xff;
        this.FM.AS.netToMirrors(44, j, k, null);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberUpdate(float f)
    {
    }

    private float rndgear[] = {
        0.0F, 0.0F, 0.0F
    };
    private static float rndgearnull[] = {
        0.0F, 0.0F, 0.0F
    };
    private int gunsActive;
    private int gunsRPM;
    public boolean APmode1;
    public boolean APmode2;

    static 
    {
        Class class1 = AC_47.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Douglas");
        Property.set(class1, "meshNameDemo", "3DO/Plane/AC-47/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/AC-47/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 2999.9F);
        Property.set(class1, "FlightModel", "FlightModels/C-47B.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitAC47.class
        });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            3, 0, 1, 1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_BombSpawn01", "_MGUN01", "_MGUN02", "_MGUN03"
        });
    }
}
