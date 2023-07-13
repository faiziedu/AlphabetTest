package com.example.quiz;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<List<String>> attemptList;

    public Adapter(List<List<String>> attemptList) {
        this.attemptList = attemptList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift_result, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> attempt = attemptList.get(position);
        holder.attemptNumberTextView.setText("Attempt " + (position + 1));
        String answersText = TextUtils.join("\n", attempt);
        holder.answersTextView.setText(answersText);
    }

    @Override
    public int getItemCount() {
        return attemptList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView attemptNumberTextView, answersTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            attemptNumberTextView = itemView.findViewById(R.id.attemptNumberTextView);
            answersTextView = itemView.findViewById(R.id.answersTextView);
        }
    }
}
