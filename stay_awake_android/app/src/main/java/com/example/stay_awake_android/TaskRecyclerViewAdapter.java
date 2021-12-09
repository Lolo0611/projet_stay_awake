package com.example.stay_awake_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stay_awake_android.databinding.FragmentTaskFormBinding;
import com.example.stay_awake_android.fragments.TaskFormFragment;
import com.example.stay_awake_android.fragments.TaskFormFragmentArgs;
import com.example.stay_awake_android.fragments.TaskFragment;
import com.example.stay_awake_android.fragments.TaskFragmentDirections;
import com.example.stay_awake_android.models.Task;
import com.example.stay_awake_android.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.stay_awake_android.databinding.FragmentTaskBinding;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private final List<Task> mValues;
    private TaskFragment mFragment;
    private Context context;
    private final String route = "deleteTask/";

    public TaskRecyclerViewAdapter(List<Task> items, TaskFragment fragment) {
        mValues = items;
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(FragmentTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getTitle());

        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(holder.mItem);
            }
        });

        holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClick(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public final ImageButton mButtonDelete;
        public Task mItem;

        public ViewHolder(FragmentTaskBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mButtonDelete = binding.buttonDeleteTask;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private void onItemClick(Task item) {
        Bundle bundle = new Bundle();
        bundle.putString("taskId", item.getId());
        bundle.putString("taskTitle", item.getTitle());
        bundle.putString("taskDescription", item.getDescription());
        bundle.putInt("taskPriority", item.getPriority());
        Navigation.findNavController(mFragment.getView()).navigate(R.id.action_TaskFragment_to_TaskFormFragment, bundle);
    }

    private void onDeleteClick(Task item) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Suppression d'un tâche");
        alert.setMessage("Êtes vous sûr de vouloir supprimer cette tâche ?");
        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest deleteTaskReq = new StringRequest(Request.Method.DELETE, AppController.url + route + item.getId(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mFragment.resfreshData();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                    }
                });

                AppController.getInstance().addToRequestQueue(deleteTaskReq);
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
}