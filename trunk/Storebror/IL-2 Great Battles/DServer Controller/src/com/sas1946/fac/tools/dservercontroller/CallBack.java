package com.sas1946.fac.tools.dservercontroller;

public abstract class CallBack<TRet, TArg1, TArg2> {
    public abstract TRet call(TArg1 val1, TArg2 val2);
}
