package com.example.stay_awake_android.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stay_awake_android.AppController;
import com.example.stay_awake_android.R;
import com.example.stay_awake_android.databinding.FragmentTaskFormBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class TaskFormFragment extends Fragment {

    private FragmentTaskFormBinding binding;
    private ProgressDialog pDialog;
    private String currentTaskId;
    private final String routeCreate = "createTask";
    private final String routeUpdate = "updateTask/";

    private String[] priorites = {"1", "2", "3"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaskFormBinding.inflate(inflater, container, false);
        binding.spinnerTaskPriority.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, priorites));

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments().getString("taskTitle") != null) {
            currentTaskId = getArguments().getString("taskId");
        }
        if(getArguments().getString("taskTitle") != null) {
            binding.editTextTaskTitle.setText(getArguments().getString("taskTitle"));
        }
        if(getArguments().getString("taskDescription") != null) {
            binding.editTextTaskDescription.setText(getArguments().getString("taskDescription"));
        }
        if(getArguments().getInt("taskPriority") != 0) {
            binding.spinnerTaskPriority.setSelection(getArguments().getInt("taskPriority") - 1);
        }

        binding.buttonValidateTaskForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickValidate();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onClickValidate() {
        if(checkField()) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Chargement...");
            pDialog.show();

            JSONObject postData = new JSONObject();
            try {
                postData.put("title", binding.editTextTaskTitle.getText());
                postData.put("description", binding.editTextTaskDescription.getText());
                postData.put("priority", binding.spinnerTaskPriority.getSelectedItem().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request;
            if(currentTaskId != null) {

                request = new JsonObjectRequest(Request.Method.PUT, AppController.url + routeUpdate + currentTaskId, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(AppController.TAG, response.toString());
                        hidePDialog();
                        getActivity().onBackPressed();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                        hidePDialog();
                    }
                });
            } else {

                request = new JsonObjectRequest(Request.Method.POST, AppController.url + routeCreate, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(AppController.TAG, response.toString());
                        hidePDialog();
                        getActivity().onBackPressed();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                        hidePDialog();
                    }
                });
            }

            AppController.getInstance().addToRequestQueue(request);
        }
    }

    private boolean checkField() {
        boolean isValid = true;

        if(binding.editTextTaskTitle.getText().toString().isEmpty()){
            binding.editTextTaskTitle.setError("Titre invalide");
            binding.editTextTaskTitle.requestFocus();
            isValid = false;
        }

        if(binding.editTextTaskDescription.getText().toString().isEmpty()){
            binding.editTextTaskDescription.setError("Description invalide");
            binding.editTextTaskDescription.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}