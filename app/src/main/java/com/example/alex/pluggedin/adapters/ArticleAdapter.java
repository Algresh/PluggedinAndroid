package com.example.alex.pluggedin.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.models.Article;
import com.squareup.picasso.Picasso;

import static  com.example.alex.pluggedin.constants.Constants.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private Context context;
    private View.OnClickListener clickListener;
    private float fontSize = FONT_SIZE_NORMAL;

    public ArticleAdapter(List<Article> articles, Context context, View.OnClickListener clickListener) {
        this.articles = articles;
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent , false);
        view.setOnClickListener(clickListener);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article item = articles.get(position);

        holder.datePublish.setText(convertDate(item.getDatePublish()));
        holder.textArticle.setText(item.getText());
        holder.titleArticle.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        if(item.getType() == 0) {//если это обзор то в тег записываем отрицательное id чтобы в дальнейшем их различать
            holder.cardView.setTag(item.getId() * (-1));
        } else {
            holder.cardView.setTag(item.getId());
        }

        changeFontSize(holder);

        Picasso.with(context).load(URL_IMAGES + item.getFile()).resize(320,240)
                .into(holder.imageArticle);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public boolean addNewItems(List<Article> newItems) {
        if(newItems != null && newItems.size() > 0) {
            articles.addAll(newItems);
            notifyItemInserted(articles.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    public int getIdLastItem () {
        return articles.get(getItemCount() - 1).getId();
    }

    public int getIdFirstItem () {
        return articles.get(0).getId();
    }

    public void addToTop(List<Article> newItems) {
        articles.addAll(0, newItems);
        notifyItemInserted(0);
    }

    private void changeFontSize(ArticleViewHolder holder) {
        holder.datePublish.setTextSize(holder.datePublish.getTextSize() * fontSize);
        holder.textArticle.setTextSize(holder.textArticle.getTextSize() * fontSize);
        holder.titleArticle.setTextSize(holder.titleArticle.getTextSize() * fontSize);
        holder.author.setTextSize(holder.author.getTextSize() * fontSize);
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView datePublish;
        TextView textArticle;
        TextView titleArticle;
        TextView author;
        ImageView imageArticle;
        CardView cardView;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            datePublish = (TextView) itemView.findViewById(R.id.datePublish);
            textArticle = (TextView) itemView.findViewById(R.id.textArticle);
            titleArticle = (TextView) itemView.findViewById(R.id.titleArticle);
            author = (TextView) itemView.findViewById(R.id.author);
            imageArticle = (ImageView) itemView.findViewById(R.id.imageArticle);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
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
