package ru.tulupov.alex.pluggedin.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.pluggedin.R;
import ru.tulupov.alex.pluggedin.adapters.fontsizes.ChangeableFontSize;
import ru.tulupov.alex.pluggedin.models.Article;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>
        implements ChangeableFontSize {

    private List<Article> articles;
    private Context context;
    private View.OnClickListener clickListener;
    private ChangeableFontSize changeableFontSize;
    private int widthScreen;

    public ArticleAdapter(List<Article> articles, Context context, View.OnClickListener clickListener) {
        this.articles = articles;
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setFontSizeParameter(ChangeableFontSize changeableFontSize) {
        this.changeableFontSize = changeableFontSize;
    }

    public void setWidthScreen(int widthScreen) {
        this.widthScreen = widthScreen;
    }

    @Override
    public void changeFontSize(ArticleViewHolder holder) {
        changeableFontSize.changeFontSize(holder);
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

        Picasso.with(context).load(URL_IMAGES + item.getFile())
                .resize(widthScreen, (widthScreen / 3) * 2).into(holder.imageArticle);

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

        public TextView getDatePublish() {
            return datePublish;
        }

        public TextView getTextArticle() {
            return textArticle;
        }

        public TextView getTitleArticle() {
            return titleArticle;
        }

        public TextView getAuthor() {
            return author;
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
