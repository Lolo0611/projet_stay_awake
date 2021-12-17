package com.example.stay_awake_android.adapters;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.stay_awake_android.AppController;
import com.example.stay_awake_android.R;
import com.example.stay_awake_android.fragments.TaskFormFragment;
import com.example.stay_awake_android.fragments.TaskFormPositionFragment;
import com.example.stay_awake_android.fragments.TaskFragment;
import com.example.stay_awake_android.models.Task;
import com.example.stay_awake_android.databinding.FragmentTaskBinding;
import com.google.android.material.snackbar.Snackbar;

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

        String content = mValues.get(position).getTitle();
        if(content.length() > 25) content = content.substring(0, 26);
        holder.mContentView.setText(content);

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
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

        holder.mButtonPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPositionClick(holder.mItem);
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
        public final ImageButton mButtonPosition;
        public final LinearLayout mLinearLayout;
        public Task mItem;

        public ViewHolder(FragmentTaskBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mButtonDelete = binding.buttonDeleteTask;
            mButtonPosition = binding.buttonPositionTask;
            mLinearLayout = binding.linearLayoutTask;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private void onItemClick(Task item) {
        FragmentManager fragmentManager = mFragment.getActivity().getSupportFragmentManager();
        DialogFragment newFragment = TaskFormFragment.newInstance(mFragment, item.getId(), item.getTitle(), item.getDescription(), item.getPriority(), item.isPermanent());
        newFragment.show(fragmentManager, "dialog");
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
                        Snackbar.make(mFragment.getView(), "La tâche à bien été supprimée.", Snackbar.LENGTH_LONG)
                                .setAction("Suppression", null).show();
                        mFragment.refreshData();
                        Toast.makeText(mFragment.getActivity(), "La tâche à bien été supprimée.", Toast.LENGTH_SHORT).show();
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

    private void onPositionClick(Task item) {
        FragmentManager fragmentManager = mFragment.getActivity().getSupportFragmentManager();
        DialogFragment newFragment = TaskFormPositionFragment.newInstance(mFragment, item.getId());
        newFragment.show(fragmentManager, "dialog");
    }
}