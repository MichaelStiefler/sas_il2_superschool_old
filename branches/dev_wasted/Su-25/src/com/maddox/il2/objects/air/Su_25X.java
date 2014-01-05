package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Time;
import com.maddox.sound.AudioStream;
import com.maddox.sound.SoundFX;
import java.text.DecimalFormat;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, TypeSupersonic, TypeFastJet, TypeFighter, 
//            TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, PaintScheme, 
//            Aircraft, AircraftLH, NetAircraft, EjectionSeat

public class Su_25X extends Scheme2 implements TypeSupersonic, TypeFastJet,
		TypeFighterAceMaker, TypeGSuit, TypeBomber {

	public Su_25X() {
		SonicBoom = 0.0F;
		fxSPO15 = newSound("aircraft.Sirena2", false);
		smplSPO15 = new Sample("sample.Sirena2.wav", 256, 65535);
		SPO15SoundPlaying = false;
		bHasSK1Seat = true;
		k14Mode = 0;
		k14WingspanType = 0;
		k14Distance = 200F;
		overrideBailout = false;
		ejectComplete = false;
		oldthrl = -1F;
		curthrl = -1F;
		smplSPO15.setInfinite(true);
		bToFire = false;
		counter = 0;
		gearTargetAngle = -1F;
        gearCurrentAngle = -1F;
        hasHydraulicPressure = true;
        
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;

	}
	
    public boolean typeBomberToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Unguided Bomb");
        } else
        if(k14Mode == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Unguided Rocket");
        } else
        if(k14Mode == 2 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
        {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Navigation");
        }
        return true;
    }

	
    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
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
    }

    public void typeBomberAdjSideslipMinus()
    {
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
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f1)
    {
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

	
	public void checkHydraulicStatus()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getStage() < 6 && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() < 6 && ((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr > 0)
        {
            gearTargetAngle = 90F;
            hasHydraulicPressure = false;
            ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
            ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 1.0F;
        } else
        if(!hasHydraulicPressure)
        {
            gearTargetAngle = 0.0F;
            hasHydraulicPressure = true;
            ((FlightModelMain) (super.FM)).CT.bHasAileronControl = true;
            ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = true;
            ((FlightModelMain) (super.FM)).CT.bHasAirBrakeControl = true;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
        }
    }

    public void moveHydraulics(float f)
    {
        if(gearTargetAngle >= 0.0F)
            if(gearCurrentAngle < gearTargetAngle)
            {
                gearCurrentAngle += 90F * f * 0.8F;
                if(gearCurrentAngle >= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            } else
            {
                gearCurrentAngle -= 90F * f * 0.8F;
                if(gearCurrentAngle <= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            }
    }


	public void getGFactors(TypeGSuit.GFactors theGFactors) {
		theGFactors.setGFactors(1.0F, 1.0F, 1.0F, 1.8F, 1.5F, 1.0F);
	}


	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if ((((FlightModelMain) (super.FM)).Gears.nearGround() || ((FlightModelMain) (super.FM)).Gears
				.onGround())
				&& ((FlightModelMain) (super.FM)).CT.getCockpitDoor() == 1.0F)
			hierMesh().chunkVisible("HMask1_D0", false);
		else
			hierMesh().chunkVisible("HMask1_D0",
					hierMesh().isChunkVisible("Pilot1_D0"));
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			break;
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f1 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -117F);
		float f2 = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
		float f3 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, -90F, 0.0F)
				: Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
		float f4 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -87F);
		float f5 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -60F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f2, 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, f2, 0.0F);
		hiermesh.chunkSetAngles("GearC8_D0", 0.0F, f3, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, f4, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, f4, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f5, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f5, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f3, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f3, 0.0F);
		hiermesh.chunkSetAngles("Hook1_D0", 0.0F, 70F * f, 0.0F);

	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
	}

	public void moveWheelSink() {
		float f = Aircraft.cvt(
				((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F,
				0.19075F, 0.0F, 1.0F);
		resetYPRmodifier();
		Aircraft.xyz[2] = 0.19075F * f;
		hierMesh().chunkSetLocate("GearC7_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected void moveRudder(float f) {
		hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
	}

	protected void moveFlap(float f) {
		float f1 = -44F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	protected void moveAirBrake(float f) {
		hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F,  -45F * f);
		hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F,  -45F * f);
		hierMesh().chunkSetAngles("Brake03_D0", 0.0F, 0.0F,  120F * f);
		hierMesh().chunkSetAngles("Brake04_D0", 0.0F, 0.0F,  45F * f);
		hierMesh().chunkSetAngles("Brake05_D0", 0.0F, 0.0F,  -120F * f);
		hierMesh().chunkSetAngles("Brake06_D0", 0.0F, 0.0F,  120F * f);
		hierMesh().chunkSetAngles("Brake07_D0", 0.0F, 0.0F,  45F * f);
		hierMesh().chunkSetAngles("Brake08_D0", 0.0F, 0.0F,  -120F * f);
	}

	public void moveSteering(float f) {

		if (FM.CT.getGear() < 0.99)
			hierMesh().chunkSetAngles("GearC22_D0", 0.0F * f, 0.0F, 0.0F);
		else
			hierMesh().chunkSetAngles("GearC22_D0", 2.0F * f, 0.0F, 0.0F);
	}

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag = false;
        boolean flag1 = (this instanceof Su_25X);
//        boolean flag2 = !(this instanceof IL_2_1940Early) && !(this instanceof IL_2_1940Late);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.startsWith("xxarmorp"))
                {
                    int i = s.charAt(8) - 48;
                    switch(i)
                    {
                    default:
                        break;

                    case 1: // '\001'
                        getEnergyPastArmor(7.070000171661377D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        break;

                    case 2: // '\002'
                    case 3: // '\003'
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                        break;

                    case 4: // '\004'
                        if(point3d.x > -1.3500000000000001D)
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                                doRicochet(shot);
                        } else
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        }
                        break;

                    case 5: // '\005'
                    case 6: // '\006'
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        if(shot.power > 0.0F)
                            break;
                        if(Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                        else
                            doRicochetBack(shot);
                        break;

                    case 7: // '\007'
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;
                    }
                }
                if(s.startsWith("xxarmorc1"))
                    getEnergyPastArmor(7.070000171661377D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                if(s.startsWith("xxarmort1"))
                    getEnergyPastArmor(6.059999942779541D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.10000000149011612D && getEnergyPastArmor(3.4000000953674316D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxsparlm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparrm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingRMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingRMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparlo"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLOut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLOut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparro"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingROut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingROut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if(s.startsWith("xxeng"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("prop"))
                {
                    if(getEnergyPastArmor(3.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                } else
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.01F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Supercharger Disabled..");
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else
                if(s.endsWith("feed"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.endsWith("fue1"))
                {
                    if(getEnergyPastArmor(0.89F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
                        FM.AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    getEnergyPastArmor(22.5F, shot);
                } else
                if(s.endsWith("cyl1"))
                {
                    if(getEnergyPastArmor(1.3F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        debuggunnery("Engine Module: Cylinders Assembly Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Operating..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            FM.AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                    if(Math.abs(point3d.y) < 0.1379999965429306D && getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.startsWith("xxeng1mag"))
                {
                    int k = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + k + " Hit, Magneto " + k + " Disabled..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, k);
                } else
                if(s.startsWith("xxeng1oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if(s.startsWith("xxw1"))
                if(FM.AS.astateEngineStates[0] == 0)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else
                if(FM.AS.astateEngineStates[0] == 1)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        FM.AS.hitTank(shot.initiator, 2, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    } else
                    if(FM.AS.astateTankStates[l] == 1)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        FM.AS.hitTank(shot.initiator, 2, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.hitTank(shot.initiator, 2, 2);
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced, State Shifted..");
                    }
                }
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("01") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Left Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.endsWith("02") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Right Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxammo"))
            {
                if(s.startsWith("xxammol1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.startsWith("xxammor1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                if(s.startsWith("xxammol2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.startsWith("xxammor2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.00345F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                debuggunnery("Armament System: Bomb Payload Detonated..");
                FM.AS.hitTank(shot.initiator, 0, 10);
                FM.AS.hitTank(shot.initiator, 1, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if(s.startsWith("xxpnm") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Pneumo System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if(s.startsWith("xxhyd") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.startsWith("xxins"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            return;
        }
        if(s.startsWith("xcockpit") || s.startsWith("xblister"))
            if(point3d.z > 0.47299999999999998D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            else
            if(point3d.y > 0.0D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            else
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
        if(s.startsWith("xcf"))
        {
            if(point3d.x < -1.9399999999999999D)
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            {
                if(point3d.x <= 1.3420000000000001D)
                    if(point3d.z < -0.59099999999999997D || point3d.z > 0.40799999237060547D && point3d.x > 0.0D)
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                    } else
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                    }
                if(chunkDamageVisible("CF") < 3)
                    hitChunk("CF", shot);
            }
        } else
        if(s.startsWith("xoil"))
        {
            if(point3d.z < -0.98099999999999998D)
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            } else
            if(point3d.x > 0.53700000000000003D || point3d.x < -0.10000000000000001D)
            {
                getEnergyPastArmor(0.20000000298023224D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochetBack(shot);
            } else
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            }
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(point3d.z > 0.159D)
                getEnergyPastArmor((double)(1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 1.335D && point3d.x < 2.3860000000000001D && point3d.z > -0.059999999999999998D && point3d.z < 0.064000000000000001D)
                getEnergyPastArmor(0.5D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 2.5299999999999998D && point3d.x < 2.992D && point3d.z > -0.23499999999999999D && point3d.z < 0.010999999999999999D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 2.5590000000000002D && point3d.z < -0.59499999999999997D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 1.849D && point3d.x < 2.2509999999999999D && point3d.z < -0.70999999999999996D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 3.0030000000000001D)
                getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            else
            if(point3d.z < -0.60600000619888306D)
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            if(Math.abs(v1.x) > 0.86599999666213989D && (shot.power <= 0.0F || World.Rnd().nextFloat() < 0.1F))
                doRicochet(shot);
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            {
                hitChunk("Tail1", shot);
            }
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder") && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(getEnergyPastArmor(0.25F, shot) > 0.0F)
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                FM.AS.setJamBullets(10, 0);
                FM.AS.setJamBullets(10, 1);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else
        if(s.startsWith("xhelm"))
        {
            getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if(shot.power <= 0.0F)
                doRicochetBack(shot);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else
            {
                i1 = s.charAt(5) - 49;
            }
            hitFlesh(i1, shot, byte0);
        }
    }
	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19: // '\023'
			((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(actor);
			return super.cutFM(i, j, actor);
		}
		return super.cutFM(i, j, actor);
	}

	public float getAirPressure(float theAltitude) {
		float fBase = 1.0F - (0.0065F * theAltitude) / 288.15F;
		float fExponent = 5.255781F;
		return 101325F * (float) Math.pow(fBase, fExponent);
	}

	public float getAirPressureFactor(float theAltitude) {
		return getAirPressure(theAltitude) / 101325F;
	}

	public float getAirDensity(float theAltitude) {
		return (getAirPressure(theAltitude) * 0.0289644F)
				/ (8.31447F * (288.15F - 0.0065F * theAltitude));
	}

	public float getAirDensityFactor(float theAltitude) {
		return getAirDensity(theAltitude) / 1.225F;
	}

	public float getMachForAlt(float theAltValue) {
		theAltValue /= 1000F;
		int i = 0;
		for (i = 0; i < TypeSupersonic.fMachAltX.length; i++)
			if (TypeSupersonic.fMachAltX[i] > theAltValue)
				break;

		if (i == 0) {
			return TypeSupersonic.fMachAltY[0];
		} else {
			float baseMach = TypeSupersonic.fMachAltY[i - 1];
			float spanMach = TypeSupersonic.fMachAltY[i] - baseMach;
			float baseAlt = TypeSupersonic.fMachAltX[i - 1];
			float spanAlt = TypeSupersonic.fMachAltX[i] - baseAlt;
			float spanMult = (theAltValue - baseAlt) / spanAlt;
			return baseMach + spanMach * spanMult;
		}
	}

	public float getMpsFromKmh(float kilometersPerHour) {
		return kilometersPerHour / 3.6F;
	}

	public float calculateMach() {
		return super.FM.getSpeedKMH() / getMachForAlt(super.FM.getAltitude());
	}

	public void soundbarier() {
		float f = getMachForAlt(super.FM.getAltitude())
				- super.FM.getSpeedKMH();
		if (f < 0.5F)
			f = 0.5F;
		float f_0_ = super.FM.getSpeedKMH()
				- getMachForAlt(super.FM.getAltitude());
		if (f_0_ < 0.5F)
			f_0_ = 0.5F;
		if ((double) calculateMach() <= 1.0D) {
			super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f;
			SonicBoom = 0.0F;
			isSonic = false;
		}
		if ((double) calculateMach() >= 1.0D) {
			super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f_0_;
			isSonic = true;
		}
		if (((FlightModelMain) (super.FM)).VmaxAllowed > 1500F)
			super.FM.VmaxAllowed = 1500F;
		if (isSonic && SonicBoom < 1.0F) {
			super.playSound("aircraft.SonicBoom", true);
			super.playSound("aircraft.SonicBoomInternal", true);
			if (((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
				HUD.log("Mach 1 Exceeded!");
			if (Config.isUSE_RENDER()
					&& World.Rnd().nextFloat() < getAirDensityFactor(super.FM
							.getAltitude()))
				shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null,
						1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
			SonicBoom = 1.0F;
		}
		if ((double) calculateMach() > 1.01D || (double) calculateMach() < 1.0D)
			Eff3DActor.finish(shockwave);
	}

	private float flapsMovement(float f, float f1, float f2, float f3, float f4) {
		float f5 = (float) Math.exp(-f / f3);
		float f6 = f1 + (f2 - f1) * f5;
		if (f6 < f1) {
			f6 += f4 * f;
			if (f6 > f1)
				f6 = f1;
		} else if (f6 > f1) {
			f6 -= f4 * f;
			if (f6 < f1)
				f6 = f1;
		}
		return f6;
	}

	public void update(float f) {
		if (super.FM.getSpeedKMH() > 700F
				&& ((FlightModelMain) (super.FM)).CT.bHasFlapsControl) {
			((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
			((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
		} else {
			((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
		}
		if (FM.CT.getFlap() < FM.CT.FlapsControl)
			FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl,
					FM.CT.getFlap(), 999F,
					cvt(FM.getSpeedKMH(), 0F, 700F, 0.5F, 0.08F)));
		else
			FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl,
					FM.CT.getFlap(), 999F,
					cvt(FM.getSpeedKMH(), 0F, 700F, 0.5F, 0.7F)));
		// Speed-induced gear retraction limits
		float f2 = super.FM.getSpeedKMH() - 600F;
		if (f2 < 0.0F)
			f2 = 0.0F;
		((FlightModelMain) (super.FM)).CT.dvGear = 0.2F - f2 / 600F;
		if (((FlightModelMain) (super.FM)).CT.dvGear < 0.0F)
			((FlightModelMain) (super.FM)).CT.dvGear = 0.0F;
		// RWR code
/*/		try {
			if (counter == 180)
				;
			{
				SPO15air();
				// SPO15ground();
			}
			counter++;
			if (counter > 180)
				counter = 0;
		} catch (NullPointerException nullpointerexception) {
		}/*/
		engineSurge(f);
		typeFighterAceMakerRangeFinder();
		
		checkHydraulicStatus();
        moveHydraulics(f);
		
		soundbarier();
		if (!super.FM.isPlayers()) {
			if (((FlightModelMain) (super.FM)).AP.way.isLanding()
					&& ((FlightModelMain) (super.FM)).Gears.onGround()
					&& super.FM.getSpeed() > 40F)
				((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
			if (((FlightModelMain) (super.FM)).AP.way.isLanding()
					&& ((FlightModelMain) (super.FM)).Gears.onGround()
					&& super.FM.getSpeed() < 40F)
				((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;

		}
		if ((((FlightModelMain) (super.FM)).AS.bIsAboutToBailout || overrideBailout)
				&& !ejectComplete && super.FM.getSpeedKMH() > 15F) {
			overrideBailout = true;
			((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = false;
			bailout();
		}
		super.update(f);
	}

	public void doEjectCatapult() {
		new MsgAction(false, this) {

			public void doAction(Object paramObject) {
				Aircraft localAircraft = (Aircraft) paramObject;
				if (Actor.isValid(localAircraft)) {
					Loc localLoc1 = new Loc();
					Loc localLoc2 = new Loc();
					Vector3d localVector3d = new Vector3d(0.0D, 0.0D, 30D);
					HookNamed localHookNamed = new HookNamed(localAircraft,
							"_ExternalSeat01");
					((Actor) (localAircraft)).pos.getAbs(localLoc2);
					localHookNamed.computePos(localAircraft, localLoc2,
							localLoc1);
					localLoc1.transform(localVector3d);
					localVector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).x;
					localVector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).y;
					localVector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).z;
					new EjectionSeat(9, localLoc1, localVector3d, localAircraft);
				}
			}

		};
		hierMesh().chunkVisible("Seat1_D0", false);
		super.FM.setTakenMortalDamage(true, null);
		((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
		((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
		((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
		((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
		((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
	}

	private void bailout() {
		if (overrideBailout)
			if (((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 0
					&& ((FlightModelMain) (super.FM)).AS.astateBailoutStep < 2) {
				if (((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F
						&& ((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.5F) {
					((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
					doRemoveBlisters();
				} else {
					((FlightModelMain) (super.FM)).AS.astateBailoutStep = 2;
				}
			} else if (((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 2
					&& ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 3) {
				switch (((FlightModelMain) (super.FM)).AS.astateBailoutStep) {
				case 2: // '\002'
					if (((FlightModelMain) (super.FM)).CT.cockpitDoorControl < 0.5F)
						doRemoveBlister1();
					break;

				case 3: // '\003'
					doRemoveBlisters();
					break;
				}
				if (((FlightModelMain) (super.FM)).AS.isMaster())
					((FlightModelMain) (super.FM)).AS
							.netToMirrors(
									20,
									((FlightModelMain) (super.FM)).AS.astateBailoutStep,
									1, null);
				AircraftState tmp178_177 = ((FlightModelMain) (super.FM)).AS;
				tmp178_177.astateBailoutStep = (byte) (tmp178_177.astateBailoutStep + 1);
				if (((FlightModelMain) (super.FM)).AS.astateBailoutStep == 4)
					((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
			} else if (((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 11
					&& ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 19) {
				int i = ((FlightModelMain) (super.FM)).AS.astateBailoutStep;
				if (((FlightModelMain) (super.FM)).AS.isMaster())
					((FlightModelMain) (super.FM)).AS
							.netToMirrors(
									20,
									((FlightModelMain) (super.FM)).AS.astateBailoutStep,
									1, null);
				AircraftState tmp383_382 = ((FlightModelMain) (super.FM)).AS;
				tmp383_382.astateBailoutStep = (byte) (tmp383_382.astateBailoutStep + 1);
				if (i == 11) {
					super.FM.setTakenMortalDamage(true, null);
					if ((super.FM instanceof Maneuver)
							&& ((Maneuver) super.FM).get_maneuver() != 44) {
						World.cur();
						if (((FlightModelMain) (super.FM)).AS.actor != World
								.getPlayerAircraft())
							((Maneuver) super.FM).set_maneuver(44);
					}
				}
				if (((FlightModelMain) (super.FM)).AS.astatePilotStates[i - 11] < 99) {
					doRemoveBodyFromPlane(i - 10);
					if (i == 11) {
						doEjectCatapult();
						super.FM.setTakenMortalDamage(true, null);
						((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
						((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
						((FlightModelMain) (super.FM)).AS.astateBailoutStep = -1;
						overrideBailout = false;
						((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = true;
						ejectComplete = true;
					}
				}
			}
	}

	private final void doRemoveBlister1() {
		if (hierMesh().chunkFindCheck("Blister1_D0") != -1
				&& ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F) {
			hierMesh().hideSubTrees("Blister1_D0");
			if (!bHasSK1Seat) {
				Wreckage localWreckage = new Wreckage(this, hierMesh()
						.chunkFind("Blister1_D0"));
				localWreckage.collide(false);
				Vector3d localVector3d = new Vector3d();
				localVector3d.set(((FlightModelMain) (super.FM)).Vwld);
				localWreckage.setSpeed(localVector3d);
			}
		}
	}

	private final void doRemoveBlisters() {
		for (int i = 2; i < 10; i++)
			if (hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1
					&& ((FlightModelMain) (super.FM)).AS.getPilotHealth(i - 1) > 0.0F) {
				hierMesh().hideSubTrees("Blister" + i + "_D0");
				if (!bHasSK1Seat) {
					Wreckage localWreckage = new Wreckage(this, hierMesh()
							.chunkFind("Blister" + i + "_D0"));
					localWreckage.collide(false);
					Vector3d localVector3d = new Vector3d();
					localVector3d.set(((FlightModelMain) (super.FM)).Vwld);
					localWreckage.setSpeed(localVector3d);
				}
			}

	}
	
    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (super.FM)).actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)((Interpolate) (super.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 800F)
                k14Distance = 800F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
        }
    }
    
    public void engineSurge(float f)
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster())
        {
            for(int i = 0; i < 2; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
                            ((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                ((FlightModelMain) (super.FM)).AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                ((FlightModelMain) (super.FM)).EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
                            ((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            {
                                if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                ((FlightModelMain) (super.FM)).EI.engines[i].setEngineStops(this);
                            } else
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                    }
                    oldthrl = curthrl;
                }

        }
    }
    
    
    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
        } else
        if(k14Mode == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
        } else
        if(k14Mode == 2 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Off");
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }
    
    public int nozzlemode;
    public long tvect;

    public void typeFighterAceMakerAdjSideslipPlus()
    {
    	
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {

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
	
    
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    public float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;


    private float gearTargetAngle;
    private float gearCurrentAngle;
    public boolean hasHydraulicPressure;
	private int counter = 0;
//	private int counter;
	private float oldthrl;
	private float curthrl;
	private float engineSurgeDamage;
	private float SonicBoom;
	private Eff3DActor shockwave;
	private boolean isSonic;
	private boolean overrideBailout;
	private boolean ejectComplete;
	protected boolean bHasBoosters;
	protected long boosterFireOutTime;
	private SoundFX fxSPO15;
	private Sample smplSPO15;
	private boolean SPO15SoundPlaying;
	private boolean bRadarWarning;
	protected boolean bHasSK1Seat;
	private static Actor hunted = null;
	public int k14Mode;
	public int k14WingspanType;
	public float k14Distance;
	public static boolean bChangedPit = false;
	public boolean bToFire;
	static {
		Class class1 = com.maddox.il2.objects.air.Su_25X.class;
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
	}
}