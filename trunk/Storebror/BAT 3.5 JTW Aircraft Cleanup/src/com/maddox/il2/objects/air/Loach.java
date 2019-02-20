package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public class Loach extends Scheme1
    implements TypeScout, TypeTransport, TypeStormovik
{

    public Loach()
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

    public void update(float f)
    {
        tiltRotor(f);
        computeVerticalThrust();
        computeOrizzontalThrust();
        computeOrizzontalThrust2();
        computeHovering();
        computeHovering2();
        computeEngine();
        computeMass();
        boolean flag = false;
        super.update(f);
        Pilot pilot = (Pilot)FM;
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
            if(FM.EI.engines[0].getPowerOutput() >= 0.0F && FM.EI.engines[0].getStage() == 6)
            {
                if(FM.EI.engines[0].getPowerOutput() > 0.5F)
                    FM.AS.setSootState(this, 0, 2);
                else
                    FM.AS.setSootState(this, 0, 4);
            } else
            {
                FM.AS.setSootState(this, 0, 0);
            }
    }

    private void tiltRotor(float f)
    {
    }

    public void computeVerticalThrust()
    {
        boolean flag = true;
        if(FM.isPlayers() && (FM instanceof RealFlightModel))
            flag = !((RealFlightModel)FM).RealMode;
        float f = FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (FM)).Loc)).z, FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 200F, 1.0F, 0.0F);
        float f3 = Aircraft.cvt(FM.getAltitude(), 0.0F, 4000F, 1.0F, 0.5F);
        float f4 = 0.0F;
        if(FM.EI.engines[0].getStage() > 5 && FM.EI.getPowerOutput() > 0.2F && flag && f1 < 10F && FM.getSpeedKMH() > 3F)
            f4 = 1.87F * f;
        else
        if(FM.EI.engines[0].getStage() > 5 && FM.EI.getPowerOutput() > 0.2F && flag && f1 > 10F)
            f4 = 1.0F * f;
        else
        if(FM.EI.engines[0].getStage() > 5 && !flag)
            f4 = 1.32F * f;
        FM.producedAF.z += f4 * (10F * FM.M.referenceWeight + 10F * FM.M.fuel) * (1.0F * f2) * (1.0F * f3);
    }

    public void computeOrizzontalThrust()
    {
        boolean flag = true;
        if(FM.isPlayers() && (FM instanceof RealFlightModel))
            flag = !((RealFlightModel)FM).RealMode;
        float f = FM.EI.engines[0].getThrustOutput();
        float f1 = Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 80F, 1.0F, 0.0F);
        float f2 = 0.0F;
        if(FM.EI.engines[0].getStage() > 5 && !flag)
            f2 = 0.2F * f;
        else
        if(FM.EI.engines[0].getStage() > 5 && FM.getSpeedKMH() > 3F && flag)
            f2 = 0.1F * f;
        FM.producedAF.x -= f2 * (10F * FM.M.mass) * (1.0F * f1);
    }

    public void computeOrizzontalThrust2()
    {
        float f = FM.EI.engines[0].getThrustOutput();
        float f1 = 0.0F;
        if(FM.EI.engines[0].getStage() > 5 && FM.EI.getPowerOutput() > 0.0F && FM.getSpeedKMH() < 0.0F)
            f1 = 0.2F * f;
        FM.producedAF.x += f1 * (10F * FM.M.mass);
    }

    public void computeHovering()
    {
        float f = FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (FM)).Loc)).z, FM.getSpeed()) * 3.6F;
        float f2 = 0.0F;
        if(FM.EI.engines[0].getStage() > 5 && f1 < 50F && FM.getSpeedKMH() > 15F && FM.CT.StabilizerControl)
            f2 = 0.56F * f;
        FM.producedAF.x -= f2 * (10F * FM.M.mass);
    }

    public void computeHovering2()
    {
        float f = FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (FM)).Loc)).z, FM.getSpeed()) * 3.6F;
        float f2 = 0.0F;
        if(FM.EI.engines[0].getStage() > 5 && f1 < 18F && FM.getSpeedKMH() > 2.0F && FM.CT.StabilizerControl)
            f2 = 0.05F * f;
        FM.producedAF.y -= f2 * (10F * FM.M.mass);
    }

    public void computeEngine()
    {
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getStage() > 5)
            if(FM.EI.getPowerOutput() <= 0.0F);
        if((double)f > 6D)
        {
            f1 = 5F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            f1 = (0.0493827F * f3 - 0.266667F * f2) + 1.08889F * f;
        }
        FM.producedAF.x -= f1 * 1000F;
    }

    public void computeMass()
    {
        float f = Aircraft.cvt(FM.getSpeedKMH(), 70F, 160F, 896F, 3000F);
        if(FM.getSpeedKMH() > 100F)
            FM.M.massEmpty = 1.0F * f;
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
        Class class1 = Loach.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Loach ");
        Property.set(class1, "meshName", "3DO/Plane/Loach/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1966F);
        Property.set(class1, "yearExpired", 2010F);
        Property.set(class1, "FlightModel", "FlightModels/Loach.fmd:LoachFM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitLoach.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 9, 9, 9, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 9, 9, 9, 9, 9, 9, 0, 
            0, 0, 0, 9, 9, 1, 1, 2, 2, 2, 
            2, 9, 9, 1, 1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", 
            "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", 
            "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_MGUN03", 
            "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev10", "_ExternalDev11", "_MGUN07", "_MGUN08", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", 
            "_ExternalRock22", "_ExternalDev12", "_ExternalDev13", "_MGUN09", "_MGUN10"
        });
    }
}