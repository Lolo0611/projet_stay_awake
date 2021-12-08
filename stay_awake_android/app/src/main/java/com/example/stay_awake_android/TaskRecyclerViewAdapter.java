package com.example.stay_awake_android;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.stay_awake_android.fragments.TaskFragment;
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
        holder.mIdView.setText(String.valueOf(position));
        holder.mContentView.setText(mValues.get(position).getTitle());
        holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest deleteTaskReq = new StringRequest(Request.Method.DELETE, AppController.url + route + holder.mItem.getId(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onDeleteClick();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
                    }
                });

                AppController.getInstance().addToRequestQueue(deleteTaskReq);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageButton mButtonDelete;
        public Task mItem;

        public ViewHolder(FragmentTaskBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mButtonDelete = binding.buttonDeleteTask;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private void onDeleteClick() {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Suppression d'un tâche");
        alert.setMessage("Êtes vous sûr de vouloir supprimer cette tâche ?");
        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mFragment.resfreshData();
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