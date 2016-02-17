// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 03/11/2015 04:00:57 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TypeAIM9Carrier.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.il2.engine.Actor;

public interface TypeAIM9Carrier
{

    public abstract Actor getMissileTarget();

    public abstract boolean hasMissiles();

    public abstract void shotMissile();

    public abstract int getMissileLockState();

    public abstract Point3f getMissileTargetOffset();
}
