package edu.jsipdemo;

import java.text.ParseException;
import java.util.TooManyListenersException;

import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipException;
import javax.sip.TransportNotSupportedException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JsipdemoActivity extends Activity implements MessageProcessor{
	private SipLayer sipLayer ;
	private String username = "100";
    private int port = 5060;
    private String ip = "192.168.11.105";
    private String msg;
    private Button btn_default;
    private Button btn_send;
    private TextView txt_to;
    private TextView txt_from;
    private TextView txt_msg;
    private TextView txt_recvmsg;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn_default = (Button)findViewById(R.id.defaultvalue);
        btn_send = (Button)findViewById(R.id.Btn_send_txt);
        txt_to = (TextView)findViewById(R.id.toaddr);
        txt_from=(TextView)findViewById(R.id.fromaddr);
        txt_msg = (TextView)findViewById(R.id.plaintext);
        txt_recvmsg = (TextView)findViewById(R.id.Recvtext);
        
    }

	
	public void onclick_sendmsg(View v) throws InvalidArgumentException, TooManyListenersException, ParseException, SipException
	{
		String strto= txt_to.getText().toString();
		String strfrom = txt_from.getText().toString();
		this.username = strfrom.substring(strfrom.indexOf(":")+1, strfrom.indexOf("@"));
		String strfrom1 = strfrom.substring(strfrom.indexOf(":")+1);
		this.ip=strfrom1.substring(strfrom1.indexOf("@")+1, strfrom1.indexOf(":"));
		
		strfrom1.substring(strfrom1.indexOf(":")).valueOf(this.port);
		this.msg = txt_msg.getText().toString();
		
		if(sipLayer ==null){
				sipLayer = new SipLayer(username, ip, port);
				sipLayer.setMessageProcessor(this);
		}
		
		sipLayer.sendMessage(strto, this.msg);
	}
//	//UI
//	public void onclick_register(View v){
//		sipLayer.sendMessage(strto, this.msg);
//	}
	public void onclick_default(View v)
	{
		txt_to.setText("sip:101@192.168.11.106:5060");
		txt_from.setText("sip:100@192.168.11.105:5060");
		txt_msg.setText("test123");
	}
	
	//捕获按下键盘上返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    // .setIcon(R.drawable.services)
                    .setTitle("EXIT")
                    .setMessage("Do you wanna quit?")
                    .setNegativeButton("cancel",
                            new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									
								}
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                        int which) {
//                                }
                            })
                    .setPositiveButton("confirm",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    finish();
                                }
                            }).show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    //彻底退出程序
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
        // 或者下面这种方式
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

    
	public void processMessage(String sender, String message) {
		// TODO Auto-generated method stub
		final String sender_f =sender;
		final String message_f=message;
		this.runOnUiThread(new Runnable() {
            public void run() {
		txt_recvmsg.setText("From"+sender_f+":"+message_f+"\n");
            }
		});
	}


	public void processError(String errorMessage) {
		// TODO Auto-generated method stub
		
	}


	public void processInfo(String infoMessage) {
		// TODO Auto-generated method stub
		
	}
}