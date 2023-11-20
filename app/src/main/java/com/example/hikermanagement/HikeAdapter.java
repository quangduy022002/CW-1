package com.example.hikermanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.myViewHolder> implements Filterable {
    private Context context;

    private List<HikeModel> hikes;
    private List<HikeModel> hikesOld;
    Animation translate_animation;
    HikeAdapter(Context context, List<HikeModel> hikes){
        this.context = context;
        this.hikes = hikes;
        this.hikesOld = hikes;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hike_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final HikeModel hike = hikes.get(position);
        holder.nameTV.setText(String.valueOf(hike.name));
        holder.locationTV.setText(String.valueOf(hike.location));
        holder.dateTV.setText(String.valueOf(hike.date));
        holder.difficultyTV.setText(String.valueOf(hike.difficulty));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                final HikeModel hike = hikes.get(holder.getBindingAdapterPosition());
                intent.putExtra("hikeId", String.valueOf(hike.id));
                intent.putExtra("name", String.valueOf(hike.name));
                intent.putExtra("location", String.valueOf(hike.location));
                intent.putExtra("date", String.valueOf(hike.date));
                intent.putExtra("parkingAvailable", String.valueOf(hike.isParking));
                intent.putExtra("length", String.valueOf(hike.length));
                intent.putExtra("difficulty", String.valueOf(hike.difficulty));
                intent.putExtra("description", String.valueOf(hike.description));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
            return hikes.size();

    }

    public class myViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView  nameTV, locationTV, lengthTV, dateTV, descriptionTV, difficultyTV, isParkingTV;
        ImageButton imageDeleteButton;

        LinearLayout mainLayout;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.name_txt);
            locationTV = itemView.findViewById(R.id.location_txt);
            difficultyTV = itemView.findViewById(R.id.difficulty_txt);
            dateTV = itemView.findViewById(R.id.date_txt);

            imageDeleteButton = itemView.findViewById(R.id.imageDeleteButtonHike);
            imageDeleteButton.setOnClickListener(this);

//            popupMenu.setOnMenuItemClickListener(this);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            translate_animation = AnimationUtils.loadAnimation(context, R.anim.translate_animation);
            mainLayout.setAnimation(translate_animation);

        }

        @Override
        public void onClick(View view) {
            final HikeModel hike = hikes.get(getBindingAdapterPosition());
            confirmDialog(String.valueOf(hike.name));
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
                    db.deleteOne(String.valueOf(hikes.get(positionToDelete).id));
                    hikes.remove(positionToDelete);
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
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase().trim();

                if (searchText.isEmpty()) {
                    hikes = hikesOld;
                } else {
                    List<HikeModel> list = new ArrayList<>();
                    for (HikeModel hike : hikesOld){
                        if(hike.name.toLowerCase().contains(searchText)){
                            list.add(hike);
                        }
                    }

                    hikes = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = hikes;
                Log.d(String.valueOf(filterResults.count), "size");
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hikes = (List<HikeModel>)  filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

