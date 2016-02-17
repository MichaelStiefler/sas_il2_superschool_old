// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 17/10/2015 02:31:35 p.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
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
