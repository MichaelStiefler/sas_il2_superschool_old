// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.10.2019 16:02:38
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   RE_2002m.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            RE_2002xyz, PaintSchemeBMPar09, PaintSchemeFMPar02, TypeDiveBomber, 
//            Aircraft, Cockpit, NetAircraft

public class RE_2002m extends com.maddox.il2.objects.air.RE_2002xyz
    implements com.maddox.il2.objects.air.TypeDiveBomber
{

    public RE_2002m()
    {
        canopyF = 0.0F;
        tiltCanopyOpened = false;
        slideCanopyOpened = false;
        blisterRemoved = false;
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -30F * f, 0.0F);
        arrestor = f;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(tiltCanopyOpened && !blisterRemoved && super.FM.getSpeed() > 75F)
            doRemoveBlister1();
    }

    private final void doRemoveBlister1()
    {
        blisterRemoved = true;
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            com.maddox.il2.objects.Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            com.maddox.JGP.Vector3d vector3d = new Vector3d();
            vector3d.set(((com.maddox.il2.fm.FlightModelMain) (super.FM)).Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public void moveCockpitDoor(float f)
    {
        if(f > canopyF)
        {
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() < 5F || tiltCanopyOpened)
            {
                tiltCanopyOpened = true;
                hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            } else
            {
                slideCanopyOpened = true;
                resetYPRmodifier();
                com.maddox.il2.objects.air.Aircraft.xyz[0] = -0.01F;
                com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.3F);
                hierMesh().chunkSetLocate("Blister4L_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
                com.maddox.il2.objects.air.Aircraft.xyz[0] = 0.01F;
                hierMesh().chunkSetLocate("Blister4R_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
            }
        } else
        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() < 5F && !slideCanopyOpened || tiltCanopyOpened)
        {
            hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if(super.FM.getSpeed() > 50F && f < 0.6F && !blisterRemoved)
                doRemoveBlister1();
            if(f == 0.0F)
                tiltCanopyOpened = false;
        } else
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.3F);
            hierMesh().chunkSetLocate("Blister4L_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
            hierMesh().chunkSetLocate("Blister4R_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
            if(f == 0.0F)
                slideCanopyOpened = false;
        }
        canopyF = f;
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
        {
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public boolean typeDiveBomberToggleAutomation()
    {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset()
    {
    }

    public void typeDiveBomberAdjAltitudePlus()
    {
    }

    public void typeDiveBomberAdjAltitudeMinus()
    {
    }

    public void typeDiveBomberAdjVelocityReset()
    {
    }

    public void typeDiveBomberAdjVelocityPlus()
    {
    }

    public void typeDiveBomberAdjVelocityMinus()
    {
    }

    public void typeDiveBomberAdjDiveAngleReset()
    {
    }

    public void typeDiveBomberAdjDiveAnglePlus()
    {
    }

    public void typeDiveBomberAdjDiveAngleMinus()
    {
    }

    public void typeDiveBomberReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted)
        throws java.io.IOException
    {
    }

    public void typeDiveBomberReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput)
        throws java.io.IOException
    {
    }

    protected void mydebug(java.lang.String s)
    {
        java.lang.System.out.println(s);
    }

    public void update(float f)
    {
        super.update(f);
        if(FM.CT.getArrestor() > 0.2F)
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                float f1 = com.maddox.il2.objects.air.RE_2002m.cvt(FM.Gears.arrestorVAngle, -26F, 11F, 1.0F, 0.0F);
                arrestor = 0.8F * arrestor + 0.2F * f1;
                moveArrestorHook(arrestor);
            } else
            {
                float f2 = (-42F * FM.Gears.arrestorVSink) / 30F;
                if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
                    com.maddox.il2.engine.Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                    f2 = 0.0F;
                if(f2 > 0.0F)
                    arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
                else
                    arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
                if(arrestor < 0.0F)
                    arrestor = 0.0F;
                else
                if(arrestor > 1.0F)
                    arrestor = 1.0F;
                moveArrestorHook(arrestor);
            }
    }

    static java.lang.Class _mthclass$(java.lang.String s)
    {
        try
        {
            return java.lang.Class.forName(s);
        }
        catch(java.lang.ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    protected float arrestor;
    public float canopyF;
    private boolean tiltCanopyOpened;
    private boolean slideCanopyOpened;
    private boolean blisterRemoved;

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "RE.2002");
        com.maddox.rts.Property.set(class1, "meshName_it", "3DO/Plane/RE-2002m(it)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/RE-2002m(multi)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        com.maddox.rts.Property.set(class1, "yearService", 1943F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1948.5F);
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.Cockpit_RE2002m.class
        });
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/RE-2002m.fmd:RE2002M4111FM");
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.9119F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 3, 3, 3, 3, 3, 9, 
            3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalTorp01", "_ExternalDev01", 
            "_ExternalBomb05"
        });
    }
}