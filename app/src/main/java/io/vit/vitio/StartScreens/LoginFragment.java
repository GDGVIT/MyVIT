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

package io.vit.vitio.StartScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.DatabaseMetaData;
import java.util.Calendar;
import java.util.regex.Pattern;

import io.vit.vitio.Extras.ErrorDefinitions;
import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Managers.ConnectAPI;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseCourses;
import io.vit.vitio.R;

/**
 * Created by shalini on 15-06-2015.
 */
public class LoginFragment extends Fragment implements ConnectAPI.RequestListener {
    private int FRAGMENTID = 0;
    private EditText regnoCol, dateCol, monthCol, yearCol, phoneCol;
    private TextView errorView;
    private Button signInbutton;
    private RadioGroup campusRadioGroup;
    private RadioButton velloreRadio, chennaiRadio;

    private ProgressDialog dialog;

    private DataHandler dataHandler;
    private ConnectAPI connectAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.login_fragment, container, false);

        //Initialise Views
        init(rootView);
        setInit();

        return rootView;
    }



    private void init(ViewGroup rootView) {
        regnoCol = (EditText) rootView.findViewById(R.id.regno_col);
        dateCol = (EditText) rootView.findViewById(R.id.date_col);
        monthCol = (EditText) rootView.findViewById(R.id.month_col);
        yearCol = (EditText) rootView.findViewById(R.id.year_col);
        phoneCol = (EditText) rootView.findViewById(R.id.phoneno_col);
        errorView = (TextView) rootView.findViewById(R.id.errortext);
        campusRadioGroup = (RadioGroup) rootView.findViewById(R.id.campus_select_radio_group);
        velloreRadio = (RadioButton) rootView.findViewById(R.id.vellore_radio);
        chennaiRadio = (RadioButton) rootView.findViewById(R.id.chennai_radio);
        signInbutton = (Button) rootView.findViewById(R.id.next_button);

        dataHandler = DataHandler.getInstance(getActivity());
        connectAPI = new ConnectAPI(getActivity());
        dialog = new ProgressDialog(getActivity());
    }

    private void setInit() {
        setTextWatchers();

        signInbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    saveData();
                    connectAPI.serverTest();
                }
                //((FragmentHolder)getActivity()).changePage(FRAGMENTID);
            }
        });


        connectAPI.setOnRequestListener(this);
        dialog.setCancelable(false);


        dataHandler.saveFirstTimeUser("true");
    }

    private void setTextWatchers() {
        regnoCol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isFieldEmpty()) {
                    if (regnoCol.getText().toString().matches("^[0-9]{2}[a-zA-Z]{3,}[0-9]{4}$")) {
                        dateCol.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateCol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isFieldEmpty()) {
                    if (dateCol.getText().toString().length()==2) {
                        monthCol.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        monthCol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isFieldEmpty()) {
                    if (monthCol.getText().toString().length()==2) {
                        yearCol.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        yearCol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isFieldEmpty()) {
                    if (yearCol.getText().toString().length()==4) {
                        phoneCol.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void saveData() {
        dataHandler.saveRegNo(regnoCol.getText().toString());
        dataHandler.saveDOB(dateCol.getText().toString() + monthCol.getText().toString() + yearCol.getText().toString());
        dataHandler.savePhoneNo(phoneCol.getText().toString());
        String campus;
        if (campusRadioGroup.getCheckedRadioButtonId() == R.id.vellore_radio) {
            campus = "vellore";
        } else {
            campus = "chennai";
        }
        dataHandler.saveCampus(campus);
    }

    private boolean validate() {

        if (isFieldEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!regnoCol.getText().toString().matches("^[0-9]{2}[a-zA-Z]{3,}[0-9]{4}$")) {
            Toast.makeText(getActivity(), "Enter valid registration number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(Integer.parseInt(dateCol.getText().toString()) > 0 && Integer.parseInt(dateCol.getText().toString()) < 32
                && Integer.parseInt(monthCol.getText().toString()) > 0 && Integer.parseInt(monthCol.getText().toString()) < 13
                && Integer.parseInt(yearCol.getText().toString()) > 1978 && Integer.parseInt(dateCol.getText().toString()) < Calendar.getInstance().get(Calendar.YEAR))) {
            Toast.makeText(getActivity(), "Enter valid date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((phoneCol.getText().toString().length() >14)||(phoneCol.getText().toString().length() <10)) {
            Toast.makeText(getActivity(), "Enter a valid 10-14 digit mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    public boolean isFieldEmpty() {
        boolean i = false;
        if (regnoCol.getText().toString().equals("") || regnoCol.getText() == null) {
            i = true;
        }
        if (dateCol.getText().toString().equals("") || dateCol.getText() == null) {
            i = true;
        }
        if (monthCol.getText().toString().equals("") || monthCol.getText() == null) {
            i = true;
        }
        if (yearCol.getText().toString().equals("") || yearCol.getText() == null) {
            i = true;
        }
        if (phoneCol.getText().toString().equals("") || phoneCol.getText() == null) {
            i = true;
        }
        return i;

    }

    public void setId(int id) {
        FRAGMENTID = id;
    }

    @Override
    public void onRequestInitiated(int code) {
        dialog.setTitle("Initiating");
        switch (code) {
            case ConnectAPI.SERVERTEST_CODE:
                dialog.setMessage("Server Testing");
                break;
            case ConnectAPI.LOGIN_CODE:
                dialog.setMessage("Logging In");
                break;
            case ConnectAPI.REFRESH_CODE:
                dialog.setMessage("Refreshing");
                break;
            default:

        }
        dialog.show();
    }

    @Override
    public void onRequestCompleted(ReturnParcel parcel, int code) {
        switch (code) {
            case ConnectAPI.SERVERTEST_CODE:
                if (parcel.getRETURN_CODE() == ErrorDefinitions.CODE_SUCCESS) {
                    connectAPI.login();
                } else {
                    if (dialog.isShowing()) {
                        dialog.hide();
                    }
                    showToast(parcel.getRETURN_MESSAGE());
                }
                break;
            case ConnectAPI.LOGIN_CODE:
                if (parcel.getRETURN_CODE() == ErrorDefinitions.CODE_SUCCESS) {
                    connectAPI.refresh();
                } else {
                    if (dialog.isShowing()) {
                        dialog.hide();
                    }
                    showToast(parcel.getRETURN_MESSAGE());
                }
                break;
            case ConnectAPI.REFRESH_CODE:
                if (dialog.isShowing()) {
                    dialog.hide();
                }
                if (parcel.getRETURN_CODE() == ErrorDefinitions.CODE_SUCCESS) {
                    startActivity(new Intent(getActivity(),HomeActivity.class));
                    getActivity().finish();
                    dataHandler.saveFirstTimeUser("false");
                } else {
                    showToast(parcel.getRETURN_MESSAGE());
                }
                break;
            default:

        }
    }

    @Override
    public void onErrorRequest(ReturnParcel parcel, int code) {
        if (dialog.isShowing()) {
            dialog.hide();
        }
        showToast(parcel.getRETURN_MESSAGE());
        switch (code) {
            case ConnectAPI.SERVERTEST_CODE:
                break;
            case ConnectAPI.LOGIN_CODE:
                break;
            case ConnectAPI.REFRESH_CODE:
                break;
            default:

        }
    }

    private void showToast(String return_message) {
        Toast.makeText(getActivity(), return_message, Toast.LENGTH_SHORT).show();
    }

}
