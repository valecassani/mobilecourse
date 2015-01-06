package it.polimi.mobilecourse.expenses;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import android.service.textservice.SpellCheckerService;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;



/**
 * Created by Matteo on 02/01/2015.
 */
public class LoginFBActivity extends Activity {

    boolean logged;
    Context ctx;
    TextView nome;
    private LoginButton lb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginfb);
        nome=(TextView)findViewById(R.id.nome);
        ctx=this;
        logged=false;

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Session.getActiveSession().onActivityResult(this,requestCode,resultCode,data);
        facebookLogin();

    }



    public void facebookLogin(){

        Session.openActiveSession(this, true, new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    Request.newMeRequest(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(final GraphUser user, final Response response) {
                            if (user != null) {
                                nome.setText("Ciao " + user.getName() + "!");
                                logged = true;
                            }
                        }
                    }).executeAsync();

                }
                else
                {
                    nome.setText("Ciao anonimo!");
                    logged = false;
                }
            }});
    }
}