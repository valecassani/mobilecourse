package it.polimi.mobilecourse.expenses;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Matteo on 21/12/2014.
 */
public class ObjDb {



        public ObjDb() {
            elements = new HashMap<String,String>();
        }
        private Map<String,String> elements;
        private void addElement(String key, String value) {
            elements.put(key, value);
        }
        public String get(String name) {
            return elements.get(name);
        }
        public String getFist() {
            return elements.get(elements.keySet().toArray()[0]);
        }
        static ObjDb jsonObjectToEntity(JSONObject jsonObject) {
            ObjDb result = new ObjDb();
            try {
                JSONArray names = jsonObject.names();
                JSONArray values = jsonObject.toJSONArray(names);
                for(int i = 0 ; i < values.length(); i++){
                    result.addElement(names.getString(i), jsonObject.getString(names.getString(i)));
                }
            }
            catch(Exception e) {
                return null;
            }
            return result;
        }
        static ArrayList<ObjDb> jsonArrayToObjDBList(JSONArray jsonArray) {
            if (jsonArray == null) return null;
            ArrayList<ObjDb> result = new ArrayList<ObjDb>();
            for(int i=0; i<jsonArray.length(); i++) {
                try {
                    result.add(jsonObjectToEntity((JSONObject) jsonArray.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        static ArrayList<String> entityListToStringList(ArrayList<ObjDb> list) {
            ArrayList<String> result = new ArrayList<String>();
            for(ObjDb element : list) {
                result.add(element.getFist());
            }
            return result;
        }
        public ArrayList<String> values() {
            return new ArrayList<String>(elements.values());
        }
}
