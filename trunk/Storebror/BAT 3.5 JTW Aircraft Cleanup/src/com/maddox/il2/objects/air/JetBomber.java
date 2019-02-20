package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombJATO;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class JetBomber extends JB
    implements TypeBomber
{

    public JetBomber()
    {
        bHasBoosters = true;
        boosterFireOutTime = -1L;
        bpos = 1.0F;
        bcurpos = 1.0F;
        btme = -1L;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
    }

    public void destroy()
    {
        doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters()
    {
        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhite.eff", 30F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhite.eff", 30F);
    }

    public void doCutBoosters()
    {
        for(int i = 0; i < 2; i++)
            if(booster[i] != null)
            {
                booster[i].start();
                booster[i] = null;
            }

    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        for(int i = 0; i < 2; i++)
            try
            {
                booster[i] = new BombJATO();
                booster[i].pos.setBase(this, findHook("_BoosterH" + (i + 1)), false);
                booster[i].pos.resetAsBase();
                booster[i].drawing(true);
            }
            catch(Exception exception)
            {
                debugprintln("Structure corrupt - can't hang Starthilferakete..");
            }

    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
        case 34:
        case 35:
        case 36:
        case 37:
        case 38:
            doCutBoosters();
            FM.AS.setGliderBoostOff();
            bHasBoosters = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        super.update(f);
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            if(this.FM.EI.engines[0].getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6)
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 0.95F)
                    this.FM.AS.setSootState(this, 0, 3);
                else
                    this.FM.AS.setSootState(this, 0, 2);
            } else
            {
                this.FM.AS.setSootState(this, 0, 0);
            }
            if(this.FM.EI.engines[1].getPowerOutput() > 0.8F && this.FM.EI.engines[1].getStage() == 6)
            {
                if(this.FM.EI.engines[1].getPowerOutput() > 0.95F)
                    this.FM.AS.setSootState(this, 1, 3);
                else
                    this.FM.AS.setSootState(this, 1, 2);
            } else
            {
                this.FM.AS.setSootState(this, 1, 0);
            }
            if(this.FM.EI.engines[2].getPowerOutput() > 0.8F && this.FM.EI.engines[2].getStage() == 6)
            {
                if(this.FM.EI.engines[2].getPowerOutput() > 0.95F)
                    this.FM.AS.setSootState(this, 2, 3);
                else
                    this.FM.AS.setSootState(this, 2, 2);
            } else
            {
                this.FM.AS.setSootState(this, 2, 0);
            }
            if(this.FM.EI.engines[3].getPowerOutput() > 0.8F && this.FM.EI.engines[3].getStage() == 6)
            {
                if(this.FM.EI.engines[3].getPowerOutput() > 0.95F)
                    this.FM.AS.setSootState(this, 3, 3);
                else
                    this.FM.AS.setSootState(this, 3, 2);
            } else
            {
                this.FM.AS.setSootState(this, 3, 0);
            }
        }
        if((FM instanceof Pilot) && bHasBoosters)
        {
            if(FM.getAltitude() > 300F && boosterFireOutTime == -1L && FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F)
            {
                doCutBoosters();
                FM.AS.setGliderBoostOff();
                bHasBoosters = false;
            }
            if(bHasBoosters && boosterFireOutTime == -1L && FM.Gears.onGround() && FM.EI.getPowerOutput() > 0.8F && FM.getSpeedKMH() > 20F)
            {
                boosterFireOutTime = Time.current() + 30000L;
                doFireBoosters();
                FM.AS.setGliderBoostOn();
            }
            if(bHasBoosters && boosterFireOutTime > 0L)
            {
                if(Time.current() < boosterFireOutTime)
                    FM.producedAF.x += 20000D;
                if(Time.current() > boosterFireOutTime + 10000L)
                {
                    doCutBoosters();
                    FM.AS.setGliderBoostOff();
                    bHasBoosters = false;
                }
            }
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 7; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

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
        if((double)Math.abs(FM.Or.getKren()) > 4.5D)
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
                if(FM.isTick(3, 0))
                {
                    if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    FM.CT.WeaponControl[3] = false;
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

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 2:
            FM.turret[0].bIsOperable = false;
            break;

        case 3:
            FM.turret[1].bIsOperable = false;
            FM.turret[2].bIsOperable = false;
            break;
        }
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
            if(f < -23F)
            {
                f = -23F;
                flag = false;
            }
            if(f > 23F)
            {
                f = 23F;
                flag = false;
            }
            if(f1 < -25F)
            {
                f1 = -25F;
                flag = false;
            }
            if(f1 > 15F)
            {
                f1 = 15F;
                flag = false;
            }
            break;

        case 1:
            if(f1 < 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            if(f1 > 88F)
            {
                f1 = 88F;
                flag = false;
            }
            break;

        case 2:
            if(f1 < -88F)
            {
                f1 = -88F;
                flag = false;
            }
            if(f1 > 2.0F)
            {
                f1 = 2.0F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    private Bomb booster[] = {
        null, null
    };
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;
    private float bpos;
    private float bcurpos;
    private long btme;
    public static boolean bChangedPit = false;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;

    static 
    {
        Class class1 = JetBomber.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "JetBomber");
        Property.set(class1, "meshName", "3DO/Plane/JetBomber(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/JB.fmd:J_B");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitJetBomber.class, CockpitJetBomber_Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            11, 11, 3, 3, 3, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", 
            "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", 
            "_ExternalRock16", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08"
        });
    }
}
