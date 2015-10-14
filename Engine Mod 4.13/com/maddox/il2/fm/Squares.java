// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Squares.java

package com.maddox.il2.fm;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

import java.io.PrintStream;

// Referenced classes of package com.maddox.il2.fm:
//            Controls

public class Squares
{

    public Squares()
    {
        dragParasiteCx = 0.0F;
        dragAirbrakeCx = 0.0F;
        dragChuteCx = 1.5F;
        dragFuselageCx = 0.0F;
        dragProducedCx = 0.0F;
        toughness = new float[44];
        eAbsorber = new float[44];
    }

    public void load(SectFile sectfile)
    {
        String s1 = "Zero Square processed from " + sectfile.toString();
        String s = "Squares";
        float f = sectfile.get(s, "Wing", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        squareWing = f;
        f = sectfile.get(s, "Aileron", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        squareAilerons = f;
        f = sectfile.get(s, "Flap", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        squareFlaps = f;
        f = sectfile.get(s, "Stabilizer", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        liftStab = f;
        f = sectfile.get(s, "Elevator", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        squareElevators = f;
        f = sectfile.get(s, "Keel", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        liftKeel = f;
        f = sectfile.get(s, "Rudder", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        squareRudders = f;
        f = sectfile.get(s, "Wing_In", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        liftWingLIn = liftWingRIn = f;
        f = sectfile.get(s, "Wing_Mid", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        liftWingLMid = liftWingRMid = f;
        f = sectfile.get(s, "Wing_Out", 0.0F);
        if(f == 0.0F)
            throw new RuntimeException(s1);
        liftWingLOut = liftWingROut = f;
        f = sectfile.get(s, "AirbrakeCxS", -1F);
        if(f == -1F)
            throw new RuntimeException(s1);
        dragAirbrakeCx = f;
        f = sectfile.get("Params", "SpinCxLoss", -1F);
        if(f == -1F)
            throw new RuntimeException(s1);
        spinCxloss = f;
        f = sectfile.get("Params", "SpinCyLoss", -1F);
        if(f == -1F)
            throw new RuntimeException(s1);
        spinCyloss = f;
        for(int i = 0; i < 8; i++)
            dragEngineCx[i] = 0.0F;

        s = "Toughness";
        for(int j = 0; j < 44; j++)
            toughness[j] = (float)sectfile.get(s, Aircraft.partNames()[j], 100) * 0.0001F;

        toughness[43] = 3.402823E+038F;
        float f1 = (2.0F * (liftWingLIn + liftWingLMid + liftWingLOut)) / (squareWing + 0.01F);
        if(f1 < 0.9F || f1 > 1.1F)
        {
            if(World.cur().isDebugFM())
                System.out.println("Error in flightmodel " + sectfile.toString() + ": (wing square) != (sum of squares*2)");
            if(f1 > 1.0F)
                squareWing = 2.0F * (liftWingLIn + liftWingLMid + liftWingLOut);
            else
                liftWingLIn = liftWingLMid = liftWingLOut = liftWingRIn = liftWingRMid = liftWingROut = 0.166667F * squareWing;
        }
    }

    public float getToughness(int i)
    {
        return toughness[i];
    }

    public void computeParasiteDrag(Aircraft aircraft, Controls controls, BulletEmitter abulletemitter[][])
    {
        dragParasiteCx = 0.0F;
        for(int i = 0; i < abulletemitter.length; i++)
        {
            if(abulletemitter[i] == null || abulletemitter[i].length <= 0)
                continue;
            for(int j = 0; j < abulletemitter[i].length; j++)
            {            	
            	if(abulletemitter[i][j] instanceof BombGunNull)
            		continue;
            	else
                {
	            	if(((abulletemitter[i][j] instanceof BombGun) || (abulletemitter[i][j] instanceof RocketBombGun) || (abulletemitter[i][j] instanceof TorpedoGun)) && abulletemitter[i][j].haveBullets() && abulletemitter[i][j].getHookName().startsWith("_External") && dragParasiteCx < 0.704F)
	                {
	            		float f = 0.125F;           			            				            	
	                    if(abulletemitter[i][j] instanceof FuelTankGun)
	                        f = 0.05F;
	                    Class class2 = (Class)Property.value(abulletemitter[i][j].getClass(), "bulletClass", null);
	                    float f2 = Property.floatValue(class2, "kalibr", 0.0F);
	                    float f4 = (float)(3.1415926535897931D * (double)f2 * (double)f2 * (double)f);
	                    dragParasiteCx += f4;
	                }
	                if((abulletemitter[i][j] instanceof RocketGun) && abulletemitter[i][j].haveBullets())
	                {
	                    Class class1 = (Class)Property.value(abulletemitter[i][j].getClass(), "bulletClass", null);
	                    if(com.maddox.il2.objects.weapons.Missile.class.isAssignableFrom(class1))
	                        continue;
	                    float f1 = Property.floatValue(class1, "kalibr", 0.0F);
	                    float f3 = (float)(3.1415926535897931D * (double)f1 * (double)f1 * 0.11999999731779099D);
	                    dragParasiteCx += f3;
	                }
	                if(!(abulletemitter[i][j] instanceof Pylon) || (abulletemitter[i][j] instanceof PylonRO_82_1) || (abulletemitter[i][j] instanceof PylonRO_82_3) || (abulletemitter[i][j] instanceof PylonPE8_FAB100) || (abulletemitter[i][j] instanceof PylonPE8_FAB250) || (abulletemitter[i][j] instanceof PylonP38RAIL3FL) || (abulletemitter[i][j] instanceof PylonP38RAIL3FR) || (abulletemitter[i][j] instanceof PylonP38RAIL3WL) || (abulletemitter[i][j] instanceof PylonP38RAIL3WR) || (abulletemitter[i][j] instanceof PylonP38RAIL5) || (abulletemitter[i][j] instanceof PylonP38RAILS) || (abulletemitter[i][j] instanceof PylonF6FPLN2) || (abulletemitter[i][j] instanceof PylonMG15120Internal))
	                    continue;
	                dragParasiteCx += 0.035F;
	                if((abulletemitter[i][j] instanceof PylonHS129BK75) || (abulletemitter[i][j] instanceof PylonHS129BK37))
	                    dragParasiteCx += 0.45F;
	                if(abulletemitter[i][j] instanceof PylonRO_WfrGr21)
	                    dragParasiteCx += 0.015F;
	                if(abulletemitter[i][j] instanceof PylonRO_WfrGr21Dual)
	                    dragParasiteCx += 0.02F;
	                if(abulletemitter[i][j] instanceof PylonVAP250)
	                    dragParasiteCx += 0.02F;
                }
            }

        }
               
        dragParasiteCx += 0.02F * controls.getRefuel();
        dragParasiteCx += 0.02F * controls.getCockpitDoor();
        if(controls.bHasBayDoorControl)
            dragParasiteCx += 0.03F * controls.getBayDoor();
        dragParasiteCx += aircraft.getExtraParasiteDrag();
        
        if ((aircraft instanceof TypeFastJet || aircraft instanceof TypeSupersonic) && dragParasiteCx > 3.0F)
        	dragParasiteCx = 3.0F;        
    }

    public float squareWing;
    public float squareAilerons;
    public float squareElevators;
    public float squareRudders;
    public float squareFlaps;
    public float liftWingLIn;
    public float liftWingLMid;
    public float liftWingLOut;
    public float liftWingRIn;
    public float liftWingRMid;
    public float liftWingROut;
    public float liftStab;
    public float liftKeel;
    public float dragEngineCx[] = {
        0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F
    };
    public float dragParasiteCx;
    public float dragAirbrakeCx;
    public float dragFuselageCx;
    public float dragChuteCx;
    public float dragProducedCx;
    float spinCxloss;
    float spinCyloss;
    public float toughness[];
    public float eAbsorber[];
    public final float dragSmallHole = 0.06F;
    public final float dragBigHole = 0.12F;
    public final float wingSmallHole = 0.4F;
    public final float wingBigHole = 0.8F;
}
