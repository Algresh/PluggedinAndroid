package ru.tulupov.alex.pluggedin.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.pluggedin.R;
import ru.tulupov.alex.pluggedin.SearchResultsActivity;
import ru.tulupov.alex.pluggedin.ShowArticleActivity;
import ru.tulupov.alex.pluggedin.ShowImageActivity;
import ru.tulupov.alex.pluggedin.ShowReviewActivity;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static ru.tulupov.alex.pluggedin.constants.Constants.FONT_SIZE_NORMAL;
import static ru.tulupov.alex.pluggedin.constants.Constants.ID;
import static ru.tulupov.alex.pluggedin.constants.Constants.ID_ARTICLE;
import static ru.tulupov.alex.pluggedin.constants.Constants.ID_REVIEW;
import static ru.tulupov.alex.pluggedin.constants.Constants.KEYWORD_QUERY;
import static ru.tulupov.alex.pluggedin.constants.Constants.SRC_OF_IMAGE;
import static ru.tulupov.alex.pluggedin.constants.Constants.TYPE;
import static ru.tulupov.alex.pluggedin.constants.Constants.TYPE_REVIEW;

public abstract class ShowBaseFragment extends Fragment implements View.OnClickListener {

    protected TextView titleTv;
    protected TextView authorTv;
    protected TextView dateTv;
    protected WebView textWV;
    protected FlowLayout layoutKeywords;
    protected Button tryAgainBtn;

    protected int idArticle;
    protected float fontSize = FONT_SIZE_NORMAL;
    protected boolean chromeTabsFlag = false;

    protected ChangeableTitle changeableTitle;

    protected class MyJavaInterface {
        @android.webkit.JavascriptInterface
        public void getGreeting(String str) {
            Intent intent = new Intent(getActivity(), ShowImageActivity.class);
            intent.putExtra(SRC_OF_IMAGE, str);
            startActivity(intent);
        }
    }

    public interface ChangeableTitle {
        void changeTitleInToolbar(String title);

        void setLatinTitle(String latinTitle);
    }

    protected void showOrHideElements(int visibility) {
        titleTv.setVisibility(visibility);
        authorTv.setVisibility(visibility);
        dateTv.setVisibility(visibility);
        textWV.setVisibility(visibility);
        layoutKeywords.setVisibility(visibility);
        int oppositeVisibility = visibility == View.GONE ? View.VISIBLE : View.GONE;
        tryAgainBtn.setVisibility(oppositeVisibility);
    }

    protected void hideAllElementsShowBtn() {
        showOrHideElements(View.GONE);
    }

    protected void showAllElementHideBtn() {
        showOrHideElements(View.VISIBLE);
    }

    protected String convertHexSubStringsToNormalString(String latinTitle) {
        latinTitle = latinTitle.replace("%C2%AB", "«");//!
        latinTitle = latinTitle.replace("%C2%BB", "»");//!
        latinTitle = latinTitle.replace("%22", "\"");//!
        latinTitle = latinTitle.replace("%27", "\'");//!
        latinTitle = latinTitle.replace("%E2%80%94", "—");//!
        return latinTitle;

    }

    @Override
    public void onPause() {
        super.onPause();
        textWV.onPause();
    }

    protected Map<String, Integer> convertBytesArray(InputStream inputStream) throws IOException {
        Map<String, Integer> map = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read()) != -1) {
                bos.write(read);
            }
            byte[] result = bos.toByteArray();
            bos.close();
            String data = new String(result);
            JSONObject jsonObj = new JSONObject(data);
            map = new HashMap<>();
            map.put(ID_ARTICLE, jsonObj.getInt(ID_ARTICLE));
            map.put(TYPE, jsonObj.getInt(TYPE));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }

        return map;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openArticleTryAgainBtn:
                getArticleById(idArticle);
                break;
            case R.id.openReviewTryAgainBtn:
                getArticleById(idArticle);
                break;
            default:
                TextView textView = (TextView) v.findViewById(R.id.keywordTV);
                if(textView != null) {
                    String keyword = textView.getText().toString();
                    Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                    intent.putExtra(KEYWORD_QUERY, keyword);
                    startActivity(intent);
                }
        }

    }

    protected void openLinkInBrowser(Uri address) {
        Intent intent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(intent);
    }

    protected Callback<Response> getCallbackRedirectLinkToApp() {
        return new Callback<Response>() {
            @Override
            public void success(Response response, Response another) {
                InputStream inputStream;
                int idArticle;
                int type;
                try {
                    inputStream = response.getBody().in();
                    Map<String, Integer> map = convertBytesArray(inputStream);
                    idArticle = map.get(ID_ARTICLE);
                    type = map.get(TYPE);

                    if(idArticle > 0) {
                        Intent intent;
                        if (type != TYPE_REVIEW) {
                            intent = new Intent(getActivity(), ShowArticleActivity.class);
                            intent.putExtra(ID, idArticle);
                        } else {
                            intent = new Intent(getActivity(), ShowReviewActivity.class);
                            intent.putExtra(ID_REVIEW, idArticle);
                        }

                        startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                String str;
                if (checkConnection()) {
                    str = getActivity().getResources()
                            .getString(R.string.something_doesnt_work);
                } else {
                    str = getActivity().getResources()
                            .getString(R.string.no_internet);
                }
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        };
    }

    protected abstract void getArticleById (int idArticle);
    protected abstract void changeFontSize(List<TextView> keywords);

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        changeableTitle = (ChangeableTitle) context;
    }

    protected boolean checkConnection() {
        ConnectivityManager connectChecker = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifiInfo != null && wifiInfo.isConnected();
    }
}
