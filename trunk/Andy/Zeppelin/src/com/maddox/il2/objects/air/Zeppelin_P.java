// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 26.02.2019 06:11:42
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Zeppelin_P.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgOutput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import com.maddox.sas1946.il2.util.Reflection;

// Referenced classes of package com.maddox.il2.objects.air:
//            Dirigeable, PaintSchemeBMPar00, Aircraft, NetAircraft

public class Zeppelin_P extends com.maddox.il2.objects.air.Dirigeable
{

    public Zeppelin_P()
    {
        fSightCurAltitude = 300F;
        fSightCurSpeed = 50F;
    }


    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        FM.CT.bHasBombSelect = true;
        if (this.thisWeaponsName.equals("none")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("Pilot9_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Pilot6_D0", false);
            return;
        } else {
            return;
        }
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        float f2 = java.lang.Math.abs(f);
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            if(f1 < -47F)
            {
                f1 = -47F;
                flag = false;
            }
            if(f1 > 47F)
            {
                f1 = 47F;
                flag = false;
            }
            if(f2 < 147F)
            {
                if(f1 < 0.5964912F * f2 - 117.6842F)
                {
                    f1 = 0.5964912F * f2 - 117.6842F;
                    flag = false;
                }
            } else
            if(f2 < 157F)
            {
                if(f1 < 0.3F * f2 - 74.1F)
                {
                    f1 = 0.3F * f2 - 74.1F;
                    flag = false;
                }
            } else
            if(f1 < 0.2173913F * f2 - 61.13044F)
            {
                f1 = 0.2173913F * f2 - 61.13044F;
                flag = false;
            }
            if(f2 >= 110F)
                if(f2 < 115F)
                {
                    if(f1 < -5F && f1 > -20F)
                        flag = false;
                } else
                if(f2 < 160F)
                {
                    if(f1 < -5F)
                        flag = false;
                } else
                if(f1 < 15F)
                    flag = false;
            break;

        case 1: // '\001'
            if(f1 < -47F)
            {
                f1 = -47F;
                flag = false;
            }
            if(f1 > 47F)
            {
                f1 = 47F;
                flag = false;
            }
            if(f < -38F)
            {
                if(f1 < -32F)
                {
                    f1 = -32F;
                    flag = false;
                }
            } else
            if(f < -16F)
            {
                if(f1 < 0.5909091F * f - 9.545455F)
                {
                    f1 = 0.5909091F * f - 9.545455F;
                    flag = false;
                }
            } else
            if(f < 35F)
            {
                if(f1 < -19F)
                {
                    f1 = -19F;
                    flag = false;
                }
            } else
            if(f < 44F)
            {
                if(f1 < -3.111111F * f + 89.88889F)
                {
                    f1 = -3.111111F * f + 89.88889F;
                    flag = false;
                }
            } else
            if(f < 139F)
            {
                if(f1 < -47F)
                {
                    f1 = -47F;
                    flag = false;
                }
            } else
            if(f < 150F)
            {
                if(f1 < 1.363636F * f - 236.5455F)
                {
                    f1 = 1.363636F * f - 236.5455F;
                    flag = false;
                }
            } else
            if(f1 < -32F)
            {
                f1 = -32F;
                flag = false;
            }
            if(f < -175.7F)
            {
                if(f1 < 80.8F)
                    flag = false;
                break;
            }
            if(f < -167F)
            {
                if(f1 < 0.0F)
                    flag = false;
                break;
            }
            if(f < -124.8F)
            {
                if(f1 < -22.8F)
                    flag = false;
                break;
            }
            if(f < -82F)
            {
                if(f1 < -16F)
                    flag = false;
                break;
            }
            if(f < 24F)
            {
                if(f1 < 0.0F)
                    flag = false;
                break;
            }
            if(f < 32F)
            {
                if(f1 < -8.3F)
                    flag = false;
                break;
            }
            if(f < 80F)
            {
                if(f1 < 0.0F)
                    flag = false;
                break;
            }
            if(f < 174F)
            {
                if(f1 < 0.5F * f - 87F)
                    flag = false;
                break;
            }
            if(f < 178.7F)
            {
                if(f1 < 0.0F)
                    flag = false;
                break;
            }
            if(f1 < 80.8F)
                flag = false;
            break;

        case 2: // '\002'
            if(f1 < -47F)
            {
                f1 = -47F;
                flag = false;
            }
            if(f1 > 47F)
            {
                f1 = 47F;
                flag = false;
            }
            if(f < -68F)
            {
                if(f1 < -32F)
                {
                    f1 = -32F;
                    flag = false;
                }
            } else
            if(f < -22F)
            {
                if(f1 < 0.5347826F * f + 4.365217F)
                {
                    f1 = 0.5347826F * f + 4.365217F;
                    flag = false;
                }
            } else
            if(f < 27F)
            {
                if(f1 < -0.3387755F * f - 14.85306F)
                {
                    f1 = -0.3387755F * f - 14.85306F;
                    flag = false;
                }
            } else
            if(f < 40F)
            {
                if(f1 < -1.769231F * f + 23.76923F)
                {
                    f1 = -1.769231F * f + 23.76923F;
                    flag = false;
                }
            } else
            if(f < 137F)
            {
                if(f1 < -47F)
                {
                    f1 = -47F;
                    flag = false;
                }
            } else
            if(f < 152F)
            {
                if(f1 < 1.0F * f - 184F)
                {
                    f1 = 1.0F * f - 184F;
                    flag = false;
                }
            } else
            if(f1 < -32F)
            {
                f1 = -32F;
                flag = false;
            }
            if(f < -172F)
            {
                if(f1 < 2.0F)
                    flag = false;
                break;
            }
            if(f < -123F)
            {
                if(f1 < 30F)
                    flag = false;
                break;
            }
            if(f < -102F)
            {
                if(f1 < 0.0F)
                    flag = false;
                break;
            }
            if(f < -36F)
            {
                if(f1 < -9F)
                    flag = false;
                break;
            }
            if(f < -5.1F)
            {
                if(f1 < 0.0F)
                    flag = false;
                break;
            }
            if(f < -1.2F)
            {
                if(f1 < 24.5F)
                    flag = false;
                break;
            }
            if(f < 62F)
            {
                if(f1 < -0.7436709F * f - 0.892496F)
                {
                    f1 = -0.7436709F * f - 0.892496F;
                    flag = false;
                }
                break;
            }
            if(f < 103F)
            {
                if(f1 < -47F)
                    flag = false;
                break;
            }
            if(f1 < 0.0F)
                flag = false;
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            for(int i = 0; i < 4; i++)
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEngineStates[i] > 3 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[i].getReadyness() < 0.1F)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.repairEngine(i);

            for(int j = 0; j < 4; j++)
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateTankStates[j] > 3 && (float)((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astatePilotStates[4] < 50F && (float)((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astatePilotStates[7] < 50F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.repairTank(j);

        }
    }

    public void update(float f)
    {
        super.update(f);
        hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.gWheelAngles[0], 0.0F);
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.gWheelAngles[1], 0.0F);
        this.FM.M.mass = this.FM.M.massEmpty;
Reflection.setFloat(this.FM.M, "parasiteMass", 0F);
Reflection.setFloat(this.FM.M, "parasiteJx", 0F);
Reflection.setFloat(this.FM.M, "pylonCoeff", 0F);
Reflection.setFloat(this.FM.M, "pylonCoeffB", 0F);
Reflection.setFloat(this.FM.M, "pylonCoeffR", 0F);
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
        fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 50F;
        if(fSightCurAltitude > 5000F)
            fSightCurAltitude = 5000F;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new java.lang.Object[] {
            new Integer((int)fSightCurAltitude)
        });
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 50F;
        if(fSightCurAltitude < 300F)
            fSightCurAltitude = 300F;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new java.lang.Object[] {
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
        if(fSightCurSpeed > 350F)
            fSightCurSpeed = 350F;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new java.lang.Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 5F;
        if(fSightCurSpeed < 50F)
            fSightCurSpeed = 50F;
        com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new java.lang.Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted)
        throws java.io.IOException
    {
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeFloat(fSightCurSpeed);
    }

    public void typeBomberReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput)
        throws java.io.IOException
    {
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = netmsginput.readFloat();
    }


    public float fSightCurAltitude;
    public float fSightCurSpeed;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Zeppelin_P.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Zeppelin");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Zeppelin_P/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1916F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1925.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/ZeppelinP.fmd:ZEP_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitZeppelin_P.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 10, 11, 11, 12, 12, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03"
        });
    }
}