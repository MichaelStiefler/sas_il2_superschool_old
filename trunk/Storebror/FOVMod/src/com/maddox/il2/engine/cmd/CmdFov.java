// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   CmdFov.java

package com.maddox.il2.engine.cmd;

import java.util.Map;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;

public class CmdFov extends Cmd
{
    public static int hudLogFOVId = -1;
    
    public Object exec(CmdEnv cmdenv, Map map)
    {
        if(map.containsKey("_$$"))
        {
            float f = Cmd.arg(map, "_$$", 0, 70F, 5F, 175F);
            float fCurFOV = Main3D.FOVX;
            
            int iMaxFovOverride = 90;
            int iMinFovOverride = 30;
            
            int i = RendersMain.width();
            int j = RendersMain.height();
            if(RendersMain.isSaveAspect() && Config.cur.windowsWideScreenFoV && (i * 3) / 4 != j && i >= j)
            {
                float fOverrideFactor = 1.75F - (float)j / (float)i;
                iMaxFovOverride = (int)((float)iMaxFovOverride * fOverrideFactor);
                iMinFovOverride = (int)((float)iMinFovOverride * fOverrideFactor);
            }
            
            if (Main3D.FOVX > (float)iMaxFovOverride - 2.5F) {
            	if ((int)f == iMaxFovOverride - 5) {
                    f = Main3D.FOVX - 5F;
            	} else if ((int)f == iMaxFovOverride) {
                    f = Main3D.FOVX + 5F;
            	}
            } else if (Main3D.FOVX < (float)iMinFovOverride + 2.5F) {
            	if ((int)f == iMinFovOverride) {
                    f = Main3D.FOVX - 5F;
            	} else if ((int)f == iMinFovOverride + 5) {
                    f = Main3D.FOVX + 5F;
            	}
            }
            if (f < 5F) f = 5F;
            if (f > 175F) f = 175F;
            Camera3D camera3d1 = (Camera3D)Actor.getByName("camera");
            camera3d1.set(f);
            Render render = (Render)Actor.getByName("renderCockpit");
            if(render != null)
            {
                Camera3D camera3d2 = (Camera3D)render.getCamera();
                camera3d2.set(f);
                if (Math.abs(f-fCurFOV) > 4.5F) {
                	if (Config.cur.ini.get("MODS", "FOVLog", 0) != 0) {
	                  if (hudLogFOVId == -1) hudLogFOVId = com.maddox.il2.game.HUD.makeIdLog();
	                  com.maddox.il2.game.HUD.log(
	                          hudLogFOVId,
	                          "FOV {0}°",
	                          new Object[] {new Integer((int)f)});
                	}
                }
            }
            Main3D.FOVX = f;
        } else
        {
            Camera3D camera3d = (Camera3D)Actor.getByName("camera");
            INFO_HARD("Camera fov: ".concat(String.valueOf(camera3d.FOV())));
        }
        return "OK";
    }

    public CmdFov()
    {
        super._properties.put("NAME", "fov");
        super._levelAccess = 1;
    }
}
