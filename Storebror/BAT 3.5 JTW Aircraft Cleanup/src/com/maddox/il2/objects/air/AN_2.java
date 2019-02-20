package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class AN_2 extends Scheme1
    implements TypeBomber, TypeScout
{

    public AN_2()
    {
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 2:
            if(this.FM.turret.length > 0)
                this.FM.turret[0].setHealth(f);
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
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;
        }
    }

    protected void moveFan(float f)
    {
        if(bDynamoOperational)
        {
            pk = Math.abs((int)(this.FM.Vwld.length() / 14D));
            if(pk >= 1)
                pk = 1;
        }
        if(bDynamoRotary != (pk == 1))
        {
            bDynamoRotary = pk == 1;
            hierMesh().chunkVisible("Cart_D0", !bDynamoRotary);
            hierMesh().chunkVisible("CartRot_D0", bDynamoRotary);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 17.987F) % 360F : (float)((double)dynamoOrient - this.FM.Vwld.length() * 1.5444015264511108D) % 360F;
        hierMesh().chunkSetAngles("Cart_D0", 0.0F, dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Turret"))
            this.FM.turret[0].bIsOperable = false;
        if(shot.chunkName.startsWith("Tail1") && ((Tuple3d) (Aircraft.Pd)).z > 0.5D && ((Tuple3d) (Aircraft.Pd)).x > -6D && ((Tuple3d) (Aircraft.Pd)).x < -4.9499998092651367D && World.Rnd().nextFloat() < 0.5F)
            this.FM.AS.hitPilot(shot.initiator, 2, (int)(shot.mass * 1000F * 0.5F));
        if(shot.chunkName.startsWith("CF") && ((Tuple3d) (Aircraft.v1)).x < -0.20000000298023224D && ((Tuple3d) (Aircraft.Pd)).x > 2.5999999046325684D && ((Tuple3d) (Aircraft.Pd)).z > 0.73500001430511475D && World.Rnd().nextFloat() < 0.178F)
            this.FM.AS.hitPilot(shot.initiator, ((Tuple3d) (Aircraft.Pd)).y > 0.0D ? 0 : 1, (int)(shot.mass * 900F));
        if(shot.chunkName.startsWith("WingLIn") && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 2.0999999046325684D)
            this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(0, (int)(shot.mass * 30F)));
        if(shot.chunkName.startsWith("WingRIn") && Math.abs(((Tuple3d) (Aircraft.Pd)).y) < 2.0999999046325684D)
            this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int)(shot.mass * 30F)));
        super.msgShot(shot);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        if(af[0] < -50F)
        {
            af[0] = -50F;
            flag = false;
        } else
        if(af[0] > 50F)
        {
            af[0] = 50F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if(f < 20F)
        {
            if(af[1] < -1F)
            {
                af[1] = -1F;
                flag = false;
            }
        } else
        if(af[1] < -5F)
        {
            af[1] = -5F;
            flag = false;
        }
        if(af[1] > 45F)
        {
            af[1] = 45F;
            flag = false;
        }
        return flag;
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        if(regiment.country().equals(PaintScheme.countryHungary))
            return PaintScheme.countryHungary + "_";
        if(regiment.country().equals(PaintScheme.countryRomania))
            return PaintScheme.countryRomania + "_";
        else
            return "";
    }

    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;

    static 
    {
        Class class1 = AN_2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "meshName", "3do/plane/An-2/hier.him");
        Property.set(class1, "iconFar_shortClassName", "An-2");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-52_3mg4e.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitJU524E.class, CockpitJU525E_GunnerOpen.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_BombSpawn01"
        });
    }
}
