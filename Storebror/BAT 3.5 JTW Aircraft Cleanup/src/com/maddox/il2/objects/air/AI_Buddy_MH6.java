package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Property;

public class AI_Buddy_MH6 extends Scheme1
    implements TypeScout, TypeTransport, TypeStormovik
{

    public AI_Buddy_MH6()
    {
        suka = new Loc();
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        rotorrpm = 0;
        pictVBrake = 0.0F;
        pictAileron = 0.0F;
        pictVator = 0.0F;
        pictRudder = 0.0F;
        pictBlister = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
    }

    protected void moveElevator(float f)
    {
    }

    protected void moveAileron(float f)
    {
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
    }

    public void moveSteering(float f)
    {
    }

    protected void moveFan(float f)
    {
        rotorrpm = Math.abs((int)((double)(FM.EI.engines[0].getw() * 0.025F) + FM.Vwld.length() / 30D));
        if(rotorrpm >= 1)
            rotorrpm = 1;
        if(FM.EI.engines[0].getw() > 100F)
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if(FM.EI.engines[0].getw() < 100F)
        {
            hierMesh().chunkVisible("Prop1_D0", true);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop1_D1"))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if(FM.EI.engines[0].getw() > 100F)
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if(FM.EI.engines[0].getw() < 100F)
        {
            hierMesh().chunkVisible("Prop2_D0", true);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop2_D1"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Tail1_CAP"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
            hierMesh().chunkVisible("Prop2_D1", false);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 100F) % 360F : (float)((double)dynamoOrient - (double)rotorrpm * 25D) % 360F;
        hierMesh().chunkSetAngles("Prop1_D0", -dynamoOrient, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, dynamoOrient * -10F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.42F, 0.0F, 0.3F);
        hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(15F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(4F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor(2.0F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
            if(s.startsWith("xxcontrols"))
                if(s.endsWith("1"))
                {
                    if(World.Rnd().nextFloat() < 0.3F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.3F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                } else
                if(s.endsWith("2"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.3F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.3F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                } else
                if(s.endsWith("3"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                } else
                if(s.startsWith("xxeng1"))
                {
                    if(s.endsWith("prp") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                        FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                    if(s.endsWith("cas") && getEnergyPastArmor(0.7F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat(20000F, 200000F) < shot.power)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat(10000F, 50000F) < shot.power)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                        if(World.Rnd().nextFloat(8000F, 28000F) < shot.power)
                        {
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        }
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    if(s.endsWith("cyl") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(FM.AS.astateEngineStates[0] < 1)
                            FM.AS.hitEngine(shot.initiator, 0, 1);
                        if(World.Rnd().nextFloat() < shot.power / 24000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                        getEnergyPastArmor(25F, shot);
                    }
                    if(s.endsWith("sup") && getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[0].setKillCompressor(shot.initiator);
                    if(s.endsWith("sup"))
                    {
                        if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                            FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                        if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                            FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                        if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                            FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                        if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                            FM.EI.engines[0].setEngineStuck(shot.initiator);
                        if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                            FM.EI.engines[0].setEngineStops(shot.initiator);
                    }
                    if(s.endsWith("oil"))
                        FM.AS.hitOil(shot.initiator, 0);
                }
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F)
                        FM.AS.hitTank(shot.initiator, i, 2);
                }
            }
            return;
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
            hitChunk("CF", shot);
        if(s.startsWith("xcockpit"))
        {
            if(point3d.z > 0.75D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(point3d.x > -1.1000000238418579D && World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(World.Rnd().nextFloat() < 0.25F)
                if(point3d.y > 0.0D)
                {
                    if(point3d.x > -1.1000000238418579D)
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                    else
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                } else
                if(point3d.x > -1.1000000238418579D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                else
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
        } else
        if(s.startsWith("xeng"))
            hitChunk("Engine1", shot);
        else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglout"))
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout"))
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int j;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else
            {
                j = s.charAt(5) - 49;
            }
            hitFlesh(j, shot, byte0);
        }
    }

    private void stability()
    {
        double d = 0.0D;
        Vector3d vector3d = new Vector3d();
        getSpeed(vector3d);
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        float f = (float)((double)FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
        if(f < 10F && FM.getSpeedKMH() < 60F && vector3d.z < -1D)
        {
            vector3d.z *= 0.90000000000000002D;
            setSpeed(vector3d);
        }
        if(this.FM.Gears.nOfGearsOnGr > 2)
        {
            vector3d.x *= 0.98999999999999999D;
            vector3d.y *= 0.98999999999999999D;
            setSpeed(vector3d);
        }
        if(FM.getSpeedKMH() > 180F)
        {
            d = (FM.getSpeedKMH() - FM.VmaxFLAPS) / 10F;
            if(d < 0.0D)
                d = 0.0D;
        }
        Point3d point3d1 = new Point3d(0.0D, 0.0D, 0.0D);
        point3d1.x = 0.0D - ((double)(FM.Or.getTangage() / 10F) - (double)FM.CT.getElevator() * 2.5D);
        point3d1.y = 0.0D - ((double)(FM.Or.getKren() / 10F) - (double)FM.CT.getAileron() * 2.5D) - d;
        point3d1.z = 2D;
        FM.EI.engines[0].setPropPos(point3d1);
        FM.producedAF.x += 7000D * (double)(-FM.CT.getElevator() * FM.EI.engines[0].getPowerOutput());
        FM.producedAF.y += 6000D * (double)(-FM.CT.getAileron() * FM.EI.engines[0].getPowerOutput());
    }

    public void update(float f)
    {
        tiltRotor(f);
        stability();
        boolean flag = false;
        super.update(f);
        Pilot pilot = (Pilot)FM;
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
            if(FM.EI.engines[0].getPowerOutput() >= 0.0F && FM.EI.engines[0].getStage() == 6)
            {
                if(FM.EI.engines[0].getPowerOutput() > 0.2F)
                    FM.AS.setSootState(this, 0, 2);
                else
                    FM.AS.setSootState(this, 0, 4);
            } else
            {
                FM.AS.setSootState(this, 0, 0);
            }
        float f1 = FM.CT.getAirBrake();
        f1 = FM.CT.getAileron();
        if(Math.abs(pictAileron - f1) > 0.01F)
            pictAileron = f1;
        f1 = FM.CT.getRudder();
        if(Math.abs(pictRudder - f1) > 0.01F)
            pictRudder = f1;
        f1 = FM.CT.getElevator();
        if(Math.abs(pictVator - f1) > 0.01F)
            pictVator = f1;
        float f2 = FM.EI.getPowerOutput() * Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 600F, 2.0F, 0.0F);
        if(FM.CT.getAirBrake() > 0.5F)
        {
            if(FM.Or.getTangage() > 5F)
            {
                FM.getW().scale(Aircraft.cvt(FM.Or.getTangage(), 45F, 90F, 1.0F, 0.1F));
                float f3 = FM.Or.getTangage();
                if(Math.abs(FM.Or.getKren()) > 90F)
                    f3 = 90F + (90F - f3);
                float f4 = f3 - 90F;
                FM.CT.trimElevator = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                f4 = FM.Or.getKren();
                if(Math.abs(f4) > 90F)
                    if(f4 > 0.0F)
                        f4 = 180F - f4;
                    else
                        f4 = -180F - f4;
                FM.CT.trimAileron = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                FM.CT.trimRudder = Aircraft.cvt(f4, -15F, 15F, 0.04F, -0.04F);
            }
        } else
        {
            FM.CT.trimAileron = 0.0F;
            FM.CT.trimElevator = 0.0F;
            FM.CT.trimRudder = 0.0F;
        }
        FM.Or.increment(f2 * (FM.CT.getRudder() + FM.CT.getTrimRudderControl()), f2 * (FM.CT.getElevator() + FM.CT.getTrimElevatorControl()), f2 * (FM.CT.getAileron() + FM.CT.getTrimAileronControl()));
    }

    private void tiltRotor(float f)
    {
    }

    private static Loc l = new Loc();
    public static boolean bChangedPit = false;
    public Loc suka;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int rotorrpm;
    private float pictVBrake;
    private float pictAileron;
    private float pictVator;
    private float pictRudder;
    private float pictBlister;
    private static final float fcA[] = {
        0.0F, 0.04F, 0.1F, 0.04F, 0.02F, -0.02F, -0.04F, -0.1F, -0.04F
    };
    private static final float fcE[] = {
        0.98F, 0.48F, 0.1F, -0.48F, -0.7F, -0.7F, -0.48F, 0.1F, 0.48F
    };
    private static final float fcR[] = {
        0.02F, 0.48F, 0.8F, 0.48F, 0.28F, -0.28F, -0.48F, -0.8F, -0.48F
    };
    private float deltaAzimuth;
    private float deltaTangage;

    static 
    {
        Class class1 = AI_Buddy_MH6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MH6 ");
        Property.set(class1, "meshName", "3DO/Plane/AI_Buddy_MH6/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1966F);
        Property.set(class1, "yearExpired", 2010F);
        Property.set(class1, "FlightModel", "FlightModels/AILoach.fmd:AILoach_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 
            0, 0, 0, 0, 9, 1, 1, 2, 2, 2, 
            2, 9, 9, 1, 1, 9, 9, 9, 9, 9, 
            9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", 
            "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", 
            "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", 
            "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev11", "_MGUN07", "_MGUN08", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", 
            "_ExternalRock22", "_ExternalDev12", "_ExternalDev13", "_MGUN09", "_MGUN10", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", 
            "_ExternalDev19"
        });
    }
}
