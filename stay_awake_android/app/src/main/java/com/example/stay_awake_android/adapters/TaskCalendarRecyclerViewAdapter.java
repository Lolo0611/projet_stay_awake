package com.example.stay_awake_android.adapters;

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

import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.stay_awake_android.AppController;
import com.example.stay_awake_android.R;
import com.example.stay_awake_android.databinding.FragmentHomeBinding;
import com.example.stay_awake_android.databinding.FragmentTaskBinding;
import com.example.stay_awake_android.databinding.FragmentTaskCalendarBinding;
import com.example.stay_awake_android.fragments.HomeFragment;
import com.example.stay_awake_android.fragments.TaskFragment;
import com.example.stay_awake_android.models.Task;

import java.util.List;

public class TaskCalendarRecyclerViewAdapter extends RecyclerView.Adapter<TaskCalendarRecyclerViewAdapter.ViewHolder> {

    private final List<Task> mValues;
    private HomeFragment mFragment;
    private Context context;

    public TaskCalendarRecyclerViewAdapter(List<Task> items, HomeFragment fragment) {
        mValues = items;
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(FragmentTaskCalendarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getTitle());
        holder.startTime.setText(mValues.get(position).getHour());

        String minutes = mValues.get(position).getHour().substring(3);
        String endTimeHour = String.valueOf(Integer.parseInt((mValues.get(position).getHour().substring(0, 2))) + (mValues.get(position).getDuration()));
        String endTime = (endTimeHour.length() == 1 ? '0' + endTimeHour : endTimeHour) + ':' + minutes;
        holder.endTime.setText(endTime);

        int padding_in_dp = mValues.get(position).getDuration() * 10;
        final float scale = context.getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        holder.title.setPadding(0, padding_in_px, 0, padding_in_px);

        if(mValues.get(position).getPriority() == 2) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_200));
        }

        if(mValues.get(position).getPriority() == 3) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.red_200));
        }

        if(mValues.get(position).isChecked()) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.green_200));
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView startTime;
        public final TextView endTime;
        public final TextView title;
        public final LinearLayout linearLayout;
        public Task mItem;

        public ViewHolder(FragmentTaskCalendarBinding binding) {
            super(binding.getRoot());
            startTime = binding.textViewTaskStartTime;
            endTime = binding.textViewTaskEndTime;
            title = binding.textViewTaskTitle;
            linearLayout = binding.linearLayoutTaskCalendar;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }

    private void onItemClick(Task item) {

    }
}