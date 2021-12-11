package com.example.stay_awake_android.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.stay_awake_android.AppController;
import com.example.stay_awake_android.R;
import com.example.stay_awake_android.adapters.TaskCalendarRecyclerViewAdapter;
import com.example.stay_awake_android.adapters.TaskRecyclerViewAdapter;
import com.example.stay_awake_android.databinding.FragmentHomeBinding;
import com.example.stay_awake_android.models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private final String route = "TasksByDay/";
    private LocalDate currentDate;

    private FragmentHomeBinding binding;

    private List<Task> taskList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        currentDate = LocalDate.now();
        refreshData();

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate = currentDate.plusDays(1);
                refreshData();
            }
        });

        binding.buttonPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate = currentDate.minusDays(1);
                refreshData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshData() {
        if(currentDate.equals(LocalDate.now())) {
            binding.buttonPreviousDay.setVisibility(View.INVISIBLE);
        } else {
            binding.buttonPreviousDay.setVisibility(View.VISIBLE);
        }

        binding.textViewCurrentDay.setText(currentDate.format(DateTimeFormatter.ofPattern("EEEE d MMMM")));

        taskList = new ArrayList<Task>();
        binding.taskCalendar.setAdapter(new TaskCalendarRecyclerViewAdapter(taskList, this));

        JsonArrayRequest tasksReq = new JsonArrayRequest(Request.Method.GET, AppController.url + route + currentDate, null,
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

                                task.setId(obj.getString("_id"));
                                task.setTitle(obj.getString("title"));
                                task.setDescription(obj.getString("description"));
                                task.setDay(LocalDate.parse(obj.getString("day")));
                                task.setHour(obj.getString("hour"));
                                task.setDuration(Integer.parseInt(obj.getString("duration")));
                                task.setPriority(Integer.parseInt(obj.getString("priority")));
                                task.setChecked(Boolean.parseBoolean(obj.getString("checked")));

                                taskList.add(task);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if(Objects.nonNull(binding)) {
                            binding.taskCalendar.getAdapter().notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(tasksReq);
    }
}