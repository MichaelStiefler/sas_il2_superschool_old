package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class LI_2 extends Scheme2
    implements TypeTransport, TypeBomber
{

    public LI_2()
    {
        fSightCurAltitude = 300F;
        fSightCurSpeed = 50F;
        fSightCurForwardAngle = 0.0F;
        fSightSetForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -45F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 20F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 20F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -120F * f1, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("WingLOut") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 6D)
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("WingROut") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 6D)
            this.FM.AS.hitTank(shot.initiator, 3, 1);
        if(shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 1.940000057220459D)
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 1.940000057220459D)
            this.FM.AS.hitTank(shot.initiator, 2, 1);
        if(shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("Nose") && ((Tuple3d) (Aircraft.Pd)).x > 4.9000000953674316D && ((Tuple3d) (Aircraft.Pd)).z > -0.090000003576278687D && World.Rnd().nextFloat() < 0.1F)
            if(((Tuple3d) (Aircraft.Pd)).y > 0.0D)
            {
                killPilot(shot.initiator, 0);
                this.FM.setCapableOfBMP(false, shot.initiator);
            } else
            {
                killPilot(shot.initiator, 1);
            }
        if(shot.chunkName.startsWith("Turret1"))
        {
            killPilot(shot.initiator, 2);
            if(((Tuple3d) (Aircraft.Pd)).z > 1.3350000381469727D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
                HUD.logCenter("H E A D S H O T");
            return;
        }
        if(shot.chunkName.startsWith("Turret2"))
        {
            this.FM.turret[1].bIsOperable = false;
            return;
        }
        if(shot.chunkName.startsWith("Turret3"))
        {
            this.FM.turret[2].bIsOperable = false;
            return;
        }
        if(shot.chunkName.startsWith("Tail") && ((Tuple3d) (Aircraft.Pd)).x < -5.8000001907348633D && ((Tuple3d) (Aircraft.Pd)).x > -6.7899999618530273D && ((Tuple3d) (Aircraft.Pd)).z > -0.44900000000000001D && ((Tuple3d) (Aircraft.Pd)).z < 0.12399999797344208D)
            this.FM.AS.hitPilot(shot.initiator, World.Rnd().nextInt(3, 4), (int)(shot.mass * 1000F * World.Rnd().nextFloat(0.9F, 1.1F)));
        if(this.FM.AS.astateEngineStates[0] > 2 && this.FM.AS.astateEngineStates[1] > 2 && World.Rnd().nextInt(0, 99) < 33)
            this.FM.setCapableOfBMP(false, shot.initiator);
        super.msgShot(shot);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("Nose"))
        {
            if(chunkDamageVisible("Nose") < 3)
                hitChunk("Nose", shot);
            if(World.Rnd().nextFloat() < 0.0575F)
                if(point3d.y > 0.0D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                else
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            if(point3d.x > 1.726D)
            {
                if(point3d.z > 0.44400000000000001D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if(point3d.z > -0.28100000000000003D && point3d.z < 0.44400000000000001D)
                    if(point3d.y > 0.0D)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    else
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if(point3d.x > 2.774D && point3d.x < 3.718D && point3d.z > 0.42499999999999999D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                if(World.Rnd().nextFloat() < 0.12F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            return;
        } else
        {
            return;
        }
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 0:
            this.FM.turret[3].setHealth(f);
            break;

        case 2:
            this.FM.turret[0].setHealth(f);
            break;

        case 3:
            this.FM.turret[1].setHealth(f);
            break;

        case 4:
            this.FM.turret[2].setHealth(f);
            break;
        }
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

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 4; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0:
            if(f1 < -5F)
            {
                f1 = -5F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;

        case 1:
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 45F)
            {
                f = 45F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;

        case 2:
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 45F)
            {
                f = 45F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;

        case 3:
            if(f < -5F)
            {
                f = -5F;
                flag = false;
            }
            if(f > 5F)
            {
                f = 5F;
                flag = false;
            }
            if(f1 < -5F)
            {
                f1 = -5F;
                flag = false;
            }
            if(f1 > 5F)
            {
                f1 = 5F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
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

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle += 0.2F;
        if(fSightCurForwardAngle > 75F)
            fSightCurForwardAngle = 75F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)(fSightCurForwardAngle * 1.0F))
        });
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle -= 0.2F;
        if(fSightCurForwardAngle < -15F)
            fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)(fSightCurForwardAngle * 1.0F))
        });
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip++;
        if(fSightCurSideslip > 45F)
            fSightCurSideslip = 45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 1.0F))
        });
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip--;
        if(fSightCurSideslip < -45F)
            fSightCurSideslip = -45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 1.0F))
        });
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if(fSightCurAltitude > 10000F)
            fSightCurAltitude = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 10F;
        if(fSightCurAltitude < 300F)
            fSightCurAltitude = 300F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 5F;
        if(fSightCurSpeed > 650F)
            fSightCurSpeed = 650F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 5F;
        if(fSightCurSpeed < 50F)
            fSightCurSpeed = 50F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberUpdate(float f)
    {
        double d = ((double)fSightCurSpeed / 3.6000000000000001D) * Math.sqrt((double)fSightCurAltitude * 0.20387359799999999D);
        d -= (double)(fSightCurAltitude * fSightCurAltitude) * 1.419E-005D;
        fSightSetForwardAngle = (float)Math.toDegrees(Math.atan(d / (double)fSightCurAltitude));
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeFloat(fSightCurSpeed);
        netmsgguaranted.writeFloat(fSightCurForwardAngle);
        netmsgguaranted.writeFloat(fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readFloat();
        fSightCurSideslip = netmsginput.readFloat();
    }

    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurForwardAngle;
    public float fSightSetForwardAngle;
    public float fSightCurSideslip;

    static 
    {
        Class class1 = LI_2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Douglas");
        Property.set(class1, "meshName", "3do/plane/Li-2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Li-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitLI2.class, CockpitLI2_Bombardier.class, CockpitLI2_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 13, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn01"
        });
    }
}
