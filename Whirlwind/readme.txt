******************************************************************************************************************
*                                   Installation Instructions:                                                   *
******************************************************************************************************************

1.) Extract to contents of this mod to the "#SAS" folder of your Modact 5.x game.

2.) Open air.ini (in Modact 5 you find this file at "<Your Game Folder>\#SAS\STD\com\maddox\il2\objects\air.ini").
    Find the following lines:

WellingtonMk3     air.Wellington3  				                      gb01  SUMMER 

3.) Behind the above mentioned line from step 2.) add the following line:

WhirlwindMkII     air.WhirlwindA 1                      NOINFO  gb01  SUMMER

4.) Scroll down for further optional .properties file modifications.










******************************************************************************************************************
*                                                  Contents:                                                     *
******************************************************************************************************************

Wirlwind Mk. II including 250lb, 500lb, Cluster and Rocket ordnance options.

******************************************************************************************************************
*                                                  Credits:                                                      *
******************************************************************************************************************

Credits:
- Created by 101tfs
- Additional Mod works by many other SAS Superschool Members





















******************************************************************************************************************
*                                  Optional properties modifications:                                            *
******************************************************************************************************************

1.) Open plane.properties (Modact 5, can be found at "<Your Game Folder>\#SAS\STD\i18n\plane.properties")
    or plane_ru.properties (other game versions, officially unsupported).

2.) At the end of the file, add the following lines:
   
WhirlwindMkII        Whirlwind Mk. II, 1942

3.) Open weapons.properties (Modact 5, can be found at "<Your Game Folder>\#SAS\STD\i18n\weapons.properties")
    or weapons_ru.properties (other game versions, officially unsupported).
    
4.) At the end of the file, add the following lines:

#####################################################################
# WhirlwindMkII
#####################################################################
WhirlwindMkII.default                           Default
WhirlwindMkII.2x250lb                           2x250 lb. Bombs
WhirlwindMkII.2x500lb                           2x500 lb. Bombs
WhirlwindMkII.2xM26                             2xM26 Cluster Bombs
WhirlwindMkII.8x5                               8x60lb Rockets
WhirlwindMkII.none                              Empty
