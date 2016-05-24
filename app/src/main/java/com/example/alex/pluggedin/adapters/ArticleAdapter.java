package com.example.alex.pluggedin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.models.Review;
import com.squareup.picasso.Picasso;

import static  com.example.alex.pluggedin.constants.Constants.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Review> articles;
    private Context context;

    public ArticleAdapter(List<Review> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent , false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Review item = articles.get(position);

        holder.datePublish.setText(convertDate(item.getDatePublish()));
        holder.textArticle.setText(item.getText());
        holder.titleArticle.setText(item.getTitle());
        holder.author.setText(item.getAuthor());

        Picasso.with(context).load(URL_IMAGES + item.getFile())
                .into(holder.imageArticle);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public boolean addNewItems(List<Review> newItems) {
        if(newItems != null && newItems.size() > 0) {
            articles.addAll(newItems);
            notifyItemInserted(articles.size() - 1);
            Log.d(MY_TAG, newItems.size() + "");
            return true;
        } else {
            return false;
        }
    }

    public int getIdLastItem () {
        return articles.get(getItemCount() - 1).getId();
    }

    public void addToTop(List<Review> newItems) {
        articles.addAll(0, newItems);

        notifyItemInserted(0);
    }


    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView datePublish;
        TextView textArticle;
        TextView titleArticle;
        TextView author;
        ImageView imageArticle;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            datePublish = (TextView) itemView.findViewById(R.id.datePublish);
            textArticle = (TextView) itemView.findViewById(R.id.textArticle);
            titleArticle = (TextView) itemView.findViewById(R.id.titleArticle);
            author = (TextView) itemView.findViewById(R.id.author);
            imageArticle = (ImageView) itemView.findViewById(R.id.imageArticle);
        }
    }

    private String convertDate (String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        String strDate = simpleDateFormat.format(calendar.getTime());

        if (date.equals(strDate)){
            return this.context.getResources().getString(R.string.today);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            strDate = simpleDateFormat.format(calendar.getTime());
            if (date.equals(strDate)) {
                return this.context.getResources().getString(R.string.yesterday);
            }
        }
        return date;
    }
}
