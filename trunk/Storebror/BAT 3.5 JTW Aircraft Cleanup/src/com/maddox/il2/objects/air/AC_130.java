package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class AC_130 extends KC_130
    implements TypeBomber
{

    public AC_130()
    {
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        fSightSetForwardAngle = 0.0F;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 2; i < 7; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(this.FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(this.FM.isPlayers())
            bChangedPit = true;
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
    }

    public boolean typeBomberToggleAutomation()
    {
        bSightAutomation = !bSightAutomation;
        bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (bSightAutomation ? "ON" : "OFF"));
        return bSightAutomation;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle++;
        if(fSightCurForwardAngle > 85F)
            fSightCurForwardAngle = 85F;
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)fSightCurForwardAngle)
        });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if(fSightCurForwardAngle < 0.0F)
            fSightCurForwardAngle = 0.0F;
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)fSightCurForwardAngle)
        });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip += 0.1F;
        if(fSightCurSideslip > 3F)
            fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 10F))
        });
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip -= 0.1F;
        if(fSightCurSideslip < -3F)
            fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 10F))
        });
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 3000F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 50F;
        if(fSightCurAltitude > 50000F)
            fSightCurAltitude = 50000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 50F;
        if(fSightCurAltitude < 1000F)
            fSightCurAltitude = 1000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 200F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 10F;
        if(fSightCurSpeed > 450F)
            fSightCurSpeed = 450F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 10F;
        if(fSightCurSpeed < 100F)
            fSightCurSpeed = 100F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberUpdate(float f)
    {
        if((double)Math.abs(this.FM.Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * f;
            if(fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if(fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * f;
        else
        if(bSightAutomation)
        {
            fSightCurDistance -= toMetersPerSecond(fSightCurSpeed) * f;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / toMeters(fSightCurAltitude)));
            if((double)fSightCurDistance < (double)toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
                bSightBombDump = true;
            if(bSightBombDump)
                if(this.FM.isTick(3, 0))
                {
                    if(this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        this.FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    this.FM.CT.WeaponControl[3] = false;
                }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(fSightCurDistance);
        netmsgguaranted.writeByte((int)fSightCurForwardAngle);
        netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeByte((int)(fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int)(fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readUnsignedByte();
        fSightCurSideslip = -3F + (float)netmsginput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = (float)netmsginput.readUnsignedByte() * 2.5F;
        fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
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

    public void update(float f)
    {
        super.update(f);
        Pilot pilot = (Pilot)this.FM;
        if(pilot != null)
        {
            Actor actor = War.GetNearestEnemy(this, getArmy(), 2000F);
            if(actor != null && pilot != null && Actor.isAlive(actor) && !(actor instanceof BridgeSegment))
            {
                Point3d point3d = new Point3d();
                actor.pos.getAbs(point3d);
                if(this.pos.getAbsPoint().distance(point3d) < 2000D && this.pos.getAbsPoint().distance(point3d) > 100D)
                {
                    point3d.sub(this.FM.Loc);
                    this.FM.Or.transformInv(point3d);
                    if(point3d.y < 0.0D)
                    {
                        this.FM.turret[0].target = actor;
                        this.FM.turret[0].tMode = 3;
                        this.FM.turretSkill = 3;
                    }
                }
            } else
            if(actor != null)
            {
                for(int i = 0; i < this.FM.turret.length; i++)
                    if(super.FM.turret[i].target != null && !(this.FM.turret[i].target instanceof Aircraft) && !Actor.isAlive(this.FM.turret[i].target))
                        this.FM.turret[i].target = null;

            }
        }
    }

    public boolean turretAngles1(int i, float af[])
    {
        boolean flag = this.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        case 0:
            if(f1 < -88F)
            {
                f1 = -88F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public float getBpos()
    {
        return bpos;
    }

    public void setBpos(float bpos)
    {
        this.bpos = bpos;
    }

    public Actor getVictim()
    {
        return victim;
    }

    public void setVictim(Actor victim)
    {
        this.victim = victim;
    }

    public static boolean bChangedPit = false;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public float fSightSetForwardAngle;
    private float bpos;
    public float bcurpos;
    private Actor victim;

    static 
    {
        Class class1 = AC_130.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AC-130");
        Property.set(class1, "meshName", "3DO/Plane/AC-130A(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/AC-130A(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1948.5F);
        Property.set(class1, "yearExpired", 1960.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-29.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitAC_130.class
        });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            3, 1, 1, 1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_BombSpawn01", "_MGUN01", "_MGUN02", "_MGUN03"
        });
    }
}
