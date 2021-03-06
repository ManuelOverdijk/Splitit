/** Native App Studio
 *
 * SplitIt - Student's choice
 *
 * Author: Manuel Overdijk
 *         manuel.overdijk@gmail.com
 *         Uva id: 10374582
 *
 * December 2014
 */

package nl.mprog.studentchoice10374582;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class RegisterActivity extends ActionBarActivity {

    private Button mRegisterButton;
    private EditText mEmailEdit;
    private EditText mPasswordEdit;

    String firebaseUrl;
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseUrl = getResources().getString(R.string.firebase_url);
        ref = new Firebase(firebaseUrl);

        mRegisterButton = (Button)findViewById(R.id.buttonRegister2);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAccount();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerAccount() {


        mEmailEdit = (EditText) findViewById(R.id.editEmailRegister);
        mPasswordEdit = (EditText) findViewById(R.id.editPasswordRegister);

        final String mEmail = mEmailEdit.getText().toString();
        final String mPassword = mPasswordEdit.getText().toString();

        if (!TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)) {


            ref.createUser(mEmail, mPassword, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Account is made",
                            Toast.LENGTH_LONG).show();
                    ref.authWithPassword(mEmail, mPassword, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Intent intent = new Intent(getApplicationContext(),GroupActivity.class);
                            startActivity(intent);

                        }
                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {

                        }
                    });
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Toast.makeText(getApplicationContext(), firebaseError.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please fill everything in", Toast.LENGTH_SHORT).show();
        }
    }
}
