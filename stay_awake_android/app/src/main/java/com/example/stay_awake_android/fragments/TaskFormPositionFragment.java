package com.example.stay_awake_android.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stay_awake_android.AppController;
import com.example.stay_awake_android.R;
import com.example.stay_awake_android.databinding.FragmentTaskFormBinding;
import com.example.stay_awake_android.databinding.FragmentTaskFormPositionBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

public class TaskFormPositionFragment extends DialogFragment {

    private static TaskFragment mParent;
    private FragmentTaskFormPositionBinding binding;
    private final String routeUpdate = "positionTask/";
    private String currentTaskId;
    public static TaskFormPositionFragment newInstance(TaskFragment parent, @Nullable String taskId) {
        TaskFormPositionFragment f = new TaskFormPositionFragment();

        mParent = parent;

        Bundle bundle = new Bundle();
        if(taskId != null) bundle.putString("taskId", taskId);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaskFormPositionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            if(getArguments().getString("taskId") != null) {
                currentTaskId = getArguments().getString("taskId");
            }
        }

        binding.buttonValidateTaskFormPosition.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                onClickValidate();
            }
        });

        binding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onClickValidate() {
        if(checkField()) {

            int heure = Integer.parseInt(binding.editTextHour.getText().toString());
            String heureString;

            if(heure < 10) {
                heureString = "0" + heure + ":00";
            } else {
                heureString = heure + ":00";
            }

            JSONObject postData = new JSONObject();
            try {
                postData.put("day", LocalDate.now().toString());
                postData.put("hour", heureString);
                postData.put("duration", binding.editTextDuration.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(currentTaskId != null) {

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, AppController.url + routeUpdate + currentTaskId, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(AppController.TAG, response.toString());
                        mParent.refreshData();
                        dismiss();
                        Toast.makeText(getActivity(), "La tâche à bien été positionnée.", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                        binding.textViewError.setVisibility(View.VISIBLE);
                    }
                });

                AppController.getInstance().addToRequestQueue(request);
            }


        }
    }

    private boolean checkField() {
        boolean isValid = true;

        if(binding.editTextHour.getText().toString().isEmpty()){
            binding.editTextHour.setError("L'heure doit être détérminée");
            binding.editTextHour.requestFocus();
            isValid = false;
        } else {
            int heure = Integer.parseInt(binding.editTextHour.getText().toString());

            if(heure < 6 || heure > 22){
                binding.editTextHour.setError("L'heure doit être comprise entre 6h et 22h");
                binding.editTextHour.requestFocus();
                isValid = false;
            }
        }
        if(binding.editTextDuration.getText().toString().isEmpty()){
            binding.editTextDuration.setError("La durée doit être détérminée");
            binding.editTextDuration.requestFocus();
            isValid = false;
        } else {
            int duree = Integer.parseInt(binding.editTextDuration.getText().toString());

            if(duree < 0 || duree > 4) {
                binding.editTextDuration.setError("La durée n'est pas valide");
                binding.editTextDuration.requestFocus();
                isValid = false;
            }
        }

        return isValid;
    }
}