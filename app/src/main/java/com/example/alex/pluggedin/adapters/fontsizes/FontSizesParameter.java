package com.example.alex.pluggedin.adapters.fontsizes;

import com.example.alex.pluggedin.adapters.ArticleAdapter;
import static com.example.alex.pluggedin.constants.Constants.*;

public class FontSizesParameter implements ChangeableFontSize {

    protected float fontSize;


    public FontSizesParameter(float fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public void changeFontSize(ArticleAdapter.ArticleViewHolder holder) {

        if (fontSize == FONT_SIZE_LARGE) {
            holder.getAuthor().setTextSize(LARGE_AUTHOR);
            holder.getTitleArticle().setTextSize(LARGE_TITLE);
            holder.getTextArticle().setTextSize(LARGE_TEXT);
            holder.getDatePublish().setTextSize(LARGE_DATE);
        }

        if (fontSize == FONT_SIZE_NORMAL) {
            holder.getAuthor().setTextSize(NORMAL_AUTHOR);
            holder.getTitleArticle().setTextSize(NORMAL_TITLE);
            holder.getTextArticle().setTextSize(NORMAL_TEXT);
            holder.getDatePublish().setTextSize(NORMAL_DATE);
        }

        if (fontSize == FONT_SIZE_SMALL) {
            holder.getAuthor().setTextSize(SMALL_AUTHOR);
            holder.getTitleArticle().setTextSize(SMALL_TITLE);
            holder.getTextArticle().setTextSize(SMALL_TEXT);
            holder.getDatePublish().setTextSize(SMALL_DATE);
        }

    }
}
