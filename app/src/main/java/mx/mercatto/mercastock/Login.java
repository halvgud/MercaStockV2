package mx.mercatto.mercastock;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
public class Login extends Fragment implements View.OnClickListener {
    TextView txSucursal;
    EditText txtUsuario;
    EditText txtPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        getActivity().setTitle("MercaStock");
        Button upButton = (Button) rootView.findViewById(R.id.button2);
        upButton.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        txSucursal=(TextView) rootView.findViewById(R.id.textView13);
        txtUsuario = (EditText) rootView.findViewById(R.id.InputUsuario);
        txtPassword = (EditText) rootView.findViewById(R.id.txtPassword);
        Constante.ConfigurarUsuario("");
        txtUsuario.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String gg = "";
            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtUsuario.getText().toString();
                value2 = txtPassword.getText().toString();

                if ((!value1.equals(gg) && !value2.equals(gg)) && (value1.length() > 1 && value2.length() == 4)) {
                    if(getView()!=null) {
                        getView().findViewById(R.id.button2).setEnabled(true);
                    }
                } else {
                    if(getView()!=null) {
                        getView().findViewById(R.id.button2).setEnabled(false);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtUsuario.getText().toString();
                value2 = txtPassword.getText().toString();

                if ((!value1.equals(gg) && !value2.equals(gg)) && (value1.length() > 1 && value2.length() == 4 )) {
                    if(getView()!=null) {
                        getView().findViewById(R.id.button2).setEnabled(true);
                    }
                } else {
                    if(getView()!=null) {
                        getView().findViewById(R.id.button2).setEnabled(false);
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        return rootView;
    }


///TODO: Implementar un spinner para el IdSucursal->Sucursal, para poder ser asignado en el movil
    @Override
    public void onClick(View v) {
        String usuario = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put(Constante.jsonUsuario, usuario);
            jsonObj1.put(Constante.jsonContrasena, password);
            jsonObj1.put(Constante.jsonClaveGCM,Constante.claveGCM);
            BGTLogIn bgt = new BGTLogIn(Constante.urlUsuarioLogIn(), getActivity(), jsonObj1);
            bgt.execute();

        } catch (Exception e){
            showToast(e.toString());
        }

    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }



}
