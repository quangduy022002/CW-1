package com.example.hikermanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.myViewHolderObservation> {
    private Context context;
    private List<ObservationModel> observations;
    private String obsDate, hikeId;

    Animation translate_animation;

    ObservationAdapter(Context context, List observations, String obsDate, String hikeId){
        this.context = context;
        this.observations = observations;
        this.obsDate = obsDate;
        this.hikeId = hikeId;
    }
    @NonNull
    @Override
    public ObservationAdapter.myViewHolderObservation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.observation_view, parent, false);
        return new ObservationAdapter.myViewHolderObservation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationAdapter.myViewHolderObservation holder, int position) {
        final ObservationModel observation = observations.get(position);
        holder.textTV.setText(String.valueOf(observation.obsText));
        holder.timeTV.setText(String.valueOf(observation.obsTime));
        holder.commentTV.setText(String.valueOf(observation.obsComment));
        holder.dateTV.setText(obsDate);
    }

    @Override
    public int getItemCount() {
            return observations.size();
    }

    public class myViewHolderObservation extends  RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener  {
        TextView textTV, timeTV, commentTV, dateTV;
        ImageButton imageButton;
        LinearLayout observationLayout;
        public myViewHolderObservation (@NonNull View itemView)  {
            super(itemView);
            textTV = itemView.findViewById(R.id.text_txt);
            timeTV = itemView.findViewById(R.id.time_txt);
            commentTV = itemView.findViewById(R.id.comment_txt);
            dateTV = itemView.findViewById(R.id.date_txt);

            imageButton = itemView.findViewById(R.id.imageButtonAction);
            imageButton.setOnClickListener(this);
            observationLayout = itemView.findViewById(R.id.observationLayout);
            translate_animation = AnimationUtils.loadAnimation(context, R.anim.translate_animation);
            observationLayout.setAnimation(translate_animation);

        }

        @Override
        public void onClick(View view) {
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            final ObservationModel observation  = observations.get(getBindingAdapterPosition());
            if (itemId == R.id.action_popup_edit) {

                Intent intent = new Intent(context, UpdateObservation.class);
                intent.putExtra("obsId", String.valueOf(observation.obsId));
                intent.putExtra("hikeId", hikeId);
                intent.putExtra("text", String.valueOf(observation.obsText));
                intent.putExtra("time", String.valueOf(observation.obsTime));
                intent.putExtra("comment", String.valueOf(observation.obsComment));
                intent.putExtra("date", obsDate);
                context.startActivity(intent);
                return true;
            } else {
                confirmDialog(String.valueOf(observation.obsText));
                return true;
            }
        }

        void confirmDialog(String nameHike) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete " + nameHike);
            builder.setMessage("Are you sure you want to delete " + nameHike + " ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   DatabaseHelper db = new DatabaseHelper(context);
                    int positionToDelete = getBindingAdapterPosition();
                    db.deleteOneObservation(String.valueOf(observations.get(positionToDelete).obsId));
                    observations.remove(positionToDelete);
                    notifyItemRemoved(positionToDelete);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }
    }
}

