// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 18-Dec-12 12:01:44 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ControlInfo.java

package com.maddox.sound;

import java.io.PrintStream;
import java.util.HashMap;

// Referenced classes of package com.maddox.sound:
//            BaseObject, SoundControl

public class ControlInfo extends BaseObject
{

    protected ControlInfo(String s, int i, Class class1)
    {
        cc = i;
        cls = class1;
        map.put(s, this);
    }

    protected static SoundControl get(String s, int i)
    {
    	try{
    		if(i == 0)
    			return null;
    		SoundControl soundcontrol;
    		ControlInfo controlinfo = (ControlInfo)map.get(s);
    		soundcontrol = (SoundControl)controlinfo.cls.newInstance();
    		soundcontrol.init(controlinfo.cc, i);
    		return soundcontrol;
    	}
    	catch(Exception exception){
    		System.out.println("Cannot create sound control " + s);
    		return null;
    	}
    }

    public static final int CC_SPEED = 64;
    public static final int CC_ANGLE = 65;
    public static final int CC_DIST = 66;
    public static final int CC_ENV = 67;
    public static final int CC_RPM = 100;
    public static final int CC_DAMAGE = 101;
    public static final int CC_DIVING = 102;
    public static final int CC_FLATTER = 103;
    public static final int CC_BRAKE = 104;
    public static final int CC_GEAR = 105;
    public static final int CC_START = 106;
    public static final int CC_LOAD = 107;
    public static final int CC_MOTOR_LD = 108;
    public static final int CC_PROP_LD = 109;
    public static final int CC_DOOR = 110;
    public static final int CC_RELSPEED = 111;
    public static final int CC_MOTOR_STAGE = 112;
    public static final int CC_BOMB_SPEED = 200;
    public static final int CC_BOMB_TIME = 201;
    public static final int CC_MASSA = 202;
    public static final int CC_EMIT_RUN = 500;
    public static final int CC_EMIT_SPEED = 501;
    public static final int CC_MOD = 1000;
    public static final int CC_THRUST = 1001;
    protected int cc;
    protected Class cls;
    protected static HashMap map = new HashMap();

    static 
    {
        new ControlInfo("speed", 64, com.maddox.sound.BoundsControl.class);
        new ControlInfo("angle", 65, com.maddox.sound.BoundsControl.class);
        new ControlInfo("dist", 66, com.maddox.sound.BoundsControl.class);
        new ControlInfo("env", 67, com.maddox.sound.EnvControl.class);
        new ControlInfo("rpm", 100, com.maddox.sound.BoundsControl.class);
        new ControlInfo("damage", 101, com.maddox.sound.BoundsControl.class);
        new ControlInfo("diving", 102, com.maddox.sound.BoundsControl.class);
        new ControlInfo("flatter", 103, com.maddox.sound.BoundsControl.class);
        new ControlInfo("brake", 104, com.maddox.sound.BoundsControl.class);
        new ControlInfo("gear", 105, com.maddox.sound.BoundsControl.class);
        new ControlInfo("start", 106, com.maddox.sound.BoundsControl.class);
        new ControlInfo("load", 107, com.maddox.sound.BoundsControl.class);
        new ControlInfo("motorld", 108, com.maddox.sound.BoundsControl.class);
        new ControlInfo("propld", 109, com.maddox.sound.BoundsControl.class);
        new ControlInfo("door", 110, com.maddox.sound.BoundsControl.class);
        new ControlInfo("relspeed", 111, com.maddox.sound.BoundsControl.class);
        new ControlInfo("mostage", 112, com.maddox.sound.BoundsControl.class);
        new ControlInfo("bombspeed", 200, com.maddox.sound.BoundsControl.class);
        new ControlInfo("bombtime", 201, com.maddox.sound.BoundsControl.class);
        new ControlInfo("massa", 202, com.maddox.sound.BoundsControl.class);
        new ControlInfo("mod", 1000, com.maddox.sound.ModControl.class);
        new ControlInfo("thrust", 1001, com.maddox.sound.BoundsControl.class);
    }
}