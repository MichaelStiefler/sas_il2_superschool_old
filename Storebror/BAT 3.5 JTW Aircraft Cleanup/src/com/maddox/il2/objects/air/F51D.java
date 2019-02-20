package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class F51D extends P_51
    implements TypeFighterAceMaker
{

    public F51D()
    {
        k14Mode = 2;
        k14WingspanType = 1;
        k14Distance = 250F;
        timeCounter = 0.0F;
        timeCounterneg = 0.0F;
        temps = World.Rnd().nextFloat(1260F, 1740F);
        tempsneg = World.Rnd().nextFloat(12F, 16F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets() || FM.CT.Weapons[2] != null && FM.CT.Weapons[2][0] != null && FM.CT.Weapons[2][FM.CT.Weapons[2].length - 1].haveBullets())
        {
            hierMesh().chunkVisible("Pylon1_D0", true);
            hierMesh().chunkVisible("Pylon2_D0", true);
            hierMesh().chunkVisible("Pylon3_D0", true);
            hierMesh().chunkVisible("Pylon4_D0", true);
            hierMesh().chunkVisible("Pylon5_D0", true);
            hierMesh().chunkVisible("Pylon6_D0", true);
            hierMesh().chunkVisible("Pylon7_D0", true);
            hierMesh().chunkVisible("Pylon8_D0", true);
            hierMesh().chunkVisible("Pylon9_D0", true);
            hierMesh().chunkVisible("Pylon10_D0", true);
            hierMesh().chunkVisible("Pylon11_D0", true);
            hierMesh().chunkVisible("Pylon12_D0", true);
        } else
        {
            hierMesh().chunkVisible("Pylon1_D0", false);
            hierMesh().chunkVisible("Pylon2_D0", false);
            hierMesh().chunkVisible("Pylon3_D0", false);
            hierMesh().chunkVisible("Pylon4_D0", false);
            hierMesh().chunkVisible("Pylon5_D0", false);
            hierMesh().chunkVisible("Pylon6_D0", false);
            hierMesh().chunkVisible("Pylon7_D0", false);
            hierMesh().chunkVisible("Pylon8_D0", false);
            hierMesh().chunkVisible("Pylon9_D0", false);
            hierMesh().chunkVisible("Pylon10_D0", false);
            hierMesh().chunkVisible("Pylon11_D0", false);
            hierMesh().chunkVisible("Pylon12_D0", false);
        }
        FM.CT.bHasLockGearControl = false;
        FM.SensPitch = 0.496F;
        FM.SensRoll = 0.267F;
        FM.SensYaw = 0.65F;
        FM.EI.engines[0].tOilOutMaxRPM = 106F;
        FM.setGCenter(0.0F);
        FM.EI.engines[0].setPropReductorValue(0.591F);
        FM.AS.setLandingLightState(false);
        FM.Gears.bTailwheelLocked = true;
    }

    public void update(float f)
    {
        float k = World.Rnd().nextFloat(0.87F, 1.04F);
        if(FM.isPlayers() && FM.CT.cockpitDoorControl > 0.9F && FM.getSpeed() > 70F * k && FM.AS.aircraft.hierMesh().chunkFindCheck("Blister1_D0") != -1 && FM.AS.getPilotHealth(0) > 0.0F)
        {
            this.playSound("aircraft.arrach", true);
            FM.AS.aircraft.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage((ActorHMesh)FM.AS.actor, FM.AS.aircraft.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) (((SndAircraft) (FM.AS.aircraft)).FM)).Vwld);
            wreckage.setSpeed(vector3d);
            FM.CT.cockpitDoorControl = 0.9F;
            FM.CT.bHasCockpitDoorControl = false;
            FM.VmaxAllowed = 161F;
            FM.Sq.dragEngineCx[0] *= 6.2F;
        }
        if(!FM.isPlayers())
            if(FM.EI.engines[0].getRPM() < 500F)
                FM.CT.cockpitDoorControl = 1.0F;
            else
                FM.CT.cockpitDoorControl = 0.0F;
        if(FM.CT.cockpitDoorControl > 0.5F && FM.getSpeed() > 18F)
            FM.VmaxAllowed = FM.getSpeed() + 1.0F;
        if(FM.CT.GearControl < 0.5F && FM.AS.bLandingLightOn)
            FM.AS.setLandingLightState(false);
        if(!FM.Gears.onGround())
        {
            FM.SensYaw = 0.48F;
            petrole();
            FM.EI.engines[0].addVside *= 1.01953D;
            FM.EI.engines[0].addVflow *= 1.0076499999999999D;
        }
        if(!FM.Gears.onGround() && FM.getSpeed() < 48F)
            FM.setGCenter(-0.09F);
        if(FM.getSpeed() > 40F * k)
            FM.CT.bHasCockpitDoorControl = false;
        else
            FM.CT.bHasCockpitDoorControl = true;
        if(FM.EI.engines[0].getRPM() > 2920F || FM.EI.engines[0].getManifoldPressure() > 2.304F || timeCounter > 1400F)
        {
            timeCounter += f;
            if(timeCounter > temps)
            {
                FM.EI.engines[0].tOilOutMaxRPM = 119.8F;
                FM.Sq.dragEngineCx[0] *= 5.4F;
            }
        } else
        {
            timeCounter = 0.0F;
        }
        if(FM.getOverload() < 0.0F)
        {
            timeCounterneg += f;
            if(timeCounterneg > tempsneg)
            {
                timeCounterneg = 0.0F;
                FM.EI.engines[0].tOilOutMaxRPM = 119.4F;
                FM.Sq.dragEngineCx[0] *= 5.7F;
                if(World.Rnd().nextFloat(0.0F, 1.0F) > 0.54F)
                    FM.EI.engines[0].doSetCyliderKnockOut(1);
            }
        } else
        {
            timeCounterneg = 0.0F;
        }
        super.update(f);
        calcg();
        compression();
        roulette();
        trims();
    }

    private void trims()
    {
        if(FM.CT.getTrimAileronControl() > 0.35F)
            FM.CT.setTrimAileronControl(0.35F);
        if(FM.CT.getTrimAileronControl() < -0.35F)
            FM.CT.setTrimAileronControl(-0.35F);
        if(FM.CT.getTrimElevatorControl() > 0.241F)
            FM.CT.setTrimElevatorControl(0.241F);
        if(FM.CT.getTrimElevatorControl() < -0.241F)
            FM.CT.setTrimElevatorControl(-0.241F);
    }

    private void roulette()
    {
        if(FM.Gears.onGround() && FM.CT.ElevatorControl >= 0.0F)
        {
            if(FM.EI.engines[0].getManifoldPressure() <= 1.417F)
            {
                FM.Gears.bTailwheelLocked = true;
                FM.EI.engines[0].addVside *= 1.0125299999999999D;
                FM.EI.engines[0].addVflow *= 1.0076499999999999D;
            } else
            if(FM.EI.engines[0].getManifoldPressure() > 1.417F)
            {
                FM.Gears.bTailwheelLocked = true;
                FM.SensYaw = 0.41F;
                double couple = FM.getSpeedKMH();
                FM.EI.engines[0].addVside *= 1.01953D;
                FM.EI.engines[0].addVflow *= 1.0076499999999999D;
            }
        } else
        if(FM.Gears.onGround() && FM.CT.ElevatorControl < -0.58F && FM.EI.engines[0].getManifoldPressure() < 1.417F)
        {
            FM.Gears.bTailwheelLocked = false;
            FM.SensYaw = 0.91F;
            petrole();
            FM.EI.engines[0].addVside *= 0.91520000000000001D;
        } else
        if(FM.Gears.onGround() && FM.CT.ElevatorControl < -0.42F && FM.EI.engines[0].getManifoldPressure() > 1.417F)
        {
            FM.Gears.bTailwheelLocked = false;
            FM.SensYaw = 0.91F;
            petrole();
            double couple2 = FM.getSpeedKMH();
            if(couple2 > 80D)
                couple2 = 80D;
            FM.EI.engines[0].addVside *= 1.01153D + couple2 / 10000D;
            FM.EI.engines[0].addVflow *= 1.0076499999999999D;
        }
    }

    private void petrole()
    {
        float petrole = FM.M.fuel;
        float cg = (petrole - 593F) / 591.28F;
        if(cg < 0.0F)
            cg = 0.0F;
        float reverse = FM.getAOA();
        reverse = (cg * reverse) / 11.9F;
        if(reverse <= 0.0F)
            reverse = 1E-005F;
        FM.SensPitch += reverse;
        if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
            FM.setGCenter(0.007F - cg - reverse);
        else
            FM.setGCenter(0.015F - cg - reverse);
        for(int l = 0; l < FM.CT.Weapons.length; l++)
            if(FM.CT.Weapons[l] != null)
            {
                for(int j1 = 0; j1 < FM.CT.Weapons[l].length; j1 += 2)
                    if((FM.CT.Weapons[l][j1] instanceof FuelTankGun) && FM.CT.Weapons[l][j1].haveBullets())
                        FM.setGCenter(0.007F - cg - reverse);
                    else
                        FM.setGCenter(0.015F - cg - reverse);

            }

    }

    private void calcg()
    {
        G = FM.getOverload();
        limite = 36800F / FM.M.mass;
        if(limite < G)
            FM.SensPitch = FM.SensPitch - G / 91F;
        if((double)G > (double)limite * 1.1040000000000001D)
        {
            FM.VmaxAllowed = FM.getSpeed() - 30F;
            FM.SensPitch = FM.SensPitch + G / 91F;
        } else
        if(FM.CT.cockpitDoorControl != 0.9F)
            FM.SensPitch = 0.496F - G / 91F;
        else
            FM.SensPitch = 0.496F - G / 91F;
    }

    private void compression()
    {
        if(FM.CT.cockpitDoorControl != 0.9F)
        {
            coeff = World.Rnd().nextFloat(-0.15F, 0.012F) * (FM.getSpeed() / 100F);
            if(FM.getSpeed() > 264F)
            {
                FM.VmaxAllowed = FM.getSpeed() + 2.0F;
                if(FM.VmaxAllowed > 310F)
                    FM.VmaxAllowed = 310F;
                FM.SensPitch = FM.SensPitch + coeff * 1.71F;
                FM.SensRoll = FM.SensRoll - coeff * 0.34F;
            } else
            {
                FM.SensPitch = 0.496F - G / 67F;
                FM.SensRoll = 0.267F;
                petrole();
            }
        }
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        if(FM.CT.GearControl > 0.5F)
            hierMesh().chunkSetAngles("GearC1_D0", 0.0F, -6F * f, 0.0F);
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 50F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 50F;
        if(k14Distance < 150F)
            k14Distance = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.675F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    private float G;
    private float limite;
    private float coeff;
    private float timeCounter;
    private float timeCounterneg;
    private float temps;
    private float tempsneg;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;

    static 
    {
        Class class1 = F51D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-51");
        Property.set(class1, "meshNameDemo", "3DO/Plane/F-51(USA)/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/F-51(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/F-51(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1947F);
        Property.set(class1, "yearExpired", 1957.5F);
        Property.set(class1, "FlightModel", "FlightModels/F51D.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitF51.class
        });
        Property.set(class1, "LOSElevation", 1.06935F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 
            3, 3, 3, 3, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDEV01", "_ExternalDEV02", "_ExternalDEV03", "_ExternalDEV04", 
            "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06"
        });
    }
}
