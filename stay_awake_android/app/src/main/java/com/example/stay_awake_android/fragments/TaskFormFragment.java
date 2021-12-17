package com.example.stay_awake_android.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class TaskFormFragment extends DialogFragment {

    private static TaskFragment mParent;
    private FragmentTaskFormBinding binding;
    private ProgressDialog pDialog;
    private String currentTaskId;
    private final String routeCreate = "createTask";
    private final String routeUpdate = "updateTask/";

    private String[] priorites = {"1", "2", "3"};

    public static TaskFormFragment newInstance(TaskFragment parent) {
        TaskFormFragment f = new TaskFormFragment();

        mParent = parent;

        return f;
    }

    public static TaskFormFragment newInstance(TaskFragment parent, @Nullable String taskId, @Nullable String taskTitle, @Nullable String taskDescription, int taskPriority, boolean taskPermanent) {
        TaskFormFragment f = new TaskFormFragment();

        mParent = parent;

        Bundle bundle = new Bundle();
        if(taskId != null) bundle.putString("taskId", taskId);
        if(taskTitle != null) bundle.putString("taskTitle", taskTitle);
        if(taskDescription != null) bundle.putString("taskDescription", taskDescription);
        if(taskPriority != 0) bundle.putInt("taskPriority", taskPriority);
        bundle.putBoolean("taskPermanent", taskPermanent);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaskFormBinding.inflate(inflater, container, false);
        binding.spinnerTaskPriority.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, priorites));

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
            if(getArguments().getString("taskTitle") != null) {
                binding.editTextTaskTitle.setText(getArguments().getString("taskTitle"));
            }
            if(getArguments().getString("taskDescription") != null) {
                binding.editTextTaskDescription.setText(getArguments().getString("taskDescription"));
            }
            if(getArguments().getInt("taskPriority") != 0) {
                binding.spinnerTaskPriority.setSelection(getArguments().getInt("taskPriority") - 1);
            }
            binding.checkBoxTaskPermanent.setChecked(getArguments().getBoolean("taskPermanent"));
        }

        binding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

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
                postData.put("permanent", binding.checkBoxTaskPermanent.isChecked());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request;
            if(currentTaskId != null) {

                request = new JsonObjectRequest(Request.Method.PUT, AppController.url + routeUpdate + currentTaskId, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(AppController.TAG, response.toString());
                        mParent.refreshData();
                        hidePDialog();
                        dismiss();
                        Toast.makeText(getActivity(), "La tâche à bien été modifiée.", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                        dismiss();
                    }
                });
            } else {

                request = new JsonObjectRequest(Request.Method.POST, AppController.url + routeCreate, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(AppController.TAG, response.toString());
                        mParent.refreshData();
                        hidePDialog();
                        dismiss();
                        Toast.makeText(getActivity(), "La tâche à bien été crée.", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
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

        return isValid;
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}