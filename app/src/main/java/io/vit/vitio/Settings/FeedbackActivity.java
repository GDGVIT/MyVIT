/*
 * Copyright (c) 2015 GDG VIT Vellore.
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.vit.vitio.Settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import io.vit.vitio.Extras.TypeFaceSpan;
import io.vit.vitio.Managers.AppController;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.R;

/**
 * Created by shalini on 13-07-2015.
 */
public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button submitButton;
    private EditText textField;
    private DataHandler dataHandler;
    private final static String FEEDBACK_URL="http://www.princebansal.comeze.com/feedback.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs_feedback_fragment);
        toolbar=(Toolbar)findViewById(R.id.app_bar);

        toolbar.setBackgroundColor(getResources().getColor(R.color.darkgray));
        setSupportActionBar(toolbar);
        SpannableString s = new SpannableString("FEEDBACK");
        s.setSpan(new TypeFaceSpan(this, "Montserrat-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeStatusBarColor(getResources().getColor(R.color.darkergray));

        submitButton=(Button)findViewById(R.id.submit_button);
        textField=(EditText)findViewById(R.id.text_field);
        submitButton.setOnClickListener(this);

        dataHandler=DataHandler.getInstance(this);
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(color);
        }
    }

    @Override
    public void onClick(View v) {
        if(textField.getText().toString().equals("")){
            Toast.makeText(FeedbackActivity.this,"Please enter valid feedback",Toast.LENGTH_SHORT).show();
            return;
        }
        String url=FEEDBACK_URL+"?message="+textField.getText().toString()+"&&regno="+dataHandler.getRegNo()+"&&dob="+dataHandler.getDOB()+"&&phone="+dataHandler.getPhoneNo();
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("feedback", response);
                        if(response.equals("Success"))
                            Toast.makeText(FeedbackActivity.this,"Thank You for registering your feedback",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(FeedbackActivity.this,"There was an error. Please try again later",Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FeedbackActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();

                    }
                });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
