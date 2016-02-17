// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 17/10/2015 02:32:36 p.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AIM9BNetSafeLog.java

package com.maddox.il2.game;

import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;

// Referenced classes of package com.maddox.il2.game:
//            HUD

public class AIM9BNetSafeLog
{

    private AIM9BNetSafeLog()
    {
    }

    private static boolean isLocalActor(Actor theActor)
    {
        return theActor == World.getPlayerAircraft() && !theActor.isNetMirror();
    }

    public static void log(Actor logActor, String logLine)
    {
        if(isLocalActor(logActor))
            HUD.log(logLine);
    }

    public static void training(Actor logActor, String logLine)
    {
        if(isLocalActor(logActor))
            HUD.log(logLine);
    }

    public static void type(Actor logActor, String logLine)
    {
        if(isLocalActor(logActor))
            EventLog.type(logLine);
    }
}
