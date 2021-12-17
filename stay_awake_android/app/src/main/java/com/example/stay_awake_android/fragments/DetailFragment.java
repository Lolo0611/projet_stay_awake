package com.example.stay_awake_android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.stay_awake_android.AppController;
import com.example.stay_awake_android.R;
import com.example.stay_awake_android.databinding.FragmentDetailBinding;
import com.example.stay_awake_android.databinding.FragmentTaskFormBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailFragment extends DialogFragment {

    private static HomeFragment mParent;
    private FragmentDetailBinding binding;
    private String currentTaskId;
    private final String routePosition = "positionTask/";
    private final String routeChecked = "checkedTask/";

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(HomeFragment parent, String taskId, String taskTitle, String taskDescription, String taskHour, int taskDuration, boolean taskChecked) {
        DetailFragment f = new DetailFragment();

        mParent = parent;

        Bundle bundle = new Bundle();
        if(taskId != null) bundle.putString("taskId", taskId);
        if(taskTitle != null) bundle.putString("taskTitle", taskTitle);
        if(taskDescription != null) bundle.putString("taskDescription", taskDescription);
        if(taskHour != null) bundle.putString("taskHour", taskHour);
        if(taskDuration != 0) bundle.putInt("taskDuration", taskDuration);
        bundle.putBoolean("taskChecked", taskChecked);

        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            if(getArguments().getString("taskTitle") != null) {
                currentTaskId = getArguments().getString("taskId");
            }
            if(getArguments().getString("taskTitle") != null) {
                binding.textViewTaskTitle.setText(getArguments().getString("taskTitle"));
            }
            if(getArguments().getString("taskDescription") != null) {
                binding.textViewTaskDescription.setText(getArguments().getString("taskDescription"));
            }
            if(getArguments().getString("taskHour") != null && getArguments().getInt("taskDuration") != 0) {
                String taskDate = getArguments().getString("taskHour") + " (" + getArguments().getInt("taskDuration") +"h)";
                binding.textViewTaskDate.setText(taskDate);
            }
            if(getArguments().getBoolean("taskChecked")) {
                binding.buttonCancelTask.setVisibility(View.INVISIBLE);
                binding.buttonCheckTask.setBackgroundColor(ContextCompat.getColor(mParent.getContext(), R.color.red_200));
                binding.buttonCheckTask.setText(R.string.unfinish);
            }
        }


        binding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.buttonCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRemove();
            }
        });

        binding.buttonCheckTask.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                onClickCheck(getArguments().getBoolean("taskChecked"));
            }
        });
    }

    private void onClickRemove() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mParent.getContext());
        alert.setTitle("Retirer la tâche");
        alert.setMessage("Êtes vous sûr de vouloir retirer la tâche de la journée ?");
        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject postData = new JSONObject();
                try {
                    postData.put("day", "");
                    postData.put("hour", "");
                    postData.put("duration", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(currentTaskId != null) {
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, AppController.url + routePosition + currentTaskId, postData, new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(AppController.TAG, response.toString());
                            mParent.refreshData();
                            dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                        }
                    });

                    AppController.getInstance().addToRequestQueue(request);
                }
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void onClickCheck(boolean checked) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("checked", !checked);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(currentTaskId != null) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, AppController.url + routeChecked + currentTaskId, postData, new Response.Listener<JSONObject>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(AppController.TAG, response.toString());
                    mParent.refreshData();
                    dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                }
            });

            AppController.getInstance().addToRequestQueue(request);
        }
    }
}