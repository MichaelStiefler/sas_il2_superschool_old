package com.maddox.il2.engine;

import java.util.List;

import com.maddox.rts.MainWin32;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.RTSConfWin;
import com.maddox.rts.Time;

class ConsoleGL0Render extends Render {

	public void exlusiveDraw() {
		if (RTSConf.cur instanceof RTSConfWin)
			((MainWin32)RTSConf.cur.mainWindow).loopMsgs();
		GObj.DeleteCppObjects();
		renders().paint(this);
	}

	public void exlusiveDrawStep(String s, int i) {
		sstep = s;
		renders().paint(this);
	}

	private boolean getAIFlyablesActive() {
		this.theAIFlyablesClassChecked = true;
		try {
			this.theAIFlyablesClass = Class.forName("com.maddox.il2.objects.air.AIFlyables");
			return (this.theAIFlyablesClass != null);
		} catch (ClassNotFoundException e) {
			this.theAIFlyablesClass = null;
			try {
				Class flyableWellingtonTest = Class.forName("com.maddox.il2.objects.air.CockpitWellington_MKIII");
				return (flyableWellingtonTest != null);
			} catch (ClassNotFoundException e2) {
				return false;
			}
		}
	}

	private void setAIFlyablesCaption() {
		if (this.theAIFlyablesClassChecked)
			return;
		if (!getAIFlyablesActive())
			return;
		String versionAIFlyables = "";
		if (this.theAIFlyablesClass != null) {
			versionAIFlyables = Property.stringValue(this.theAIFlyablesClass, "Version", "");
			if (versionAIFlyables.length() > 0)
				versionAIFlyables = " " + versionAIFlyables;
		}
		String userLanguage = RTSConf.cur.locale.getLanguage().toLowerCase();
		if (userLanguage == null)
			userLanguage = "us";
		else
			userLanguage = userLanguage.substring(0, 2);
		if (userLanguage == "en")
			userLanguage = "us";
		if ((!"de".equals(userLanguage)) && (!"fr".equals(userLanguage)) && (!"cs".equals(userLanguage))
				&& (!"pl".equals(userLanguage)) && (!"hu".equals(userLanguage)) && (!"lt".equals(userLanguage))
				&& (!"us".equals(userLanguage)) && (!"ru".equals(userLanguage)))
			userLanguage = "us";
		if ("ru".equals(userLanguage))
			AI_FLYABLES = " \u0438 SAS \u043b\u0435\u0442\u043d\u044b\u0439 AI \u0421\u0430\u043c\u043e\u043b\u0435\u0442\u044b"
					+ versionAIFlyables;
		else if ("de".equals(userLanguage))
			AI_FLYABLES = " und SAS steuerbaren AI Flugzeugen" + versionAIFlyables;
		else if ("fr".equals(userLanguage))
			AI_FLYABLES = " et SAS pilotable AI a\u00E9ronefs" + versionAIFlyables;
		else if ("cs".equals(userLanguage))
			AI_FLYABLES = " a SAS schopn\u00FD letu AI Letadla" + versionAIFlyables;
		else if ("pl".equals(userLanguage))
			AI_FLYABLES = " i SAS kontrolowalnym AI Samoloty" + versionAIFlyables;
		else if ("hu".equals(userLanguage))
			AI_FLYABLES = " \u00e9s SAS rep\u00FClhet\u0151 AI rep\u00FCl\u0151g\u00E9pek" + versionAIFlyables;
		else if ("lt".equals(userLanguage))
			AI_FLYABLES = " ir SAS tinkamas skraidyti AI orlaiviai" + versionAIFlyables;
		else
			AI_FLYABLES = " and SAS flyable AI Aircrafts" + versionAIFlyables;

	}

	public void render() {
		if (ConsoleGL0.backgroundMat != null) {
			if (RendersMain.getViewPortWidth() / RendersMain.getViewPortHeight() < 1) {
				float f = (float)RendersMain.getViewPortWidth() * 0.75F;
				float f2 = ((float)RendersMain.getViewPortHeight() - f) * 0.5F;
				drawTile(0.0F, f2, RendersMain.getViewPortWidth(), f, 0.0F, ConsoleGL0.backgroundMat, -1, 0.0F, 1.0F, 1.0F, -1F);
			} else {
				drawTile(0.0F, 0.0F, RendersMain.getViewPortWidth(), RendersMain.getViewPortHeight(), 0.0F,
						ConsoleGL0.backgroundMat, -1, 0.0F, 1.0F, 1.0F, -1F);
			}
			if (sstep != null) {
				this.setAIFlyablesCaption();
				int i = 0xff0000ff;
				if ("ru".equalsIgnoreCase(RTSConf.cur.locale.getLanguage())) {
					TTFont.font[2].output(i, (float)RendersMain.getViewPortWidth() * 0.083F,
							(float)RendersMain.getViewPortHeight() * 0.12F, 0.0F, sstep);
					TTFont.font[2].output(i, (float)RendersMain.getViewPortWidth() * 0.083F,
							((float)RendersMain.getViewPortHeight() * 0.12F + (float)TTFont.font[2].height())
									- (float)TTFont.font[2].descender(), 0.0F, GAME_VERSION
									+ " c SAS ModA\u043a\u0442\u0438\u0432\u0430\u0442\u043e\u0440\u043e\u043c "
									+ MODACT_VERSION + AI_FLYABLES);
				} else {

					String userLanguage = RTSConf.cur.locale.getLanguage().toLowerCase();
					if (userLanguage == null)
						userLanguage = "us";
					else
						userLanguage = userLanguage.substring(0, 2);
					if (userLanguage == "en")
						userLanguage = "us";
					if ((!"de".equals(userLanguage)) && (!"fr".equals(userLanguage)) && (!"cs".equals(userLanguage))
							&& (!"pl".equals(userLanguage)) && (!"hu".equals(userLanguage)) && (!"lt".equals(userLanguage))
							&& (!"us".equals(userLanguage)))
						userLanguage = "us";

					String versionString = GAME_VERSION + " with SAS ModActivator " + MODACT_VERSION + AI_FLYABLES;
					if ("de".equals(userLanguage))
						versionString = GAME_VERSION + " mit SAS ModActivator " + MODACT_VERSION + AI_FLYABLES;
					else if ("fr".equals(userLanguage))
						versionString = GAME_VERSION + " avec SAS ModActivator " + MODACT_VERSION + AI_FLYABLES;
					else if ("cs".equals(userLanguage))
						versionString = GAME_VERSION + " s SAS ModAktivátorem " + MODACT_VERSION + AI_FLYABLES;
					else if ("pl".equals(userLanguage))
						versionString = GAME_VERSION + " z SAS ModAktywatorem " + MODACT_VERSION + AI_FLYABLES;
					else if ("hu".equals(userLanguage))
						versionString = GAME_VERSION + " egy\u00fctt SAS ModActivator " + MODACT_VERSION + AI_FLYABLES;
					else if ("lt".equals(userLanguage))
						versionString = GAME_VERSION + " su SAS ModAktyvatoriumi " + MODACT_VERSION + AI_FLYABLES;

					TTFont.font[2].output(i, (float)RendersMain.getViewPortWidth() * 0.02F,
							(float)RendersMain.getViewPortHeight() * 0.17F, 0.0F, sstep);
					TTFont.font[2].output(i, (float)RendersMain.getViewPortWidth() * 0.02F,
							((float)RendersMain.getViewPortHeight() * 0.17F + (float)TTFont.font[2].height())
									- (float)TTFont.font[2].descender(), 0.0F, versionString);
				}
			}
			return;
		}
		if (ConsoleGL0.bActive || ConsoleGL0.consoleListener != null) {
			List list = RTSConf.cur.console.historyOut();
			int j = RTSConf.cur.console.startHistoryOut();
			if (!RTSConf.cur.console.isShowHistoryOut()) {
				list = RTSConf.cur.console.historyCmd();
				j = RTSConf.cur.console.startHistoryCmd();
			}
			int k = getViewPortHeight() / ConsoleGL0.font.height() - 1;
			if (ConsoleGL0.bActive) {
				int l = RTSConf.cur.console.editBuf.length();
				String s = RTSConf.cur.console.getPrompt();
				int l1 = s.length();
				int i2 = 0;
				if (l1 != 0)
					s.getChars(0, l1, buf, 0);
				if (l + l1 > 0) {
					if (l + l1 > buf.length)
						buf = new char[l + l1 + 16];
					if (l != 0) {
						RTSConf.cur.console.editBuf.getChars(0, l, buf, l1);
						float f4 = (float)getViewPortWidth() - ConsoleGL0.typeOffset;
						do {
							float f6 = ConsoleGL0.font.width(buf, 0, (RTSConf.cur.console.editPos - i2) + l1);
							if (f6 < f4)
								break;
							i2++;
							RTSConf.cur.console.editBuf.getChars(i2, l, buf, l1);
						} while (true);
					}
					ConsoleGL0.font
							.output(-1, ConsoleGL0.typeOffset, -ConsoleGL0.font.descender(), 0.0F, buf, 0, (l - i2) + l1);
				}
				if ((Time.endReal() / 500L) % 3L != 0L) {
					float f5 = 0.0F;
					if (RTSConf.cur.console.editPos + l1 > 0)
						f5 = ConsoleGL0.font.width(buf, 0, (RTSConf.cur.console.editPos - i2) + l1);
					buf[0] = '|';
					ConsoleGL0.font.output(-1, (ConsoleGL0.typeOffset + f5) - 1.0F, -ConsoleGL0.font.descender(), 0.0F, buf, 0,
							1);
				}
			}
			if (RTSConf.cur.console.bWrap) {
				int i1 = j;
				int k1 = 1;
				for (; k > 0 && i1 < list.size(); i1++) {
					String s1 = (String)(String)list.get(i1);
					int j2;
					for (j2 = s1.length() - 1; j2 >= 0 && s1.charAt(j2) < ' '; j2--)
						;
					if (j2 > 0) {
						j2++;
						int l2 = 0;
						int i3 = 0;
						int j3 = j2;
						ofs[l2] = i3;
						do {
							if (j3 <= 0 || ConsoleGL0.font.width(s1, i3, j3) <= 0.0F)
								break;
							int k3 = j3;
							do {
								if (ConsoleGL0.font.width(s1, i3, j3) + ConsoleGL0.typeOffset <= (float)getViewPortWidth())
									break;
								while (--j3 > 0 && s1.charAt(i3 + j3) != ' ')
									;
							} while (j3 != 0);
							if (j3 == 0)
								for (j3 = k3; ConsoleGL0.font.width(s1, i3, j3) + ConsoleGL0.typeOffset > (float)getViewPortWidth()
										&& --j3 != 0;)
									;
							if (l2 + 1 >= ofs.length)
								break;
							l2++;
							if (j3 == 0) {
								ofs[l2] = j2;
								break;
							}
							i3 += j3;
							j3 = j2 - i3;
							ofs[l2] = i3;
						} while (true);
						for (; l2 > 0 && k > 0; k--) {
							ConsoleGL0.font.output(-1, ConsoleGL0.typeOffset,
									ConsoleGL0.font.height() * k1 - ConsoleGL0.font.descender(), 0.0F, s1, ofs[l2 - 1], ofs[l2]
											- ofs[l2 - 1]);
							k1++;
							l2--;
						}

					}
				}

			} else {
				if (k > list.size() - j)
					k = list.size() - j;
				for (int j1 = 0; j1 < k; j1++) {
					float f3 = ConsoleGL0.font.height() * (j1 + 1) - ConsoleGL0.font.descender();
					String s2 = (String)(String)list.get(j1 + j);
					int k2;
					for (k2 = s2.length() - 1; k2 >= 0 && s2.charAt(k2) < ' '; k2--)
						;
					if (k2 > 0)
						ConsoleGL0.font.output(-1, ConsoleGL0.typeOffset, f3, 0.0F, s2, 0, k2 + 1);
				}

			}
		}
	}

	public ConsoleGL0Render(float f) {
		super(f);
		buf = new char[128];
		ofs = new int[128];
		sstep = null;
		useClearDepth(false);
		useClearColor(false);
	}

	private static final String GAME_VERSION = "V 4.13RC04m";
	private static final String MODACT_VERSION = "6";
	private static String AI_FLYABLES = "";
	private Class theAIFlyablesClass = null;
	private boolean theAIFlyablesClassChecked = false;
	public static final int COLOR = -1;
	private char buf[];
	private int ofs[];
	public String sstep;
}
