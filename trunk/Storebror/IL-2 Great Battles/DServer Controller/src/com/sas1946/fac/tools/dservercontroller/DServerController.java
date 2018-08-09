package com.sas1946.fac.tools.dservercontroller;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

public class DServerController {

    private final static int        MAX_LOG_LINES = 20;
    protected Shell                 shlDserverController;
    private final FormToolkit       formToolkit   = new FormToolkit(Display.getDefault());
    private List                    logList;
    private Label                   lblTickDelayValue;
    private final boolean           authenticated = false;

    CallBack<Void, RConClient.RETURN_CODE, String> rConCallback  = new CallBack<Void, RConClient.RETURN_CODE, String>() {
                                                      @Override
                                                      public Void call(RConClient.RETURN_CODE val1, String val2) {
                                                          DServerController.this.rConClientCallback(val1, val2);
                                                          return null;
                                                      }
                                                  };

    private void tickDelayWatcherCallback() {
        Display.getDefault().asyncExec(() -> DServerController.this.lblTickDelayValue.setText(Float.toString(TickDelayWatcher.getInstance().getTickDelay())));
    }

    private void rConClientCallback(RConClient.RETURN_CODE returnCode, String feedback) {
        Display.getDefault().asyncExec(() -> {
            DServerController.this.addLog(feedback);
            DServerController.this.handleRConClientCallback(returnCode, feedback);
        });
    }

    private void addLog(String logLine) {
        Display.getDefault().asyncExec(() -> {
            while (DServerController.this.logList.getItemCount() > MAX_LOG_LINES) {
                DServerController.this.logList.remove(0);
            }
            DServerController.this.logList.add(logLine);
        });
    }

    private void handleRConClientCallback(RConClient.RETURN_CODE returnCode, String feedback) {
        switch (returnCode) {

            case RCONCLIENT_MESSAGE_RECEIVED:
                this.handleRConClientMessageReceived(RConClient.getInstance().getLastCommand(), feedback);
                break;

            case RCONCLIENT_CONNECTED:
                RConClient.getInstance().sendCommand(RConClient.COMMAND.auth, Settings.getInstance().getrConSettings().getUser(), Settings.getInstance().getrConSettings().getPass());
                break;

            default:
                break;

        }
    }

    private void handleRConClientMessageReceived(RConClient.COMMAND command, String feedback) {
        switch (command) {
            case none:
                break;
            case mystatus:
                RConClient.getInstance().sendCommand(RConClient.COMMAND.serverstatus);
                break;
            case auth:
                RConClient.getInstance().sendCommand(RConClient.COMMAND.mystatus);
                break;
            case getconsole:
                break;
            case getplayerlist:
                break;
            case serverstatus:
                RConClient.getInstance().sendCommand(RConClient.COMMAND.spsget);
                break;
            case kick:
                break;
            case ban:
                break;
            case banuser:
                break;
            case unbanall:
                break;
            case serverinput:
                break;
            case sendstatnow:
                break;
            case cutchatlog:
                break;
            case chatmsg:
                break;
            case opensds:
                break;
            case spsget:
                RConClient.getInstance().sendCommand(RConClient.COMMAND.getplayerlist);
                break;
            case spsreset:
                break;
            case shutdown:
            default:
                break;
        }
    }

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            final DServerController window = new DServerController();
            window.open();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        final Display display = Display.getDefault();
        this.createContents();
        this.shlDserverController.open();
        this.shlDserverController.layout();
        
        MySqlConnector mySqlConnector = MySqlConnector.getInstance();

        TickDelayWatcher.getInstance().setTickDelayUpdateCallback(() -> this.tickDelayWatcherCallback());
        RConClient.getInstance().setRConCallback(this.rConCallback);
        RConClient.getInstance().connect(Settings.getInstance().getrConSettings().getAddress(), Settings.getInstance().getrConSettings().getPort());

        while (!this.shlDserverController.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        this.shlDserverController = new Shell();
        this.shlDserverController.setSize(800, 373);
        this.shlDserverController.setText("DServer Controller");

        this.logList = new List(this.shlDserverController, SWT.BORDER | SWT.V_SCROLL);
        this.logList.setBounds(10, 10, 764, 241);
        this.formToolkit.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        this.formToolkit.adapt(this.logList, true, true);

        final Button btnCheckButton = new Button(this.shlDserverController, SWT.CHECK);
        btnCheckButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final Button btn = (Button) e.getSource();
                btn.setSelection(DServerController.this.authenticated);
            }
        });
        btnCheckButton.setBounds(10, 257, 96, 16);
        this.formToolkit.adapt(btnCheckButton, true, true);
        btnCheckButton.setText("Authenticated");

        final Label lblSps = new Label(this.shlDserverController, SWT.NONE);
        lblSps.setBounds(10, 279, 55, 15);
        this.formToolkit.adapt(lblSps, true, true);
        lblSps.setText("SPS:");

        final Label lblNewLabel = new Label(this.shlDserverController, SWT.NONE);
        lblNewLabel.setBounds(71, 279, 55, 15);
        this.formToolkit.adapt(lblNewLabel, true, true);
        lblNewLabel.setText("0.0");

        Label lblTickDelay = new Label(shlDserverController, SWT.NONE);
        lblTickDelay.setText("Tick Delay:");
        lblTickDelay.setBounds(10, 300, 55, 15);
        formToolkit.adapt(lblTickDelay, true, true);

        lblTickDelayValue = new Label(shlDserverController, SWT.NONE);
        lblTickDelayValue.setText("0.0");
        lblTickDelayValue.setBounds(71, 300, 55, 15);
        formToolkit.adapt(lblTickDelayValue, true, true);

    }
}
