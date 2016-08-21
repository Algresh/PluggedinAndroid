package ru.tulupov.alex.pluggedin.presenters;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static ru.tulupov.alex.pluggedin.constants.Constants.ID_ARTICLE;
import static ru.tulupov.alex.pluggedin.constants.Constants.TYPE;

public class BasePresenter {

    protected String convertHexSubStringsToNormalString(String latinTitle) {
        latinTitle = latinTitle.replace("%C2%AB", "«");//!
        latinTitle = latinTitle.replace("%C2%BB", "»");//!
        latinTitle = latinTitle.replace("%22", "\"");//!
        latinTitle = latinTitle.replace("%27", "\'");//!
        latinTitle = latinTitle.replace("%28", "(");//!
        latinTitle = latinTitle.replace("%29", ")");//!
        latinTitle = latinTitle.replace("%E2%80%94", "—");//!
        return latinTitle;

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
}
