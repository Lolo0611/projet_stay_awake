package com.example.stay_awake_android.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.stay_awake_android.AppController;
import com.example.stay_awake_android.R;
import com.example.stay_awake_android.TaskRecyclerViewAdapter;
import com.example.stay_awake_android.databinding.FragmentSecondBinding;
import com.example.stay_awake_android.databinding.FragmentTaskBinding;
import com.example.stay_awake_android.databinding.FragmentTaskListBinding;
import com.example.stay_awake_android.models.Task;
import com.example.stay_awake_android.placeholder.PlaceholderContent;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private ProgressDialog pDialog;

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

        resfreshData();

        return binding.getRoot();
    }

    public void resfreshData() {
        taskList = new ArrayList<Task>();
        binding.taskList.setAdapter(new TaskRecyclerViewAdapter(taskList, this));

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Chargement...");
        pDialog.show();

        JsonArrayRequest tasksReq = new JsonArrayRequest(Request.Method.GET, AppController.url + route, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(AppController.TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Task task = new Task();

                                task.setId(obj.getString("_id"));
                                task.setTitle(obj.getString("title"));
                                task.setDescription(obj.getString("description"));
                                //task.setDate(LocalDateTime.parse(obj.getString("Date")));
                                //task.setDuration(LocalDateTime.parse(obj.getString("Duration")));
                                task.setPriority(Integer.parseInt(obj.getString("priority")));

                                taskList.add(task);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if(Objects.nonNull(binding.taskList)) {
                            binding.taskList.getAdapter().notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(tasksReq);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.floatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(TaskFragment.this)
                        .navigate(R.id.action_TaskFragment_to_TaskFormFragment);
            }
        });
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}