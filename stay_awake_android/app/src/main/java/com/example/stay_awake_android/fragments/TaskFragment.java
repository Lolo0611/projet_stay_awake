package com.example.stay_awake_android.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.stay_awake_android.AppController;
import com.example.stay_awake_android.R;
import com.example.stay_awake_android.adapters.TaskRecyclerViewAdapter;
import com.example.stay_awake_android.databinding.FragmentTaskListBinding;
import com.example.stay_awake_android.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class TaskFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private final String route = "Tasks";

    private FragmentTaskListBinding binding;

    private List<Task> taskList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskFragment() {
    }

    @SuppressWarnings("unused")
    public static TaskFragment newInstance(int columnCount) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaskListBinding.inflate(inflater, container, false);

        refreshData();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().findViewById(R.id.button_taskAdd).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.button_taskAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DialogFragment newFragment = TaskFormFragment.newInstance(TaskFragment.this);
                newFragment.show(fragmentManager, "dialog");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.button_taskAdd).setVisibility(View.INVISIBLE);
    }

    public void refreshData() {
        taskList = new ArrayList<Task>();
        binding.taskList.setAdapter(new TaskRecyclerViewAdapter(taskList, this));

        JsonArrayRequest tasksReq = new JsonArrayRequest(Request.Method.GET, AppController.url + route, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(AppController.TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Task task = new Task();

                                if(obj.has("_id")) task.setId(obj.getString("_id"));
                                if(obj.has("title")) task.setTitle(obj.getString("title"));
                                if(obj.has("description")) task.setDescription(obj.getString("description"));
                                if(obj.has("priority")) task.setPriority(Integer.parseInt(obj.getString("priority")));
                                if(obj.has("permanent")) task.setPermanent(obj.getBoolean("permanent"));

                                taskList.add(task);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if(Objects.nonNull(binding.taskList)) {
                            binding.taskList.getAdapter().notifyDataSetChanged();
                        }
                        //saveData();
                    }
                }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                /*restoreData();
                if(Objects.nonNull(binding.taskList)) {
                    binding.taskList.getAdapter().notifyDataSetChanged();
                }*/
            }
        });

        AppController.getInstance().addToRequestQueue(tasksReq);
    }

    public void saveData() {
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        AppController.editor.putString(route, json);
        AppController.editor.apply();
    }

    public void restoreData() {
        Toast.makeText(getActivity(), "Tâches restorées.", Toast.LENGTH_SHORT).show();

        String serializedObject = AppController.sharedPreferences.getString(route, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Task>>(){}.getType();
            taskList = gson.fromJson(serializedObject, type);
        }
    }
}