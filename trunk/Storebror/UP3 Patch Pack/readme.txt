UP3 RC4 Patch Pack v1.06 v2

Contents:
- All fixes listed below
- All feature enhancements listed below
- UP3 RC4 without obfuscation
- SAS Common Utils v1.09

Included feature enhancements:
- Replication of aircraft control surfaces movement over the net and through NTRK recordings
- Replication of pilot head movement over the net and through NTRK recordings
- Animated AI pilot heads, according to pilot's task (watch wing leader, watch enemy etc.)
- Screenshots taken in native code, much faster than before
- "Stabs4All" included, can be activated through conf.ini (*)
- New DiffFM included, capable of reading different FM file versions, less intrusive
- I-16 Planes can hook to TB-3 SPB bombers in flight.
  Hook Parameters on Ground: Distance to hook point max. 5m
  Hook Parameters in Air: Distance to hook point max. 15m, speed difference max. 30km/h, flight vector difference max. 25 degrees
- G4M-2 and Ohka flying bomb can be hooked to each other on ground now (max. distance to hook point: 50m)
- Ju-88 Mistel and 109/190 fighter aircraft can be hooked to each other on ground now (max. distance to hook point: 50m) (needs further testing)
- Torpedo drop parameters shown in command menu (default: Tab key)
- Torpedo water impact values shown in HUD log on impact
- Implemented "smooth" limits for torpedo drop parameters.
  When the drop limits (entry speed, impact angle) are exceeded, the torpedo doesn't break immediately,
  but with a probability according to the proportion of the exceedance vs. the original limits
- WYSIWYG:
  New "smooth" Torpedo Engagement Limits,
  Random Bomb, Rocket, Ordnance drop spread,
  bouncing bombs on water (skip-bombing),
  Detonation Delay (Bomb fuze) settings and
  Random Explosion Radii get replicated over the net, so that everyone sees what you see
  (and your NTRK playbacks always look the same like original as well)
- AI Gunners are slightly better again when being on Veteran or Ace level

(*) To activate the Stabilizers for all Aircraft ("Stabs4All"), add the following entry
    to your conf.ini file:
    [MODS]
    Stabs4All=1

Included fixes:
- Bf 109 Series Cockpit Code fix
- Bf 109 G14 Hotfix
- Early 109 Hotfix
- Invincible 20mm Flak Hotfix
- Jet Booster Hotfix
- Ju-87C Hotfix
- Net Max Lag Hotfix
- Objects Hotfix Pack 01
- SM.79 and Cant Z.1007 Gunner Hotfix
- Sniper Gunner Hotfix
- Swordfish Spawn Hotfix
- Typhoon Update Fix (new 3D, fully skinnable)
- Fixed very old online bug that caused wrong visual animation of engine states
  (with 2 or more engines)
- Fixed "Stabs4All" bug preventing that functionality to work on most systems
- Fixed Cockpit bug which showed wrong meshes after repairing the cockpit from
  damage using RRR function, applies to Bf 109 B/C/D/E/F, Fi-156, Ju-87D/G
- Fixed Head Movement bug where, when the head is turned to either side, pitching
  the head up/down got misinterpreted to tilting it left/right when viewed from
  externals
- Fixed a Mission Builder Bug where erratically airspawns were created when the
  mission contained "Standalone" Home Bases, but no single "Standalone" Spawn Point
- Fixed pairwise shooting X-4 missiles on Me 410 D
- Logging relieved from surplus content
- TBM-1 Torpedo loadout fixed
- Me-163 Flight Model from 4.13 implemented to get rid of the lame duck FM which existed up to 4.10
- Bomb rotation axis fixed, was set to a Null-Vector in UP3 (??)
- Replaced the partly predictable and reproduceable "pseudo" random number generator static initialization, now "Random" _IS_ random.
- Initial static random "0" seed for bombs fixed
- Pe-2 wounded gunner bug fixed
- Further fix for gunner position switch bug, reducing log output and defaults to player previous army setting now
- Missing Dead Meshes fixed for following pacific buildings: Flag_ArmeeRouge, Flag_LW, Flag_RAF, Flag_US, Flag_VVS
- Missing Dead Meshes fixed for Train "Platform1_Dmg"
- Disappearing Bombs/Torpedos/Rockets bug fixed
  (this was an old IL-2 Core game bug where Bombs/Torpedos/Rockets would simply disappear instead of being dropped/shot online or during NTRK playback)
- VAP bomb (IL-2 incendiary cluster bomb) rearming fixed, based on hotfix by SAS~Skylla from 22 Sep 2015
- FM2 rocket trigger fixed, now all rockets can be fired with rocket trigger key (previously the last pair had to be fired using bomb trigger key)
- Invincible Opel Maultier Flak fixed
- Do-217 crew position locked up in belly gunner position fixed
- Pairwise Hs-293 and Fritz-X drop on Do-217K-2 fixed
- SBD gunner envelope limits fixed
- B-29 gunner positions fixed
- Fokker DXXI compass fixed
- H8K outer frame parts appearing in cockpit view when plane is damaged: Fixed
- I-16 SPB "Drone" picks up fuel from mothership until it reaches 100%: Fixed, will refuel up to initial mission start limit only
- Enemy planes become available on player homebase not only after (belly/emergency) landing, but even if they crashed there: Fixed
- On maps with more than 256 bridges, when a human player destroys a bridge with index 256 or bigger, another bridge with that index modulo 256 will be destroyed instead: Fixed
- MDS/RRR: other players will only see one rocket being rearmed although you have (and see) all: Fixed
- MDS/RRR: other players will not see any bomb/droptank/torpedo rearming at all: Fixed
- MDS/RRR: other players will see an empty loadout when loadout options are changed: Fixed

********************

Installation:
1.) Run JSGME.exe from your UP3 RC4 game folder and disable any of the following previously installed mods(packs), if applicable:
    * Any old Hotfix Pack (* see below)
    * Any old Patch Pack (e.g. "#UP#_Patch_Pack_105")
    * UP3 RC4 without obfuscation ("#UP#_Unobfuscated_MDS_for_UP3"), because this is integrated in this patch pack already
2.) Check your enabled JSGME Mods, now there should only be:
    * #UP#_Enable_UP.3.0
    * Additional JSGME Mods not related to Fixes, Patches, unobfuscated UP etc.
3.) Extract this Patch Pack to your UP3 RC4 game folder
4.) Run jsgme.exe and activate this mod ("#UP#_Patch_Pack_106")

********************

(*) Deactivation / Deinstallation of previously installed Hotfixes and/or Hotfix Packs:
Previously installed Hotfixes and Hotfix Packs have to be deactivated from within JSGME.
The Hotfix Packs appear like this in JSGME, watch out for them and deactivate them if they appear to be activated:
- #UP#_Hotfix_Pack_v1.00
- #UP#_Hotfix_Pack_v1.01

The Hotfixes appear like this in JSGME, watch out for them and deactivate them if they appear to be activated:
- #UP#_Early_109_Hotfix_for_UP3
- #UP#_Jet_Booster_Hotfix_for_UP3
- #UP#_Ju87C_Hotfix_for_UP3
- #UP#_SM.79_and_Cant_Z.1007_Gunner_Hotfix_for_UP3
- #UP#_Sniper_Gunner_Hotfix_for_UP3

The Bf 109 G14 Hotfix installs directly into the #UP# folder, please remove or deactivate that folder:
- UP3 Bf 109G-14 Hotfix
